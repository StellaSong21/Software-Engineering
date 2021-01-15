package fudan.se.lab4.service.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.service.RuleService;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.service.strategy.ProfitStrategy;
import fudan.se.lab4.service.strategy.TargetStrategy;
import fudan.se.lab4.service.strategy.impl.TargetStrategyImpl;

public class RuleServiceImpl implements RuleService {
    TargetStrategy targetStrategy = new TargetStrategyImpl();
    @Override
    public RuleResult discount(RuleContext ruleContext) {
//        int id=ruleContext.getUserId();
//        int type=new UserServiceImpl().getType(id);//客户资格类型
        Rule rule = ruleContext.getRule();//单条策略
        //profitType：0是满减，1是满赠，2是打折
        //TODO 先调用TargetStrategyImpl看是否满足优惠条件
        //TODO 用下面这个反射来调用对rule对象的处理！27行后switch后面的内容自己去掉
        try {
            int isValid = targetStrategy.isValid(ruleContext, rule);
            if (isValid != -1) {
                Class clazz = Class.forName("fudan.se.lab4.service.strategy.impl.ProfitStrategyImplType" + rule.getProfitType());
                ProfitStrategy profitStrategy = (ProfitStrategy) clazz.newInstance();
                return profitStrategy.profitProcess(ruleContext, rule, isValid);
            }else {
                return new RuleResult(rule,0,"");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //TODO 参考下面这个来实现ProfitStrategyImplType0/1/2的函数，实现完了后需要删除这里下面的这些函数
//    private RuleResult discountType0(RuleContext ruleContext, Rule rule) { //满减
//        double discount = 0.0;
//        String description = "";
//        Double priceBeforeCal = 0.0;
//        if (rule.getOriented() == null) { //  优惠对象是全体
//            priceBeforeCal = ruleContext.getPurePrice();
//        }
//
//
//        if (priceBeforeCal >= rule.getCondition()) {  //价格满足要求
//            if (rule.isCanAdd()) {
//                int times = (int) (priceBeforeCal / rule.getCondition());
//                discount = times * rule.getProfit();
//            } else {
//                discount = rule.getProfit();
//            }
//            description = "满" + rule.getCondition() + "减" + rule.getProfit();
//        }
//        return new RuleResult(rule, discount, description);
//    }
//
//    private RuleResult discountType1(RuleContext ruleContext, Rule rule) {   //满赠
//        String description = "";
//        double discount = 0.0;
//        Order order = ruleContext.getOrder();
//        DrinkUtil drinkUtil = new DrinkUtil();
//        Map<String, Integer> drinkNameAndNum = new HashMap<String, Integer>();
//        int num = 0;
//        for (OrderItem orderItem : order.getOrderItems()) {
//            if (isInOrientedList(orderItem, rule)) {
//                num++;
//                if (!drinkNameAndNum.containsKey(orderItem.getName())) {
//                    drinkNameAndNum.put(orderItem.getName(), 1);
//                } else {
//                    drinkNameAndNum.put(orderItem.getName(), drinkNameAndNum.get(orderItem.getName()) + 1);
//                }
//            }
//        }
//        if (rule.getFreeDrinks() == null) {
//            List<Map.Entry<String, Integer>> sortList = new ArrayList<Map.Entry<String, Integer>>(drinkNameAndNum.entrySet());
//            Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
//                @Override
//                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                    return (int) drinkUtil.getDrinks(o2.getKey()).getPrice() * 100 - (int) drinkUtil.getDrinks(o1.getKey()).getPrice() * 100;
//                }
//            });
//            int sub = rule.getCondition() + (int) rule.getProfit();
//            if (num > rule.getCondition()) {
//                if (rule.isCanAdd()) {
//                    for (Map.Entry<String, Integer> entry : sortList) {
//                        Drinks drink = drinkUtil.getDrinks(entry.getKey());
//                        if (num > 0 && (entry.getValue() * sub) >= num) {
//                            int tmp = num / sub;
//                            discount +=  (double)tmp * drink.getPrice() * rule.getProfit();
//                            num -= (num / sub) * sub;
//                        } else if (num > 0) {
//                            discount += entry.getValue() * drink.getPrice() * rule.getProfit();
//                            num -= entry.getValue() * sub;
//                        }
//                    }
//                } else {
//                    discount += drinkUtil.getDrinks(sortList.get(0).getKey()).getPrice() * rule.getProfit();
//                    num = 0;
//                }
//            }
//
//            for (Map.Entry<String, Integer> entry : sortList) {
//                description += entry.getKey() + " ";
//            }
//            description += ":买" + rule.getCondition() + "杯送" + (int) rule.getProfit() + "杯";
//        }
//
//
//        description = (discount == 0.0) ? "" : description;
//        return new RuleResult(rule, discount, description);
//    }

//    private RuleResult discountType2(RuleContext ruleContext, Rule rule) { //打折
//        String description = "";
//        Order order = ruleContext.getOrder();
//        DrinkUtil drinkUtil = new DrinkUtil();
//        Map<String, Integer> drinkNameAndNum = new HashMap<String, Integer>();
//        for (OrderItem orderItem : order.getOrderItems()) {
//            if (isInOrientedList(orderItem, rule)) {
//                if (!drinkNameAndNum.containsKey(orderItem.getName())) {
//                    drinkNameAndNum.put(orderItem.getName(), 1);
//                } else {
//                    drinkNameAndNum.put(orderItem.getName(), drinkNameAndNum.get(orderItem.getName()) + 1);
//                }
//            }
//        }
//        double discount = 0.0;
//        for (Map.Entry<String, Integer> entry : drinkNameAndNum.entrySet()) {
//            Drinks drink = drinkUtil.getDrinks(entry.getKey());
//            if (entry.getValue() >= rule.getCondition()) {
//                if (rule.isCanAdd()) {
//                    int times = (int) (entry.getValue() / rule.getCondition());
//                    discount += times * rule.getDiscountRange() * (1 - rule.getProfit()) * drink.getPrice();
//                } else {
//                    discount += rule.getDiscountRange() * (1 - rule.getProfit()) * drink.getPrice();
//                }
//                description += entry.getKey() + ": " + "每" + rule.getCondition() + "杯," + rule.getDiscountRange() + "杯" + (rule.getProfit() * 10) + "折";
//            }
//        }
//        return new RuleResult(rule, discount, description);
//    }
//
//    private boolean isInOrientedList(OrderItem orderItem, Rule rule) {
//        for (Drinks d : rule.getOriented()) {
//            if (d.getSize() == 0) {
//                if (d.getName().equals(orderItem.getName())) {
//                    return true;
//                }
//            } else {
//                if (d.getName().equals(orderItem.getName()) && d.getSize() == orderItem.getSize()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
