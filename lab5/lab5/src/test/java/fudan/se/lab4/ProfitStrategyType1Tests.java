package fudan.se.lab4;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;
import fudan.se.lab4.service.strategy.ProfitStrategy;
import fudan.se.lab4.service.strategy.impl.ProfitStrategyImplType1;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfitStrategyType1Tests {
    private ProfitStrategy profitStrategy;
    private DrinkRepository drinkRepository;
    private Rule ruleOneKind;//只对一种商品有优惠
    private Rule ruleMoreKind;//对多种商品有优惠

    @Before
    public void initial() {
        profitStrategy = new ProfitStrategyImplType1();
        drinkRepository = new DrinkRepositoryImpl();

        List<RuleRepositoryImpl.Item> discountRange1 = new ArrayList<>();
        List<Drinks> drinks1 = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Drinks tea = drinkRepository.getDrink("greenTea");
            tea.setSize(i);
            drinks1.add(tea);
        }
        RuleRepositoryImpl.Item discount1 = new RuleRepositoryImpl().new Item(1, 3, drinks1);
        discountRange1.add(discount1);
        ruleOneKind = new Rule(0, 0, 1, 1, true, null, null, 1, null, discountRange1, null, "onekind");

        List<RuleRepositoryImpl.Item> discountRange2 = new ArrayList<>();
        List<Drinks> drinks2 = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Drinks teag = drinkRepository.getDrink("greenTea");
            teag.setSize(i);
            drinks2.add(teag);
            Drinks tear = drinkRepository.getDrink("redTea");
            tear.setSize(i);
            drinks2.add(tear);
        }
        RuleRepositoryImpl.Item discount2 = new RuleRepositoryImpl().new Item(1, 3, drinks2);
        discountRange2.add(discount2);
        ruleMoreKind = new Rule(0, 0, 1, 1, true, null, null, 1, null, discountRange2, null, "twokinds");
    }

    @Test
    public void testDrinksOneKind() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), 3));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, ruleOneKind, 1);
        assertTrue(equals(result, new RuleResult(ruleOneKind, 16, "onekind")));

        result = profitStrategy.profitProcess(ruleContext1, ruleMoreKind, 1);
        assertTrue(equals(result, new RuleResult(ruleMoreKind, 16, "twokinds")));
    }

    @Test
    public void testDrinksMoreKinds() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), 3));
        }
        orderItems1.add(new OrderItem("redTea", new ArrayList<>(), 2));
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        RuleResult result = profitStrategy.profitProcess(ruleContext1, ruleMoreKind, 1);
        assertTrue(equals(result, new RuleResult(ruleMoreKind, 18, "twokinds")));


        for (int i = 0; i < 2; i++) {
            orderItems1.add(new OrderItem("greenTea", new ArrayList<>(), 3));
        }
        orderItems1.add(new OrderItem("redTea", new ArrayList<>(), 2));
        ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);
        result = profitStrategy.profitProcess(ruleContext1, ruleMoreKind, 2);
        assertTrue(equals(result, new RuleResult(ruleMoreKind, 36, "twokinds")));
    }

    private boolean equals(RuleResult result1, RuleResult result2) {
        Rule rule1 = result1.getRule();
        Rule rule2 = result2.getRule();
        return rule1 != null && rule1.equals(rule2) && result1.getDiscount() == result2.getDiscount() && result1.getRuleDescription().equals(result2.getRuleDescription());
    }
}
