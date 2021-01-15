package fudan.se.lab4.service.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.PromotionResult;
import fudan.se.lab4.dto.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.entity.*;
import fudan.se.lab4.service.PromotionService;

import java.util.*;

public class PromotionServiceImpl implements PromotionService {
    ArrayList<Rule> rules = initSaleRule();

    //本来应该从数据库的促销规则表中读取数据，因为本次lab不涉及数据库表设计，故在此写死，若要添加或修改促销规则，在此处修改一次即可

    public ArrayList<Rule> initSaleRule(){
        ArrayList<Rule> rules = new ArrayList<>();
        //profitType：0是满减，1是满赠，2是打折
        Drinks orient1 = new Espresso();
        orient1.setName("espresso");
        orient1.setSizeInSaleRule(3);
        Drinks orient2 = new RedTea();
        orient2.setName("redTea");
        orient2.setSizeInSaleRule(0);
        Drinks orient3 = new GreenTea();
        orient3.setName("greenTea");
        orient3.setSizeInSaleRule(0);
        Drinks orient4 = new Cappuccino();
        orient4.setName("cappuccino");
        orient4.setSizeInSaleRule(0);

        Drinks send1 = new RedTea();
        send1.setName("redTea");
        Drinks send2 = new GreenTea();
        send2.setName("greenTea");

        rules.add(new Rule(1,0,2,2,2,0.8,true,new ArrayList<Drinks>(){{add(orient1);}},null));
        rules.add(new Rule(1,0,1,3,3,1,true,new ArrayList<Drinks>(){{add(orient2);add(orient3);}},null));
        rules.add(new Rule(1,0,2,2,1,0.5,true,new ArrayList<Drinks>(){{add(orient4);}},null));
        rules.add(new Rule(2,0,0,100,100,30,true,null,null));

        return rules;
    }


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
