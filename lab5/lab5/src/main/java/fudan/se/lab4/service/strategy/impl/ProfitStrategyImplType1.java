package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.dto.RuleResult;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;
import fudan.se.lab4.service.PriceService;
import fudan.se.lab4.service.impl.PriceServiceImpl;
import fudan.se.lab4.service.strategy.ProfitStrategy;

import java.util.*;

public class ProfitStrategyImplType1 implements ProfitStrategy {
    private PriceService priceService = new PriceServiceImpl();
    public RuleResult profitProcess(RuleContext ruleContext, Rule rule, int max) {
        //TODO 相当于原来的RuleServiceImpl.java里的private RuleResult discountType1(RuleContext ruleContext, Rule rule) 满赠
        String description = "";
        double discount = 0.0;
        Order order = ruleContext.getOrder();
        //  Map<Drinks, Integer> sendDrinks = new HashMap<>();
        DrinkRepository drinkUtil = new DrinkRepositoryImpl();
        //对全部订单生效，无条件赠送
//        if (rule.getDiscountRange() == null || rule.getDiscountRange().size() == 0) {
//            if (rule.getFreeDrinks() != null) {
//                //任选默认选最贵的，所以send里面只有一个饮品
//                for (RuleRepositoryImpl.Item send : rule.getFreeDrinks()) {
//                    sendDrinks.put(send.getDrinksList().get(0), (int) send.getNumber());
//                    discount += send.getDrinksList().get(0).getPrice();
//                }
//            } else {
//                return null;
//            }
//        }


        //不为null暂时不考虑了
        if (rule.getFreeDrinks() == null) {
            //默认买三送一

            //有条件的赠送
            List<RuleRepositoryImpl.Item> discountRange = rule.getDiscountRange();
            int[] num = new int[discountRange.size()];
            Map<String, Integer> drinkNum = new HashMap<>();
            //整理orderItem 里各个茶类型及其数量
            for (OrderItem orderItem : order.getOrderItems()) {
                String key = orderItem.getName() + "#" + orderItem.getSize();
                if (!drinkNum.containsKey(key)) {
                    drinkNum.put(key, 1);
                } else {
                    drinkNum.put(key, drinkNum.get(key) + 1);
                }
            }

//        int max = Integer.MAX_VALUE;
//
//        for (RuleRepositoryImpl.Item processObject : discountRange) {
//            int index = discountRange.indexOf(processObject);
//            if(processObject.getRequiretType()==1){
//                for (Drinks drinks : processObject.getDrinksList()) {
//                    if(drinkNum.containsKey(drinks.getName()+"#"+drinks.getSize())){
//                        num[index] += drinkNum.get(drinks.getName()+"#"+drinks.getSize());
//                    }else {
//                        num[index] += 0;
//                    }
//                }
//            }
//
//            int temp = (int) (num[index] / processObject.getNumber());
//            if (temp < max) {
//                //记录最多送多少次
//                max = temp;
//            }
//        }

            Map<String, Integer> require = new HashMap<>();

            for (RuleRepositoryImpl.Item processObject : discountRange) {
//                int index = discountRange.indexOf(processObject);
                if (processObject.getRequiretType() == 1) {
                    for (Drinks drinks : processObject.getDrinksList()) {
                        if (drinkNum.containsKey(drinks.getName() + "#" + drinks.getSize())) {
//                            num[index] += drinkNum.get(drinks.getName() + "#" + drinks.getSize());
                            require.put(drinks.getName() + "#" + drinks.getSize(), drinkNum.get(drinks.getName() + "#" + drinks.getSize()));

                        }
//                        else {
//                           num[index] += 0;
//                        }
                    }
                }


                List<Map.Entry<String, Integer>> sortList = new ArrayList<Map.Entry<String, Integer>>(require.entrySet());
                Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        if (rule.getIsOnlyBasicsDrinks() == 1) {
                            return (int) drinkUtil.getDrink(o2.getKey().split("#")[0]).getPrice() * 100 - (int) drinkUtil.getDrink(o1.getKey().split("#")[0]).getPrice() * 100;
                        }
                        return 1;
                    }
                });

                for (Map.Entry<String, Integer> mapping : sortList) {
                    //默认discountRange的size=1
                    max = (int) (max * rule.getProfit());
                    if (max <= 0) {
                        break;
                    }
                    if (mapping.getValue() - max > 0) {
                        discount += max * priceService.charge(drinkUtil.getDrink(mapping.getKey().split("#")[0]).getPrice(), EnvironmentContext.getEnvironmentContext().getCurrencyNow());
                        // sendDrinks.put(mapping.getKey(), max);
                        break;
                    } else {
                        discount += mapping.getValue() * priceService.charge(drinkUtil.getDrink(mapping.getKey().split("#")[0]).getPrice(),EnvironmentContext.getEnvironmentContext().getCurrencyNow());
                        ;
                        //    sendDrinks.put(mapping.getKey(), max);
                        max = max - mapping.getValue();
                    }
                }

            }
        }
        return new RuleResult(rule, discount, rule.getDescirption());
    }

}

