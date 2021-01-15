package fudan.se.lab4.service.impl;

import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.PromotionResult;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.entity.*;
import fudan.se.lab4.repository.RuleRepository;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;
import fudan.se.lab4.service.PromotionService;

import java.util.*;

public class PromotionServiceImpl implements PromotionService {
    List<Rule> rules = EnvironmentContext.getEnvironmentContext().getRules();

    @Override
    public PromotionResult chooseRules(Order order, double purePrice) {
        class DiscountAndPromotion{
            double totalDiscount;
            List<String> totalDes = new ArrayList<>();
            DiscountAndPromotion(Double d){
                totalDiscount = d;
            }

        }

        Map<Integer,DiscountAndPromotion> eachPromotionAndDiscount = new HashMap<>();
        RuleResult ruleResult;
        RuleServiceImpl ruleService = new RuleServiceImpl();
        for(Rule rule : rules){
            RuleContext ruleContext = new RuleContext(order,rule,purePrice);
            ruleResult = ruleService.discount(ruleContext);
            if(!eachPromotionAndDiscount.containsKey(rule.getGroupId())){
                DiscountAndPromotion discountAndPromotion = new DiscountAndPromotion(ruleResult.getDiscount());
                if(ruleResult.getDiscount() != 0.0)
                    discountAndPromotion.totalDes .add(ruleResult.getRuleDescription());
                eachPromotionAndDiscount.put(rule.getGroupId(),discountAndPromotion);
            }else {
                double plus = ruleResult.getDiscount();
                eachPromotionAndDiscount.get(rule.getGroupId()).totalDiscount += plus;
                if(ruleResult.getDiscount() != 0.0)
                    eachPromotionAndDiscount.get(rule.getGroupId()).totalDes.add(ruleResult.getRuleDescription());
            }
        }

        List<Map.Entry<Integer,DiscountAndPromotion>> sortList = new ArrayList<Map.Entry<Integer, DiscountAndPromotion>>(eachPromotionAndDiscount.entrySet());
        Collections.sort(sortList, new Comparator<Map.Entry<Integer, DiscountAndPromotion>>() {
            @Override
            public int compare(Map.Entry<Integer, DiscountAndPromotion> o1, Map.Entry<Integer, DiscountAndPromotion> o2) {
                return (int)(o2.getValue().totalDiscount*100) - (int)(o1.getValue().totalDiscount*100);
            }
        });

        return new PromotionResult(sortList.get(0).getValue().totalDes,sortList.get(0).getValue().totalDiscount);
    }






}
