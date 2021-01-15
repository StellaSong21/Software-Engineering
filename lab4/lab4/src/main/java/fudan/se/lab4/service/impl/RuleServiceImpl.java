package fudan.se.lab4.service.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.dto.Rule;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.service.RuleService;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.util.DrinkUtil;

import java.util.*;

public class RuleServiceImpl implements RuleService {
    @Override
    public RuleResult discount(RuleContext ruleContext) {
//        int id=ruleContext.getUserId();
//        int type=new UserServiceImpl().getType(id);//客户资格类型
        Rule rule = ruleContext.getRule();//单条策略
        //profitType：0是满减，1是满赠，2是打折
        switch (rule.getProfitType()) {
            case 0:
                return discountType0(ruleContext, rule);
            case 1:
                return discountType1(ruleContext, rule);
            case 2:
                return discountType2(ruleContext, rule);
        }
        return null;
    }

    private RuleResult discountType0(RuleContext ruleContext, Rule rule) { //满减
        double discount = 0.0;
        String description = "";
        Double priceBeforeCal = 0.0;
        if (rule.getOriented() == null) { //  优惠对象是全体
            priceBeforeCal = ruleContext.getPurePrice();
        }
        //TODO 未来可以有不是针对所有商品的满减，本次lab不要求

        if (priceBeforeCal >= rule.getCondition()) {  //价格满足要求
            if (rule.isCanAdd()) {
                int times = (int) (priceBeforeCal / rule.getCondition());
                discount = times * rule.getProfit();
            } else {
                discount = rule.getProfit();
            }
            description = "满" + rule.getCondition() + "减" + rule.getProfit();
        }
        return new RuleResult(rule, discount, description);
    }

    private RuleResult discountType1(RuleContext ruleContext, Rule rule) {   //满赠
        String description = "";
        double discount = 0.0;
        Order order = ruleContext.getOrder();
        DrinkUtil drinkUtil = new DrinkUtil();
        Map<String, Integer> drinkNameAndNum = new HashMap<String, Integer>();
        int num = 0;
        for (OrderItem orderItem : order.getOrderItems()) {
            if (isInOrientedList(orderItem, rule)) {
                num++;
                if (!drinkNameAndNum.containsKey(orderItem.getName())) {
                    drinkNameAndNum.put(orderItem.getName(), 1);
                } else {
                    drinkNameAndNum.put(orderItem.getName(), drinkNameAndNum.get(orderItem.getName()) + 1);
                }
            }
        }
        if (rule.getFreeDrinks() == null) {
            List<Map.Entry<String, Integer>> sortList = new ArrayList<Map.Entry<String, Integer>>(drinkNameAndNum.entrySet());
            Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return (int) drinkUtil.getDrinks(o2.getKey()).getPrice() * 100 - (int) drinkUtil.getDrinks(o1.getKey()).getPrice() * 100;
                }
            });
            int sub = rule.getCondition() + (int) rule.getProfit();
            if (num > rule.getCondition()) {
                if (rule.isCanAdd()) {
                    for (Map.Entry<String, Integer> entry : sortList) {
                        Drinks drink = drinkUtil.getDrinks(entry.getKey());
                        if (num > 0 && (entry.getValue() * sub) >= num) {
                            discount += (num / sub) * drink.getPrice() * rule.getProfit();
                            num -= (num / sub) * sub;
                        } else if (num > 0) {
                            discount += entry.getValue() * drink.getPrice() * rule.getProfit();
                            num -= entry.getValue() * sub;
                        }
                    }
                } else {
                    discount += drinkUtil.getDrinks(sortList.get(0).getKey()).getPrice() * rule.getProfit();
                    num = 0;
                }
            }

            for (Map.Entry<String, Integer> entry : sortList) {
                description += entry.getKey() + " ";
            }
            description += ":买" + rule.getCondition() + "杯送" + (int) rule.getProfit() + "杯";
        }
        //TODO 之后可以拓展送的是别的饮料的情况

        description = (discount == 0.0) ? "" : description;
        return new RuleResult(rule, discount, description);
    }

    private RuleResult discountType2(RuleContext ruleContext, Rule rule) { //打折
        String description = "";
        Order order = ruleContext.getOrder();
        DrinkUtil drinkUtil = new DrinkUtil();
        Map<String, Integer> drinkNameAndNum = new HashMap<String, Integer>();
        for (OrderItem orderItem : order.getOrderItems()) {
            if (isInOrientedList(orderItem, rule)) {
                if (!drinkNameAndNum.containsKey(orderItem.getName())) {
                    drinkNameAndNum.put(orderItem.getName(), 1);
                } else {
                    drinkNameAndNum.put(orderItem.getName(), drinkNameAndNum.get(orderItem.getName()) + 1);
                }
            }
        }
        double discount = 0.0;
        for (Map.Entry<String, Integer> entry : drinkNameAndNum.entrySet()) {
            Drinks drink = drinkUtil.getDrinks(entry.getKey());
            if (entry.getValue() >= rule.getCondition()) {
                if (rule.isCanAdd()) {
                    int times = (int) (entry.getValue() / rule.getCondition());
                    discount += times * rule.getDiscountRange() * (1 - rule.getProfit()) * drink.getPrice();
                } else {
                    discount += rule.getDiscountRange() * (1 - rule.getProfit()) * drink.getPrice();
                }
                description += entry.getKey() + ": " + "每" + rule.getCondition() + "杯," + rule.getDiscountRange() + "杯" + (rule.getProfit() * 10) + "折";
            }
        }
        return new RuleResult(rule, discount, description);
    }

    private boolean isInOrientedList(OrderItem orderItem, Rule rule) {
        for (Drinks d : rule.getOriented()) {
            if (d.getSize() == 0) {
                if (d.getName().equals(orderItem.getName())) {
                    return true;
                }
            } else {
                if (d.getName().equals(orderItem.getName()) && d.getSize() == orderItem.getSize()) {
                    return true;
                }
            }
        }
        return false;
    }
}
