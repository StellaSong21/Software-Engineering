package fudan.se.lab2;

import fudan.se.lab2.constant.InfoConstant;
import fudan.se.lab2.entity.Cappuccino;
import fudan.se.lab2.entity.Coffee;
import fudan.se.lab2.entity.Espresso;
import fudan.se.lab2.repository.CappuccinoRepository;
import fudan.se.lab2.repository.EspressoRepository;
import fudan.se.lab2.repository.impl.CappuccinoRepositoryImpl;
import fudan.se.lab2.repository.impl.EspressoRepositoryImpl;
import fudan.se.lab2.service.impl.PriceServiceImpl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PriceServiceTests {
    private PriceServiceImpl priceService;

    @Before
    public void setUp() {
        priceService = new PriceServiceImpl();
    }

    @After
    public void tearDown() {
        priceService = null;
    }


    private Cappuccino jmockCappuccino(int size) {
        Mockery context = new JUnit4Mockery();
        CappuccinoRepository test = context.mock(CappuccinoRepository.class);
        context.checking(new Expectations() {
            {
                Cappuccino cappuccino = new Cappuccino();
                cappuccino.setName("cappuccino");
                cappuccino.setDescription("cappuccino");
                cappuccino.setPrice(20);
                cappuccino.setSize(size);
                allowing(test).getCappuccino("cappuccino");
                will(returnValue(cappuccino));
            }
        });
        return test.getCappuccino("cappuccino");
    }

    private Espresso jmockEspresso(int size) {
        Mockery context = new JUnit4Mockery();
        EspressoRepository test = context.mock(EspressoRepository.class);
        context.checking(new Expectations() {
            {
                Espresso espresso = new Espresso();
                espresso.setName("espresso");
                espresso.setDescription("espresso");
                espresso.setPrice(22);
                espresso.setSize(size);
                allowing(test).getEspresso("espresso");
                will(returnValue(espresso));
            }
        });
        return test.getEspresso("espresso");
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //正常情况 测试两种咖啡的每一种size 检查价格
    @Test
    public void testCostRight() {
        Map<Coffee, Integer> order = new LinkedHashMap<>();

        order.put(jmockEspresso(1), 1);
        order.put(jmockEspresso(2), 1);
        order.put(jmockEspresso(3), 1);

        order.put(jmockCappuccino(1), 1);
        order.put(jmockCappuccino(2), 1);
        order.put(jmockCappuccino(3), 1);

        assertEquals(150.0, priceService.cost(order), 0.0);

    }

    @Test
    //size和价格的关系
    public void testCoffeeCost() {
        Coffee coffee = jmockCappuccino(1);
        assertEquals(22.0, coffee.cost(), 0.0);
        Coffee coffee1 = jmockCappuccino(2);
        assertEquals(24.0, coffee1.cost(), 0.0);
        Coffee coffee2 = jmockCappuccino(3);
        assertEquals(26.0, coffee2.cost(), 0.0);

        Coffee coffee00 = jmockEspresso(1);
        assertEquals(24.0, coffee00.cost(), 0.0);
        Coffee coffee11 = jmockEspresso(2);
        assertEquals(26.0, coffee11.cost(), 0.0);
        Coffee coffee22 = jmockEspresso(3);
        assertEquals(28.0, coffee22.cost(), 0.0);

    }


    //size小
    @Test
    public void testCostSizeSmall() {
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The size of coffee is invalid.");
        order.put(jmockCappuccino(-1), 1);
        priceService.cost(order);
    }

    //size大
    @Test
    public void testCostSizeBig() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The size of coffee is invalid.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        order.put(jmockCappuccino(5), 1);
        priceService.cost(order);
    }

    //杯数为负数或0
    @Test
    public void testCostIntegerNegative() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The number of coffee is negative or zero.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        order.put(jmockCappuccino(1), -1);
        priceService.cost(order);
    }

    @Test
    public void testCostIntegerZero() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The number of coffee is negative or zero.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        order.put(jmockCappuccino(1), 0);
        priceService.cost(order);

    }

    //订单为空
    @Test
    public void testCostMapNULL() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The map of order is null or the order is empty.");
        priceService.cost(null);

    }

    //订单长度为空
    @Test
    public void testCostEntryNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The map of order is null or the order is empty.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        priceService.cost(order);
    }


    //咖啡为空
    @Test
    public void testCostCoffeeNULL() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The order has something wrong.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        order.put(null, 1);
        priceService.cost(order);
    }

    //咖啡假如被修改，导致名字为空
    @Test
    public void testCostCoffeeNameNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Coffee name is wrong.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        Coffee coffee = jmockEspresso(1);
        coffee.setName(null);
        order.put(coffee, 1);
        priceService.cost(order);
    }

    //咖啡假如被修改，名字不对
    @Test
    public void testCostCoffeeNameWrong() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Coffee name is wrong.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        Coffee coffee = jmockEspresso(1);
        coffee.setName("111");
        order.put(coffee, 1);
        priceService.cost(order);
    }

    //咖啡假如被修改，描述为空
    @Test
    public void testCostCoffeeDescriptionNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Coffee description is wrong.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        Coffee coffee = jmockCappuccino(1);
        coffee.setDescription(null);
        order.put(coffee, 1);
        priceService.cost(order);
    }

    //咖啡假如被修改，描述不对
    @Test
    public void testCostCoffeeDescriptionWrong() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Coffee description is wrong.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        Coffee coffee = jmockCappuccino(1);
        coffee.setDescription("ss");
        order.put(coffee, 1);
        priceService.cost(order);
    }

    //咖啡假如被修改，价格小于最低价格22=20+2
    @Test
    public void testCostCoffeePriceLow() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The order has something wrong.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        Coffee coffee = jmockCappuccino(1);
        coffee.setPrice(19);
        order.put(coffee, 1);
        priceService.cost(order);
    }

    //咖啡价格适中
    @Test
    public void testCostCoffeePriceOK() {
        try {
            Map<Coffee, Integer> order = new LinkedHashMap<>();
            Coffee coffee = jmockCappuccino(1);
            coffee.setPrice(22);
            order.put(coffee, 1);
            priceService.cost(order);
        } catch (RuntimeException e) {
            fail("No exception should be thrown");
        }

    }

    //咖啡假如被修改，价格高于最高价格28=22+6
    @Test
    public void testCostCoffeePriceHigh() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The order has something wrong.");
        Map<Coffee, Integer> order = new LinkedHashMap<>();
        Coffee coffee = jmockCappuccino(1);
        coffee.setPrice(27);
        order.put(coffee, 1);
        priceService.cost(order);
    }

    //咖啡顺便测试了一下jmock模拟的getCappuccino()
    @Test
    public void testGetCappuccino() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, "111"));
        new CappuccinoRepositoryImpl().getCappuccino("111");
    }

    @Test
    public void testGetEspresso() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, "111"));
        new EspressoRepositoryImpl().getEspresso("111");
    }



    //检查价格错误
    @Test
    public void testCostWrong() {
        Map<Coffee, Integer> order = new LinkedHashMap<>();

        order.put(jmockEspresso(1), 1);
        order.put(jmockEspresso(2), 3);
        order.put(jmockEspresso(3), 7);

        order.put(jmockCappuccino(1), 5);
        order.put(jmockCappuccino(2), 1);
        order.put(jmockCappuccino(3), 4);

        assertNotEquals(150.0, priceService.cost(order), 0.0);

    }





}
