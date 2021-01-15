package fudan.se.lab4.dto;

import fudan.se.lab4.entity.Rule;

public class RuleResult {
    private Rule rule;
    private String ruleDescription;
    private double discount;
    public RuleResult(Rule rule,double discount, String ruleDescription){
        this.rule = rule;
        this.discount = discount;
        this.ruleDescription = ruleDescription;
    }

    public Rule getRule() {
        return rule;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public double getDiscount() {
        return discount;
    }
}
