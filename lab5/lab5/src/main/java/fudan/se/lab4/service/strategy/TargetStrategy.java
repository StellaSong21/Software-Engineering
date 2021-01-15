package fudan.se.lab4.service.strategy;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.entity.Rule;

public interface TargetStrategy {
    /**
     *
     * @param ruleContext
     * @param rule
     * @return 判断优惠条件是否生效
     */
    int isValid(RuleContext ruleContext, Rule rule);
}
