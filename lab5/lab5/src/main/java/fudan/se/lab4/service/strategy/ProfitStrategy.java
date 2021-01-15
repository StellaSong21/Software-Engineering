package fudan.se.lab4.service.strategy;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;

import java.util.List;

public interface ProfitStrategy {
    /**
     *
     * @param ruleContext
     * @param rule
     * @return RuleResult对象
     */
    RuleResult profitProcess(RuleContext ruleContext, Rule rule,int valid);
}
