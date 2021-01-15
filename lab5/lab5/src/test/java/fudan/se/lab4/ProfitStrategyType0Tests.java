package fudan.se.lab4;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.*;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;
import fudan.se.lab4.service.strategy.ProfitStrategy;
import fudan.se.lab4.service.strategy.impl.ProfitStrategyImplType0;
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
public class ProfitStrategyType0Tests {
    private ProfitStrategy profitStrategy;
    private Rule ruleCanAdd;
    private Rule ruleNotAdd;

    /*
     * 对满减的测试
     * 1.
     *
     */
    @Before
    public void initial() {
        profitStrategy = new ProfitStrategyImplType0();
        List<RuleRepositoryImpl.Item> discountRange = new ArrayList<>();
        RuleRepositoryImpl.Item discount = new RuleRepositoryImpl().new Item(0, 100, null);
        discountRange.add(discount);

        ruleCanAdd = new Rule(0, 0, 0, 30, true, null, null, 0, null, discountRange, null, "100-30 canAdd");
        ruleNotAdd = new Rule(0, 0, 0, 30, false, null, null, 0, null, discountRange, null, "100-30 notAdd");
    }

    @Test
    public void testPriceEquals() {
        List<OrderItem> orderItems1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems1.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext1 = new RuleContext(new Order("1", orderItems1), null, 100);

        RuleResult resultCanAdd = profitStrategy.profitProcess(ruleContext1, ruleCanAdd, 1);
        assertTrue(equals(resultCanAdd, new RuleResult(ruleCanAdd, 30, "100-30 canAdd")));
    }

    @Test
    public void testPriceTwice() {
        List<OrderItem> orderItems3 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orderItems3.add(new OrderItem("redTea", new ArrayList<>(), 1));
        }
        RuleContext ruleContext3 = new RuleContext(new Order("1", orderItems3), null, 250);

        RuleResult resultCanAdd = profitStrategy.profitProcess(ruleContext3, ruleCanAdd, 2);
        assertTrue(equals(resultCanAdd, new RuleResult(ruleCanAdd, 60, "100-30 canAdd")));

        RuleResult resultNotAdd = profitStrategy.profitProcess(ruleContext3, ruleNotAdd, 1);
        assertTrue(equals(resultNotAdd, new RuleResult(ruleNotAdd, 30, "100-30 notAdd")));
    }

    private boolean equals(RuleResult result1, RuleResult result2) {
        Rule rule1 = result1.getRule();
        Rule rule2 = result2.getRule();
        return rule1 != null && rule1.equals(rule2) && result1.getDiscount() == result2.getDiscount() && result1.getRuleDescription().equals(result2.getRuleDescription());
    }
}
