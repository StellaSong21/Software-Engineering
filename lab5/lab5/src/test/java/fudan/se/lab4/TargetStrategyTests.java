package fudan.se.lab4;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;
import fudan.se.lab4.service.strategy.impl.TargetStrategyImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TargetStrategyTests {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z", Locale.ENGLISH);
    private DrinkRepository drinkRepository;
    private RuleRepositoryImpl ruleRepository;
    private TargetStrategyImpl targetStrategy;
    private Map<String, Rule> rules;
    private Map<String, RuleContext> ruleContexts;

    @org.junit.Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void initial() {
        drinkRepository = new DrinkRepositoryImpl();
        ruleRepository = new RuleRepositoryImpl();
        targetStrategy = new TargetStrategyImpl();
        rules = new HashMap<>();
        ruleContexts = new HashMap<>();
    }

    @Before
    public void initRule() {
        //1.没有优惠条件
        Rule ruleNull = new Rule(0, 0, 0, 0, true, null, null, 0, null, null, null, null);
        rules.put("null", ruleNull);

        //2.1优惠条件为当前时间
        Date current = new Date();
        Date from = getDate(current, 0, 0, 0);
        Date to = getDate(current, 23, 59, 59);
        Rule ruleCorrectTime = new Rule(0, 0, 0, 0, true, from, to, 0, null, null, null, null);
        rules.put("correctTime", ruleCorrectTime);

        //2.2优惠条件不是当前时间
        Date fromWrong = getDate(plusTwo(current), 0, 0, 0);
        Date toWrong = getDate(plusTwo(current), 23, 59, 59);
        Rule ruleWrongTime = new Rule(0, 0, 0, 0, true, fromWrong, toWrong, 0, null, null, null, null);
        rules.put("wrongTime", ruleWrongTime);

        //3.优惠条件为订单的总价
        List<RuleRepositoryImpl.Item> orderConditionTotalPrice = new ArrayList<>();
        RuleRepositoryImpl.Item requireTotalPrice = ruleRepository.new Item(0, 100, null);
        orderConditionTotalPrice.add(requireTotalPrice);
        Rule ruleTotalPrice = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionTotalPrice, null, null, null);
        rules.put("totalPrice", ruleTotalPrice);

        //4.优惠条件为某类商品的总价
        List<RuleRepositoryImpl.Item> orderConditionPartialPrice = new ArrayList<>();
        List<Drinks> drinksPartialPrice = new ArrayList<>();
        drinksPartialPrice.add(drinkRepository.getDrink("redTea"));
        RuleRepositoryImpl.Item requirePartialPrice = ruleRepository.new Item(0, 100, drinksPartialPrice);
        orderConditionPartialPrice.add(requirePartialPrice);
        Rule rulePartialPrice = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionPartialPrice, null, null, null);
        rules.put("partialPrice", rulePartialPrice);

        //5.优惠条件为饮品的数量，无关饮品种类
        List<RuleRepositoryImpl.Item> orderConditionDrinksNull = new ArrayList<>();
        RuleRepositoryImpl.Item requireDrinksNull = ruleRepository.new Item(1, 3, null);
        orderConditionDrinksNull.add(requireDrinksNull);
        Rule ruleDrinksNull = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionDrinksNull, null, null, null);
        rules.put("drinksNull0", ruleDrinksNull);

        List<RuleRepositoryImpl.Item> orderConditionDrinksNull1 = new ArrayList<>();
        RuleRepositoryImpl.Item requireDrinksNull1 = ruleRepository.new Item(1, 3, null);
        orderConditionDrinksNull1.add(requireDrinksNull1);
        Rule ruleDrinksNull1 = new Rule(0, 0, 1, 0, true, null, null, 0, orderConditionDrinksNull1, null, null, null);
        rules.put("drinksNull1", ruleDrinksNull1);

        //6.优惠条件为单一饮品的数量
        List<RuleRepositoryImpl.Item> orderConditionDrinksOne = new ArrayList<>();
        List<Drinks> drinksOne = new ArrayList<>();
        Drinks redTea = drinkRepository.getDrink("redTea");
        redTea.setSize(3);
        drinksOne.add(redTea);
        RuleRepositoryImpl.Item requireDrinksOne = ruleRepository.new Item(1, 3, drinksOne);
        orderConditionDrinksOne.add(requireDrinksOne);
        Rule ruleDrinksOne = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionDrinksOne, null, null, null);
        rules.put("drinksOne0", ruleDrinksOne);

//        List<RuleRepositoryImpl.Item> orderConditionDrinksOne1 = new ArrayList<>();
//        List<Drinks> drinksOne1 = new ArrayList<>();
//        Drinks redTea1 = drinkRepository.getDrink("redTea");
//        redTea1.setSize(3);
//        drinksOne1.add(redTea1);
//        RuleRepositoryImpl.Item requireDrinksOne1 = ruleRepository.new Item(1, 3, drinksOne1);
//        orderConditionDrinksOne1.add(requireDrinksOne1);
//        Rule ruleDrinksOne1 = new Rule(0, 0, 1, 0, true, null, null, 0, orderConditionDrinksOne1, null, null, null);
//        rules.put("drinksOne1", ruleDrinksOne1);

        //7.优惠条件为多种饮品的数量
        List<RuleRepositoryImpl.Item> orderConditionDrinksMore = new ArrayList<>();
        List<Drinks> drinksMore = new ArrayList<>();
        drinksMore.add(drinkRepository.getDrink("redTea"));
        drinksMore.add(drinkRepository.getDrink("greenTea"));
        drinksMore.add(drinkRepository.getDrink("cappuccino"));
        RuleRepositoryImpl.Item requireDrinksMore = ruleRepository.new Item(1, 3, drinksMore);
        orderConditionDrinksMore.add(requireDrinksMore);
        Rule ruleDrinksMore = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionDrinksMore, null, null, null);
        rules.put("drinksMore0", ruleDrinksMore);

//        List<RuleRepositoryImpl.Item> orderConditionDrinksMore1 = new ArrayList<>();
//        List<Drinks> drinksMore1 = new ArrayList<>();
//        drinksMore1.add(drinkRepository.getDrink("redTea"));
//        drinksMore1.add(drinkRepository.getDrink("greenTea"));
//        drinksMore1.add(drinkRepository.getDrink("cappuccino"));
//        RuleRepositoryImpl.Item requireDrinksMore1 = ruleRepository.new Item(1, 3, drinksMore1);
//        orderConditionDrinksMore1.add(requireDrinksMore1);
//        Rule ruleDrinksMore1 = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionDrinksMore1, null, null, null);
//        rules.put("drinksMore1", ruleDrinksMore1);

        //8.优惠条件为不可累加的
        List<RuleRepositoryImpl.Item> orderConditionCanAddPrice = new ArrayList<>();
        RuleRepositoryImpl.Item requireCanAddPrice = ruleRepository.new Item(0, 100, null);
        orderConditionCanAddPrice.add(requireCanAddPrice);
        Rule ruleCanAddPrice = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionCanAddPrice, null, null, null);
        rules.put("canAddPrice", ruleCanAddPrice);

        List<RuleRepositoryImpl.Item> orderConditionNotAddPrice = new ArrayList<>();
        RuleRepositoryImpl.Item requireNotAddPrice = ruleRepository.new Item(0, 100, null);
        orderConditionNotAddPrice.add(requireNotAddPrice);
        Rule ruleNotAddPrice = new Rule(0, 0, 0, 0, false, null, null, 0, orderConditionNotAddPrice, null, null, null);
        rules.put("notAddPrice", ruleNotAddPrice);

        List<RuleRepositoryImpl.Item> orderConditionCanAddDrinks = new ArrayList<>();
        RuleRepositoryImpl.Item requireCanAddDrinks = ruleRepository.new Item(1, 3, null);
        orderConditionCanAddDrinks.add(requireCanAddDrinks);
        Rule ruleCanAddDrinks = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionCanAddDrinks, null, null, null);
        rules.put("canAddDrinks", ruleCanAddDrinks);

        List<RuleRepositoryImpl.Item> orderConditionNotAddDrinks = new ArrayList<>();
        RuleRepositoryImpl.Item requireNotAddDrinks = ruleRepository.new Item(1, 3, null);
        orderConditionNotAddDrinks.add(requireNotAddDrinks);
        Rule ruleNotAddDrinks = new Rule(0, 0, 0, 0, false, null, null, 0, orderConditionNotAddDrinks, null, null, null);
        rules.put("notAddDrinks", ruleNotAddDrinks);

        //9.优惠条件中有多个列表，有多个require
        List<RuleRepositoryImpl.Item> orderConditionRequireMore = new ArrayList<>();
        RuleRepositoryImpl.Item requireMore1 = ruleRepository.new Item(1, 3, null);
        List<Drinks> drinksRequireMore2 = new ArrayList<>();
        drinksRequireMore2.add(drinkRepository.getDrink("redTea"));
        RuleRepositoryImpl.Item requireMore2 = ruleRepository.new Item(1, 1, drinksRequireMore2);
        orderConditionRequireMore.add(requireMore1);
        orderConditionRequireMore.add(requireMore2);
        Rule ruleRequireMore = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionRequireMore, null, null, null);
        rules.put("requireMore", ruleRequireMore);

        //10.优惠条件中既有时间，也有对饮品的限制
        List<RuleRepositoryImpl.Item> orderConditionComposite1 = new ArrayList<>();
        RuleRepositoryImpl.Item requireComposite1 = ruleRepository.new Item(1, 3, null);
        orderConditionComposite1.add(requireComposite1);
        Rule ruleComposite1 = new Rule(0, 0, 0, 0, true, from, to, 0, orderConditionComposite1, null, null, null);
        rules.put("composition1", ruleComposite1);

        List<RuleRepositoryImpl.Item> orderConditionComposite2 = new ArrayList<>();
        RuleRepositoryImpl.Item requireComposite2 = ruleRepository.new Item(1, 3, null);
        orderConditionComposite2.add(requireComposite2);
        Rule ruleComposite2 = new Rule(0, 0, 0, 0, true, fromWrong, toWrong, 0, orderConditionComposite2, null, null, null);
        rules.put("composition2", ruleComposite2);

        //11.优惠条件中requireType不合法
        List<RuleRepositoryImpl.Item> orderConditionInvalidRequireType = new ArrayList<>();
        RuleRepositoryImpl.Item requireInvalidType = ruleRepository.new Item(Integer.MAX_VALUE, 100, null);
        orderConditionInvalidRequireType.add(requireInvalidType);
        Rule ruleInvalidRequireType = new Rule(0, 0, 0, 0, true, null, null, 0, orderConditionInvalidRequireType, null, null, null);
        rules.put("invalidRequireType", ruleInvalidRequireType);
    }

    @Before
    public void initRuleContext() {
        //1.任意一个订单
        //2.时间满足条件
        //3.总价满足条件
        List<OrderItem> orderItems1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems1.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        ruleContexts.put("totalPriceEquals", ruleContext1);

        List<OrderItem> orderItems2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems2.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext2 = new RuleContext(new Order("1", orderItems2), null, 10);
        ruleContexts.put("totalPriceLess", ruleContext2);

        //4.某类商品的总价满足条件
        List<OrderItem> orderItems3 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems3.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext3 = new RuleContext(new Order("1", orderItems3), null, 10);
        ruleContexts.put("partialPrice", ruleContext3);

        //5.饮品的数量满足条件，3
        List<OrderItem> orderItems4 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            orderItems4.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext4 = new RuleContext(new Order("1", orderItems4), null, 100);
        ruleContexts.put("drinksNull4", ruleContext4);

        List<OrderItem> orderItems5 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems5.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext5 = new RuleContext(new Order("1", orderItems5), null, 100);
        ruleContexts.put("drinksNull3", ruleContext5);

        List<OrderItem> orderItems17 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            orderItems17.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext17 = new RuleContext(new Order("1", orderItems17), null, 100);
        ruleContexts.put("drinksNull2", ruleContext17);

        //6.某种饮品的数量满足条件，红茶，3
        List<OrderItem> orderItems6 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems6.add(new OrderItem("greenTea", new ArrayList<>(), 3));
        }
        RuleContext ruleContext6 = new RuleContext(new Order("1", orderItems6), null, 100);
        ruleContexts.put("drinksOneLess1", ruleContext6);

        List<OrderItem> orderItems7 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems7.add(new OrderItem("redTea", new ArrayList<>(), 3));
        }
        RuleContext ruleContext7 = new RuleContext(new Order("1", orderItems7), null, 100);
        ruleContexts.put("drinksOneEquals", ruleContext7);

        List<OrderItem> orderItems8 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems8.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext8 = new RuleContext(new Order("1", orderItems8), null, 100);
        ruleContexts.put("drinksOneLess2", ruleContext8);

        //7.多种饮品的数量满足条件，红茶，绿茶，卡布奇诺，3
        List<OrderItem> orderItems9 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            orderItems9.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        orderItems9.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        RuleContext ruleContext9 = new RuleContext(new Order("1", orderItems9), null, 100);
        ruleContexts.put("drinksMoreEquals", ruleContext9);

        List<OrderItem> orderItems10 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems10.add(new OrderItem("espresso", new ArrayList<>(), 1));
        }
        RuleContext ruleContext10 = new RuleContext(new Order("1", orderItems10), null, 100);
        ruleContexts.put("drinksMoreLess", ruleContext10);

        //8.优惠条件不可累加，饮品数量，3
        List<OrderItem> orderItems11 = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            orderItems11.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext11 = new RuleContext(new Order("1", orderItems11), null, 10);
        ruleContexts.put("notAddDrinks", ruleContext11);

        List<OrderItem> orderItems12 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            orderItems12.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext12 = new RuleContext(new Order("1", orderItems12), null, 200);
        ruleContexts.put("notAddPrice", ruleContext12);

        //9.多个require，饮品数量，3，红茶，1
        List<OrderItem> orderItems13 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems13.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext13 = new RuleContext(new Order("1", orderItems13), null, 100);
        ruleContexts.put("requireMoreOne", ruleContext13);

        List<OrderItem> orderItems14 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            orderItems14.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        for (int i = 0; i < 2; i++) {
            orderItems14.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext14 = new RuleContext(new Order("1", orderItems14), null, 100);
        ruleContexts.put("requireMoreBoth", ruleContext14);

        //10.时间限制，饮品数量，3
        List<OrderItem> orderItems15 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            orderItems15.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext15 = new RuleContext(new Order("1", orderItems15), null, 100);
        ruleContexts.put("compositionDrinksLess", ruleContext15);

        List<OrderItem> orderItems16 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems16.add(new OrderItem("greenTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext16 = new RuleContext(new Order("1", orderItems16), null, 100);
        ruleContexts.put("compositionDrinksEquals", ruleContext16);
    }

    @After
    public void clear() {
        drinkRepository = null;
        ruleRepository = null;
        targetStrategy = null;
        rules.clear();
        rules = null;
        ruleContexts.clear();
        ruleContexts = null;
    }

    @Test
    public void testNull() {
        assertTrue(isValid("totalPriceEquals", "null", 1));
    }

    @Test
    public void testTime() {
        assertTrue(isValid("totalPriceEquals", "correctTime", 1));
        assertTrue(isValid("totalPriceEquals", "wrongTime", -1));
    }

    @Test
    public void testTotalPrice() {
        assertTrue(isValid("totalPriceEquals", "totalPrice", 1));
        assertTrue(isValid("totalPriceLess", "totalPrice", -1));
    }

    @Test
    public void testPartialPrice() {
        assertTrue(isValid("partialPrice", "partialPrice", -1));
    }

    @Test
    public void testDrinksNull0() {
        assertTrue(isValid("drinksNull3", "drinksNull0", 1));
        assertTrue(isValid("drinksNull2", "drinksNull0", -1));
    }

    @Test
    public void testDrinksNull1() {
        assertTrue(isValid("drinksNull4", "drinksNull1", 1));
        assertTrue(isValid("drinksNull3", "drinksNull1", -1));
    }

    @Test
    public void testDrinksOne() {
        assertTrue(isValid("drinksOneEquals", "drinksOne0", 1));
        assertTrue(isValid("drinksOneLess1", "drinksOne0", -1));
        assertTrue(isValid("drinksOneLess2", "drinksOne0", -1));
    }

    @Test
    public void testDrinksMore() {
        assertTrue(isValid("drinksMoreEquals", "drinksMore0", 1));
        assertTrue(isValid("drinksMoreLess", "drinksMore0", -1));
    }

    @Test
    public void testNotAdd() {
        assertTrue(isValid("notAddPrice", "canAddPrice", 2));
        assertTrue(isValid("notAddPrice", "notAddPrice", 1));
        assertTrue(isValid("notAddDrinks", "canAddDrinks", 3));
        assertTrue(isValid("notAddDrinks", "notAddDrinks", 1));
    }

    @Test
    public void testRequireMore() {
        assertTrue(isValid("requireMoreBoth", "requireMore", 1));
        assertTrue(isValid("requireMoreOne", "requireMore", -1));
    }

    @Test
    public void testComposition() {
        assertTrue(isValid("compositionDrinksEquals", "composition1", 1));
        assertTrue(isValid("compositionDrinksLess", "composition1", -1));
        assertTrue(isValid("compositionDrinksEquals", "composition2", -1));
    }

    @Test
    public void testInvalidRequireType() {
//        thrown.expectMessage(new LoggerServiceImpl().log("PROMOTION_TYPE_NOT_EXISTED"));
        assertTrue(isValid("totalPriceEquals", "invalidRequireType", -1));
    }

    private Date getDate(Date date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    private Date plusTwo(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        return calendar.getTime();
    }

    private boolean isValid(String ruleContext, String rule, int result) {
        return targetStrategy.isValid(ruleContexts.get(ruleContext), rules.get(rule)) == result;
    }
}

