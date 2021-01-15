package fudan.se.lab4;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ProfitStrategyType2Tests {
//    private LoggerService loggerService = new LoggerServiceImpl();
    private ProfitStrategy profitStrategy;
    private DrinkRepository drinkRepository;
    private Rule ruleAllKind;//对所有商品打折：包括购买一个茶或咖啡类时8.5折以及双十一5折
    private Rule rulePriceCanAdd;//对某种商品价格打折:可叠加 考虑可打折的商品不是所有都存在
//    private Rule rulePriceNotAdd;//对某种商品价格打折:不可叠加 max会设置成1 一般没有这种情况
    private Rule ruleKindCanAdd;//对某种特定（限定size）商品按杯数有优惠基础价格：大杯速溶咖啡八折 可叠加
    private Rule ruleKindNotAdd;//对某种特定（限定size）商品按杯数有优惠基础价格：大杯速溶咖啡八折 不可叠加 只有第一次八折
    private Rule ruleKindCanAdd2;//对某种商品按杯数有优惠基础价格：第二件半价可叠加
    private Rule ruleKindCanAdd3;//对多种商品按杯数有优惠基础价格：有很多商品都可以半价优惠
    private Rule ruleKindNotAdd2;//对某种商品按杯数有优惠：不可叠加 max会是1 只有第一个第二件半价会便宜
    private Rule ruleKindNotAdd3;//对多种商品按杯数有优惠基础价格：但是只有第一次符合的可以半价优惠
    @Before
    public void initial() {
        profitStrategy = new ProfitStrategyImplType2();
        drinkRepository = new DrinkRepositoryImpl();

        List<RuleRepositoryImpl.Item> discountRange1 = new ArrayList<>();
        List<Drinks> drinks11 = new ArrayList<>();
        //大杯速溶咖啡两杯八折
        Drinks tea1 = drinkRepository.getDrink("espresso");
        tea1.setSize(3);
        drinks11.add(tea1);
        RuleRepositoryImpl.Item discount1 = new RuleRepositoryImpl().new Item(1, 2, drinks11);
        discountRange1.add(discount1);
        ruleKindCanAdd = new Rule(0, 0, 2, 0.8, true, null, null, 1, null, discountRange1, null, 110);//"kindCanAdd"
        ruleKindNotAdd = new Rule(0, 0, 2, 0.8, false, null, null, 1, null, discountRange1, null, 111);//"kindNotAdd"

        //买二第二杯半价
        List<RuleRepositoryImpl.Item> discountRange12 = new ArrayList<>();
        List<Drinks> drinks12 = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Drinks teac = drinkRepository.getDrink("cappuccino");
            teac.setSize(i);
            drinks12.add(teac);
        }
        RuleRepositoryImpl.Item discount12 = new RuleRepositoryImpl().new Item(1, 1, drinks12);
        discountRange12.add(discount12);
        ruleKindCanAdd2 = new Rule(0, 0, 2, 0.5, true, null, null, 1, null, discountRange12, null, 104);//"kindCanAdd2"

        //卡布奇诺只有一次半价的机会 第二杯半价不可叠加
        ruleKindNotAdd2 = new Rule(0, 0, 2, 0.5, true, null, null, 1, null, discountRange12, null, 105);//kindNotAdd2

        //会对大杯绿茶和大杯红茶都半价 可叠加
        List<RuleRepositoryImpl.Item> discountRange2 = new ArrayList<>();
        List<Drinks> drinks21 = new ArrayList<>();
        List<Drinks> drinks22 = new ArrayList<>();
        Drinks tea21 = drinkRepository.getDrink("greenTea");
        tea21.setSize(3);
        drinks21.add(tea21);
        Drinks tea22 = drinkRepository.getDrink("redTea");
        tea22.setSize(3);
        drinks22.add(tea22);
        RuleRepositoryImpl.Item discount21 = new RuleRepositoryImpl().new Item(1, 1, drinks21);
        RuleRepositoryImpl.Item discount22 = new RuleRepositoryImpl().new Item(1, 1, drinks22);
        discountRange2.add(discount21);
        discountRange2.add(discount22);
        ruleKindCanAdd3 = new Rule(0, 0, 2, 0.5, true, null, null, 1, null, discountRange2, null, 106);//kindCanAdd3
        ruleKindNotAdd3 = new Rule(0, 0, 2, 0.5, false, null, null, 1, null, discountRange2, null, 107); //kindNotAdd3

        //对所有商品打折85折
        ruleAllKind = new Rule(0, 0, 2, 0.85, true, null, null, 0, null, null, null, 108);//allKind

        //对某种商品打折85折可叠加
        List<RuleRepositoryImpl.Item> discountRange4 = new ArrayList<>();
        List<Drinks> drinks4 = new ArrayList<>();

        //只对茶打折基础价格8折 可叠加
        for (int i = 1; i < 4; i++) {
            Drinks teag = drinkRepository.getDrink("greenTea");
            teag.setSize(i);
            drinks4.add(teag);
            Drinks tear = drinkRepository.getDrink("redTea");
            tear.setSize(i);
            drinks4.add(tear);
        }
        RuleRepositoryImpl.Item discount4 = new RuleRepositoryImpl().new Item(0, 0, drinks4);
        discountRange4.add(discount4);
        rulePriceCanAdd = new Rule(0, 0, 2, 0.8, true, null, null, 1, null, discountRange4, null, 109);//priceCanAdd

       //不可叠加
//        rulePriceNotAdd = new Rule(0, 0, 2, 0.8, false, null, null, 1, null, discountRange4, null, "priceCanAdd");

    }


    @Test
    //一杯咖啡或茶，双十一 订单总价打8.5折
    public void testAllKind() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), 2));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, ruleAllKind, 1);
        assertTrue(equals(result, new RuleResult(ruleAllKind, 15, "allKind")));
    }

    @Test
    //对某类商品价格打折:可叠加：对茶打8折 红茶18 绿茶16 18+16=34 34*3*0.2=20.4
    public void testPriceCanAdd() {
        //虽然可以打折，但是没有可以打折的商品
        List<OrderItem> orderItems1 = new ArrayList<>();
        orderItems1.add(new OrderItem("espresso", new ArrayList<>(), 1));
        RuleContext ruleContext = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result0 = profitStrategy.profitProcess(ruleContext, rulePriceCanAdd, 1);
        assertTrue(equals(result0, new RuleResult(rulePriceCanAdd, 0, "priceCanAdd")));

        orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        RuleContext ruleContext3 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result3 = profitStrategy.profitProcess(ruleContext3, rulePriceCanAdd, 1);
        assertTrue(equals(result3, new RuleResult(rulePriceCanAdd, 3.2, "priceCanAdd")));

        //3杯红茶和4杯绿茶 基础价格打折
        for (int i = 1; i < 4; i++) {
            orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), i));
            orderItems1.add(new OrderItem("redTea", new ArrayList<>(), i));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, rulePriceCanAdd, 1);
        assertTrue(equals(result, new RuleResult(rulePriceCanAdd, 23.6, "priceCanAdd")));

        //多两杯其他的茶
        orderItems1.add(new OrderItem("espresso", new ArrayList<>(), 1));
        orderItems1.add(new OrderItem("espresso", new ArrayList<>(), 2));
        RuleContext ruleContext2 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result2 = profitStrategy.profitProcess(ruleContext2, rulePriceCanAdd, 1);
        assertTrue(equals(result2, new RuleResult(rulePriceCanAdd, 23.6, "priceCanAdd")));

    }

    @Test
    //对单个特定商品有优惠 大杯速溶咖啡两杯八折 一杯基础价格20；两杯便宜8元 4杯便宜16元
    public void testKindCanAdd() {
        //多次优惠
        List<OrderItem> orderItems = new ArrayList<>();
        //可以打折但是没有这个商品 订单里有其它size的咖啡
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        RuleContext ruleContext2 = new RuleContext(new Order("1", orderItems), null, 100);
        RuleResult result2 = profitStrategy.profitProcess(ruleContext2, ruleKindCanAdd, 2);
        assertTrue(equals(result2, new RuleResult(ruleKindCanAdd, 0, "kindCanAdd")));


        //三杯大杯但只有两杯有优惠
        for (int i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        RuleContext ruleContext = new RuleContext(new Order("1", orderItems), null, 100);
        RuleResult result0 = profitStrategy.profitProcess(ruleContext, ruleKindCanAdd, 1);
        assertTrue(equals(result0, new RuleResult(ruleKindCanAdd, 8, "kindCanAdd")));

        //4杯多次优惠
        List<OrderItem> orderItems1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderItems1.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, ruleKindCanAdd, 2);
        assertTrue(equals(result, new RuleResult(ruleKindCanAdd, 16, "kindCanAdd")));
    }

    @Test
    //对单个特定商品有优惠 大杯速溶咖啡两杯八折 一杯基础价格20；两杯便宜8元 4杯便宜16元
    public void testKindNotAdd() {
        //多次优惠
        List<OrderItem> orderItems = new ArrayList<>();
        //可以打折但是没有这个商品 订单里有其它size的咖啡
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        RuleContext ruleContext2 = new RuleContext(new Order("1", orderItems), null, 100);
        RuleResult result2 = profitStrategy.profitProcess(ruleContext2, ruleKindNotAdd, 2);
        assertTrue(equals(result2, new RuleResult(ruleKindNotAdd, 0, "kindNotAdd")));


        //三杯大杯但只有两杯有优惠
        for (int i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        RuleContext ruleContext = new RuleContext(new Order("1", orderItems), null, 100);
        RuleResult result0 = profitStrategy.profitProcess(ruleContext, ruleKindNotAdd, 1);
        assertTrue(equals(result0, new RuleResult(ruleKindNotAdd, 8, "kindNotAdd")));

        //4杯还是只有1次优惠 也就是优惠两杯
        List<OrderItem> orderItems1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            orderItems1.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, ruleKindNotAdd, 1);
        assertTrue(equals(result, new RuleResult(ruleKindNotAdd, 8, "kindNotAdd")));
    }

    @Test
    //对某类商品打折 可叠加
    public void testKindCanAdd2() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        //三杯不同杯型的卡布奇诺 半价 基础价格22元 size大小2，4，6
        for (int i = 1; i < 4; i++) {
            orderItems1.add(new OrderItem("cappuccino", new ArrayList<>(), i));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, ruleKindCanAdd2, 1);
        assertTrue(equals(result, new RuleResult(ruleKindCanAdd2, 11, "kindCanAdd2")));

        //四杯 半价便宜
        orderItems1.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        RuleContext ruleContext2 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result2 = profitStrategy.profitProcess(ruleContext2, ruleKindCanAdd2, 2);
        assertTrue(equals(result2, new RuleResult(ruleKindCanAdd2, 22, "kindCanAdd2")));


        //5杯饮料4杯卡布奇诺 半价便宜
        orderItems1.add(new OrderItem("espresso", new ArrayList<>(), 1));
        RuleContext ruleContext3 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result3 = profitStrategy.profitProcess(ruleContext3, ruleKindCanAdd2, 2);
        assertTrue(equals(result3, new RuleResult(ruleKindCanAdd2, 22, "kindCanAdd2")));

    }

    //对某种商品按杯数优惠 不可叠加
    @Test
    public void testKindNotAdd2() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        //三杯不同杯型的卡布奇诺 半价 基础价格22元 size大小2，4，6
        for (int i = 1; i < 4; i++) {
            orderItems1.add(new OrderItem("cappuccino", new ArrayList<>(), i));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, ruleKindNotAdd2, 1);
        assertTrue(equals(result, new RuleResult(ruleKindNotAdd2, 11, "kindNotAdd2")));

        //四杯 半价便宜
        orderItems1.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        RuleContext ruleContext2 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result2 = profitStrategy.profitProcess(ruleContext2, ruleKindNotAdd2, 1);
        assertTrue(equals(result2, new RuleResult(ruleKindNotAdd2, 11, "kindNotAdd2")));


        //5杯饮料4杯卡布奇诺 半价便宜
        orderItems1.add(new OrderItem("espresso", new ArrayList<>(), 1));
        RuleContext ruleContext3 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result3 = profitStrategy.profitProcess(ruleContext3, ruleKindNotAdd2, 1);
        assertTrue(equals(result3, new RuleResult(ruleKindNotAdd2, 11, "kindNotAdd2")));

    }


    @Test
    //对多类商品打折 大红茶和大绿茶第二杯半价
    public void testKindCanAdd3() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        //有其他不可以打折的商品
        orderItems1.add(new OrderItem("redTea", new ArrayList<>(), 3));
        orderItems1.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        orderItems1.add(new OrderItem("espresso", new ArrayList<>(), 1));
        //假设可以有大杯红茶和大杯绿茶的2次半价机会，但是只有一杯大杯红茶
        RuleContext ruleContext3 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result3 = profitStrategy.profitProcess(ruleContext3, ruleKindCanAdd3, 2);
        assertTrue(equals(result3, new RuleResult(ruleKindCanAdd3, 9, "kindCanAdd3")));

        //假设只有一次半价机会 七杯红茶六杯绿茶 3杯大杯红茶两杯大杯绿茶 大杯半价 16 18 半价 8+9=17
        for (int i = 1; i < 4; i++) {
            orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), i));
            orderItems1.add(new OrderItem("redTea", new ArrayList<>(), i));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, ruleKindCanAdd3, 1);
        assertTrue(equals(result, new RuleResult(ruleKindCanAdd3, 17, "kindCanAdd3")));

        // 假设两次半价机会 13杯 17*2
        for (int i = 1; i < 4; i++) {
            orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), i));
            orderItems1.add(new OrderItem("redTea", new ArrayList<>(), i));
        }
        RuleContext ruleContext2 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result2 = profitStrategy.profitProcess(ruleContext2, ruleKindCanAdd3, 2);
        assertTrue(equals(result2, new RuleResult(ruleKindCanAdd3, 34, "kindCanAdd3")));


    }

    @Test
    //对多类商品打折 大红茶和大绿茶第二杯半价 不可叠加
    public void testKindNotAdd3() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        //有其他不可以打折的商品
        orderItems1.add(new OrderItem("redTea", new ArrayList<>(), 3));
        orderItems1.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        orderItems1.add(new OrderItem("espresso", new ArrayList<>(), 1));
        //假设可以有大杯红茶和大杯绿茶的2次半价机会，但是只有一杯大杯红茶
        RuleContext ruleContext3 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result3 = profitStrategy.profitProcess(ruleContext3, ruleKindNotAdd3, 1);
        assertTrue(equals(result3, new RuleResult(ruleKindNotAdd3, 9, "kindNotAdd3")));

        //假设只有一次半价机会 七杯红茶六杯绿茶 3杯大杯红茶两杯大杯绿茶 大杯半价 16 18 半价 8+9=17
        for (int i = 1; i < 4; i++) {
            orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), i));
            orderItems1.add(new OrderItem("redTea", new ArrayList<>(), i));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, ruleKindNotAdd3, 1);
        assertTrue(equals(result, new RuleResult(ruleKindNotAdd3, 17, "kindNotAdd3")));

        // 假设两次半价机会 13杯 但是不可以叠加
        for (int i = 1; i < 4; i++) {
            orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), i));
            orderItems1.add(new OrderItem("redTea", new ArrayList<>(), i));
        }
        RuleContext ruleContext2 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result2 = profitStrategy.profitProcess(ruleContext2, ruleKindNotAdd3, 1);
        assertTrue(equals(result2, new RuleResult(ruleKindNotAdd3, 17, "kindNotAdd3")));


    }



    private boolean equals(RuleResult result1, RuleResult result2) {
        Rule rule1 = result1.getRule();
        Rule rule2 = result2.getRule();
        boolean r = result1.getDiscount() == result2.getDiscount();
        boolean r1 = result1.getRuleDescription().equals(result2.getRuleDescription());
        return rule1 != null && rule1.equals(rule2) && result1.getDiscount() == result2.getDiscount() && result1.getRuleDescription().equals(result2.getRuleDescription());
    }
}
