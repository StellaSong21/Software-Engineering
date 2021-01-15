package fudan.se.lab4;

//import fudan.se.lab4.service.PriceService;
//import fudan.se.lab4.service.impl.PriceServiceImpl;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTests {
    private final String RULE_DES = "RULE_DES_";
    private OrderServiceImpl orderService;
    private PaymentInfo paymentInfo;
    private List<String> EMPTYMSGS = new ArrayList<>();
//    private LoggerService loggerService = new LoggerServiceImpl();
//    private PriceService priceService = new PriceServiceImpl();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        orderService = new OrderServiceImpl();
////        priceService.changeCurrentCurrency("RMB");
//        EnvironmentContext.getEnvironmentContext().changeCurrency("RMB");
//        EnvironmentContext.getEnvironmentContext().changeLanguage("en");
    }

    @After
    public void tearDown() {
        orderService = null;
        paymentInfo = null;
        EMPTYMSGS = null;
    }

    private OrderContext packEnRMBOrderContext(Order order){
        OrderContext ret = new OrderContext("en","RMB",order);
        return ret;
    }
    private OrderContext packEnHDKOrderContext(Order order){
        OrderContext ret = new OrderContext("en","HDK",order);
        return ret;
    }

    // 测试订单为null
    @Test
    public void testOrderNull() {
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(InfoConstant.ORDER_WRONG);

        assertNull(orderService.pay(packEnRMBOrderContext(null)));
    }


    // 测试订单中饮品列表为null
    @Test
    public void testOrderItemNull() {
        assertNull(orderService.pay(packEnRMBOrderContext( new Order("nullOrderItem", null))));
        //test OrderItem null
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(InfoConstant.ORDER_WRONG);
    }

    // 测试订单中饮品数量为空
    @Test
    public void testOderItemEmpty() {
        List<OrderItem> orderItems = new ArrayList<>();
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(InfoConstant.ORDER_WRONG);
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("0", orderItems)) );
        assertNull(paymentInfo);
    }

    // 测试订单中饮品数目不同的情况：1，3
    @Test
    public void testOrderItemByNum() {
        List<OrderItem> orderItems = new ArrayList<>();

        //test 1 OrderItem
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        Order order;
        order = new Order("1", orderItems);
        paymentInfo = orderService.pay(new OrderContext("en","RMB",order));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        //test 3 OrderItem
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("3", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(66, 0, 66, EMPTYMSGS)));
    }

    // 测试饮品name为null
    @Test
    public void testDrinkNameNull() {
        List<OrderItem> orderItems = new ArrayList<>();
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, "null"));
        orderItems.add(new OrderItem(null, new ArrayList<>(), 1));
        assertNull(orderService.pay(packEnRMBOrderContext( new Order("nullDrinkName", orderItems))));
    }

    // 测试饮品name错误
    @Test
    public void testDrinkNameError() {
        List<OrderItem> orderItems = new ArrayList<>();
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, "error"));
        orderItems.add(new OrderItem("error", new ArrayList<>(), 1));
        assertNull(orderService.pay(packEnRMBOrderContext(new Order("errorName", orderItems)) ));
    }

    // 测试四种饮品的价格
    @Test
    public void testDrinkKinds() {
        List<OrderItem> orderItems = new ArrayList<>();
        // cappuccino
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext( new Order("cappuccino", orderItems)));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(24, 0, 24, EMPTYMSGS)));

        // espresso
        orderItems.clear();
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("espresso", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        // redTea
        orderItems.clear();
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("redTea", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(20, 0, 20, EMPTYMSGS)));

        // greenTea
        orderItems.clear();
        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("greenTea", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(18, 0, 18, EMPTYMSGS)));
    }

    // 测试订单中饮品的不同的组合：1.卡布奇诺+浓缩咖啡，2.红茶+绿茶，3.咖啡+茶
    @Test
    public void testOrderItemCombination() {
        List<OrderItem> orderItems = new ArrayList<>();

        //1.
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("coffee", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(46, 0, 46, EMPTYMSGS)));

        //2.
        orderItems.clear();
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("tea", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(38, 0, 38, EMPTYMSGS)));
    }

    // 测试饮品数量size 不存在为null的情况，以下只分过大和过小的情况
    @Test
    public void testOrderItemSizeSmall() {
        List<OrderItem> orderItems = new ArrayList<>();
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(loggerService.log("INGREDIENT_INVALID"));
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), Integer.MIN_VALUE));
        assertNull(orderService.pay(packEnRMBOrderContext( new Order("smallSize", orderItems))));
    }

    @Test
    public void testOrderItemSizeLarge() {
        List<OrderItem> orderItems = new ArrayList<>();
//        thrown.expect(RuntimeException.class);
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), Integer.MAX_VALUE));
        assertNull(orderService.pay(packEnRMBOrderContext(new Order("largeSize", orderItems))));
    }

    // 对咖啡三种杯型的测试
    @Test
    public void testCoffeeSizeCorrect() {
        List<OrderItem> orderItems = new ArrayList<>();

        // size = 1
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("size=1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        // size = 2
        orderItems.clear();
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 2));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("size=2", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(24, 0, 24, EMPTYMSGS)));

        // size = 3
        orderItems.clear();
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("size=3", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(26, 0, 26, EMPTYMSGS)));
    }

    // 对茶三种杯型的测试
    @Test
    public void testTeaSizeRight() {
        List<OrderItem> orderItems = new ArrayList<>();

        // size = 1
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("size=1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(20, 0, 20, EMPTYMSGS)));

        // size = 2
        orderItems.clear();
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 2));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("size=2", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        // size = 3
        orderItems.clear();
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("size=3", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(23, 0, 23, EMPTYMSGS)));
    }

    // 测试配料为null
    @Test
    public void testOrderIngredientNull() {
        List<OrderItem> orderItems = new ArrayList<>();
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(InfoConstant.INGREDIENT_INVALID);
        orderItems.add(new OrderItem("espresso", null, 1));
        assertNull(orderService.pay(packEnRMBOrderContext(new Order("nullIngredient", orderItems))));
    }

    // 对配料列表不同长度的测试
    @Test
    public void testOrderIngredientByNum() {
        //1.配料列表的长度为0
        //2.配料列表的长度为1
        //3.配料列表的长度为3

        List<OrderItem> orderItems = new ArrayList<>();

        //1.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        //2.
        orderItems.clear();
        List<Ingredient> ingredients2 = new ArrayList<>();
        ingredients2.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("espresso", ingredients2, 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(23, 0, 23, EMPTYMSGS)));

        //3.
        orderItems.clear();
        List<Ingredient> ingredients3 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ingredients3.add(new Ingredient("cream", 1));
        }
        orderItems.add(new OrderItem("espresso", ingredients3, 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(25, 0, 25, EMPTYMSGS)));
    }

    // 测试配料name为null
    @Test
    public void testOrderIngredientNameNull() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(InfoConstant.INGREDIENT_INVALID);
        ingredients.add(new Ingredient(null, 1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        assertNull(orderService.pay(packEnRMBOrderContext(new Order("nullIngredientName", orderItems)) ));
    }

    // 测试配料name错误
    @Test
    public void testOrderIngredientNameError() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(InfoConstant.INGREDIENT_INVALID);
        ingredients.add(new Ingredient("error", 1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        assertNull(orderService.pay(packEnRMBOrderContext(new Order("nullIngredientName", orderItems)) ));
    }

    // 测试配料name正确，只测一种，原因是在一个csv文件中
    @Test
    public void testOrderIngredientNameCorrect() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("nullIngredientName", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(23, 0, 23, EMPTYMSGS)));
    }

    // 测试配料数目为负数:-1
    @Test
    public void testIngredientSizeSmall() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
//        thrown.expect(RuntimeException.class);
//        thrown.expectMessage(InfoConstant.INGREDIENT_INVALID);
        ingredients.add(new Ingredient("cream", -1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        assertNull(orderService.pay(packEnRMBOrderContext( new Order("1", orderItems))));
    }

    // 测试配料的不同size
    @Test
    public void testIngredientByNum() {
        List<OrderItem> orderItems = new ArrayList<>();

        //test 0 ingredient
        List<Ingredient> ingredients1 = new ArrayList<>();
        ingredients1.add(new Ingredient("cream", 0));
        orderItems.add(new OrderItem("espresso", ingredients1, 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("0", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, EMPTYMSGS)));

        //test 1 ingredient
        orderItems.clear();
        List<Ingredient> ingredients2 = new ArrayList<>();
        ingredients2.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("espresso", ingredients2, 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(23, 0, 23, EMPTYMSGS)));

        //test 3 ingredient
        orderItems.clear();
        List<Ingredient> ingredients3 = new ArrayList<>();
        ingredients3.add(new Ingredient("cream", 3));
        orderItems.add(new OrderItem("espresso", ingredients3, 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("3", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(25, 0, 25, EMPTYMSGS)));
    }

    // 测试不同配料类型的组合：milk+cream
    @Test
    public void testIngredientCombination() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("cream", 1));
        ingredients.add(new Ingredient("milk", 1));
        orderItems.add(new OrderItem("espresso", ingredients, 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("0", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(24.2, 0, 24.2, EMPTYMSGS)));
    }

    // 对浓缩咖啡的优惠方式一的测试
    @Test
    public void testEspressoPromotion() {
        //1.只有一大杯
        //2.一大杯加一小杯
        //3.两大杯，其中一杯加入配料
        //4.三大杯

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext( new Order("1", orderItems)));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(26, 0, 26, msgs)));

        //2.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("2", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(48, 0, 48, msgs)));

        //3.
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 1));
        orderItems.remove(1);
        List<Ingredient> ingredients3 = new ArrayList<>();
        ingredients3.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("espresso", ingredients3, 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext( new Order("3", orderItems)));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(53, 8, 45, msgs)));

        //4.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext( new Order("4", orderItems)));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(79, 8, 71, msgs)));
    }

    // 对卡布奇诺的优惠方式一的测试
    @Test
    public void testCappuccinoPromotion() {
        //1.一小杯
        //2.一小杯加一大杯
        //3.两个小杯，其中一个加入配料
        //4.三杯
        //5.四小杯

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(24, 0, 24, msgs)));

        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 3));
        //2.
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext( new Order("2", orderItems)));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(52, 11, 41, msgs)));

        //3.
        orderItems.remove(1);
        List<Ingredient> ingredients3 = new ArrayList<>();
        ingredients3.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("cappuccino", ingredients3, 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext( new Order("3", orderItems)));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(49, 11, 38, msgs)));

        //4.
        orderItems.remove(1);
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 2));
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("4", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(78, 11, 67, msgs)));

        //5.
        orderItems.clear();
        for (int i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("4", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(96, 22, 74, msgs)));

    }

    // 对红茶的优惠方式一的测试
    @Test
    public void testRedTeaPromotion() {
        //1.3杯小杯红茶
        //2.4杯小杯红茶
        //3.9杯小杯红茶
        //4.4杯红茶，其中一杯为大杯
        //5.4杯红茶，其中一杯加入配料

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        for (int i = 0; i < 3; i++) {
            orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(60, 0, 60, msgs)));

        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 2));
        //4.
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext( new Order("4", orderItems)));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(83, 18, 65, msgs)));

        //5.
        orderItems.remove(3);
        List<Ingredient> ingredients5 = new ArrayList<>();
        ingredients5.add(new Ingredient("cream", 1));
        orderItems.add(new OrderItem("redTea", ingredients5, 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext( new Order("5", orderItems)));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(81, 18, 63, msgs)));

        //2.
        orderItems.remove(3);
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("2", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(80, 18, 62, msgs)));

        //3.
        for (int i = 0; i < 5; i++) {
            orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("3", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(180, 36, 144, msgs)));
    }

    // 对绿茶的优惠方式一的测试
    @Test
    public void testGreenTeaPromotion() {
        //1.4杯小杯绿茶
        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 2));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(72, 16, 56, msgs)));
    }

    //对两种茶的优惠方式的测试
    @Test
    public void testTeaCombinationPromotion() {
        //1.满减，八杯茶，一杯红茶
        //2.满减，八杯茶，两杯红茶
        //3.满减，九杯茶，一杯红茶
        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 2));

        //1.
        for (int i = 0; i < 7; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(146, 34, 112, msgs)));

        //3.
        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext( new Order("1", orderItems)));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(164, 34, 130, msgs)));

        //2.
        orderItems.remove(0);
        orderItems.remove(0);
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(148, 36, 112, msgs)));
    }

    // 对满100减30的优惠方式的测试
    @Test
    public void testFullReductionPromotion() {
        //1.<100
        //2.=100
        //3.>100
        //4.220

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(22, 0, 22, msgs)));

        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 4));
        //2.
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 2));
        for (int i = 0; i < 3; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("2", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(100, 30, 70, msgs)));

        //3.
        orderItems.clear();
        for (int i = 0; i < 5; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("3", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(110, 30, 80, msgs)));

        //4.
        for (int i = 0; i < 5; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("4", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(220, 60, 160, msgs)));
    }

    // 对组合优惠中不同饮品组合优惠的测试
    @Test
    public void testCombinationPromotion() {
        //1.两种咖啡
        //2.卡布奇诺+茶
        //3.浓缩咖啡+茶
        //4.三种

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        int i = 0;

        //1.

        for (; i < 2; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        for (i = 0; i < 6; i++) {
            orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+1));
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+3));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(196, 41, 155, msgs)));

        //2.
        msgs.clear();

        orderItems.clear();
        for (i = 0; i < 8; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        for (i = 0; i < 2; i++) {
            orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("2", orderItems)) );
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+2));
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+3));
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+6));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(192, 71.8, 120.2, msgs)));//43

        //3.
        msgs.clear();

        orderItems.clear();
        for (i = 0; i < 8; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        for (i = 0; i < 2; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("3", orderItems)) );
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+1));
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+2));
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+6));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(196, 69.4, 126.6, msgs)));//40

        //4.
        orderItems.clear();
        msgs.clear();
        for (i = 0; i < 2; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        }
        for (i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        for (i = 0; i < 2; i++) {
            orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("4", orderItems)) );
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+1));
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+2));
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+3));
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+6));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(172, 60.8, 111.2, msgs)));//35
    }

    // 两种优惠都有，最优选择的测试
    @Test
    public void testOptimalPromotion() {
        //1.两个优惠都有，但是满减最优：5杯红茶
        //2.两个优惠都有，但是组合优惠最优：
        //3.两个优惠都有，但是两个优惠方式一样

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        //1.
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+4));
        for (int i = 0; i < 5; i++) {
            orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(100, 30, 70, msgs)));

        //2.
        orderItems.clear();
        msgs.clear();
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+2));
        for (int i = 0; i < 7; i++) {
            orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        orderItems.add(new OrderItem("redTea", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(146, 34, 112, msgs)));

        //3.
        orderItems.clear();
        msgs.clear();
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+1));
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+3));
        for (int i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        }
        for (int i = 0; i < 2; i++) {
            orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));

        }
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("3", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(148, 30, 118, msgs)));
    }

    @Test
    public void testTime() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(26, 0, 26, msgs)));
    }

    //todo 测试满足时间11-11
    @Test
    public void testTimeCorrect() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();

        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("1", orderItems)) );
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+5));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(26, 13, 13, msgs)));
    }

    //至少有一茶类和一咖啡类，打八五折
    @Test
    public void testTeaAndCoffee() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<String> msgs = new ArrayList<>();
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+6));
        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("tea&coffee", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(40, 6, 34, msgs)));

        orderItems.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnRMBOrderContext(new Order("tea&coffee", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(80, 12, 68, msgs)));
    }

//    测试切换成当前店铺地区不支持的币种
    @Test
    public void testSetCurrencyWrong(){
        //不支持泰铢
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        Order order = new Order("cappuccino", orderItems);
        OrderContext orderContext = new OrderContext("en","THB",order);
        assertNull(orderService.pay(orderContext));
    }

    //测试港币
    @Test
    public void testHDK(){
        List<OrderItem> orderItems = new ArrayList<>();
        //测试单饮料价格
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 1));
        paymentInfo = orderService.pay(packEnHDKOrderContext(new Order("cappuccino", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(30.0, 0, 30.0, EMPTYMSGS)));

        //测试带配料饮料价格
        orderItems = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("cream", 1));//1*1.25
        ingredients.add(new Ingredient("milk", 1));//1.2*1.25
        orderItems.add(new OrderItem("espresso", ingredients, 1)); //22*1.25
        paymentInfo = orderService.pay(packEnHDKOrderContext(new Order("0", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(30.25, 0, 30.25, EMPTYMSGS)));

        //3杯大Espresso-基础价格打折测试
        List<String> msgs = new ArrayList<>();
        ingredients = new ArrayList<>();
        orderItems = new ArrayList<>();
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 1));
        ingredients.add(new Ingredient("cream", 1));//1*1.25
        orderItems.add(new OrderItem("espresso", ingredients, 3)); //26*1.25 + 1*1.25
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3)); //26*1.25
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 3)); //26*1.25
        paymentInfo = orderService.pay(packEnHDKOrderContext(new Order("4", orderItems) ));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(98.75, 10.0, 88.75, msgs)));

        //一小杯加一大杯Cappuccino-基础价格2打折测试
        msgs = new ArrayList<>();
        orderItems = new ArrayList<>();
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 3));
        orderItems.add(new OrderItem("cappuccino", new ArrayList<>(), 3)); //28 * 1.25
        orderItems.add(new OrderItem("cappuccino", ingredients, 1)); //25 * 1.25
        paymentInfo = orderService.pay(packEnHDKOrderContext(new Order("2", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(66.25, 13.75, 52.5, msgs)));

        //4杯小红茶-基础价格买增测试
        msgs = new ArrayList<>();
        orderItems = new ArrayList<>();
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 2));
        for (int i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("greenTea", ingredients, 1));//19*1.25
        }
        paymentInfo = orderService.pay(packEnHDKOrderContext(new Order("1", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(95, 20, 75, msgs)));

        //买100-30-总价满减测试
        msgs = new ArrayList<>();
        orderItems = new ArrayList<>();
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES + 4));
        for (int i = 0; i < 4; i++) {
            orderItems.add(new OrderItem("redTea", ingredients, 1));//21*1.25
        }
        paymentInfo = orderService.pay(packEnHDKOrderContext(new Order("1", orderItems) ));
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(105.0, 30, 75, msgs)));

        //至少一茶一咖啡85折-总价打折测试
        orderItems = new ArrayList<>();
        msgs = new ArrayList<>();
        msgs.add(EnvironmentContext.getEnvironmentContext().getBundle().getString(RULE_DES+6));
        orderItems.add(new OrderItem("greenTea", ingredients, 1));//19*1.25
        orderItems.add(new OrderItem("espresso", new ArrayList<>(), 1));//22*1.25
        paymentInfo = orderService.pay(packEnHDKOrderContext(new Order("tea&coffee", orderItems)) );
        assertTrue(paymentInfoEquals(paymentInfo, new PaymentInfo(51.25, 7.69, 43.56, msgs)));
    }

    private boolean paymentInfoEquals(PaymentInfo pay1, PaymentInfo pay2) {
        if (pay1 == pay2) {
            return true;
        }
        if (null == pay1 || null == pay2) {
            return false;
        }
        if (Math.abs(pay1.getPrice() - pay2.getPrice()) > 0.01) {
            return false;
        }
        if (Math.abs(pay1.getDiscount() - pay2.getDiscount()) > 0.01) {
            return false;
        }
        if (Math.abs(pay1.getDiscountPrice() - pay2.getDiscountPrice()) > 0.01) {
            return false;
        }
        List<String> msgs = pay1.getMsgs();
//        if (msgs == null || !msgs.equals(pay2.getMsgs())) {
//            return false;
//        }
        for(int i = 0; i < msgs.size();i++){
            if(!MessageFormat.format(pay2.getMsgs().get(i),EnvironmentContext.getEnvironmentContext().getCurrentCurrencySymbol()).equals(msgs.get(i)))
                return false;
        }
        return true;
    }
}
