package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;
import fudan.se.lab4.service.strategy.ProfitStrategy;

public class ProfitStrategyImplType0 implements ProfitStrategy {
    public RuleResult profitProcess(RuleContext ruleContext, Rule rule,int max) {
        //TODO 相当于原来的RuleServiceImpl.java里的private RuleResult discountType0(RuleContext ruleContext, Rule rule) 满减

        double discount = 0.0;
        if(rule.getDiscountRange().size() == 1 && rule.getDiscountRange().get(0).getDrinksList() == null){
            if (rule.getIsOnlyBasicsDrinks()==0){
//                double priceBeforeCal = ruleContext.getPurePrice();
//                double condition = rule.getDiscountRange().get(0).getNumber();
//                if(rule.isCanAdd()){
//                    int times = (int) (priceBeforeCal / condition);
//                    discount += times * rule.getProfit();
//                }else {
//                    discount += rule.getProfit();
//                }
                discount += max*rule.getProfit();
            }
        }else {
            //TODO
        }

        return new RuleResult(rule, discount, rule.getDescirption());
    }
}
