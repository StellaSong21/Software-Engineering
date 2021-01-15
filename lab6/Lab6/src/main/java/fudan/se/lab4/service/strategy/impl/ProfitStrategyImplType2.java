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
//import fudan.se.lab4.service.PriceService;
//import fudan.se.lab4.service.impl.PriceServiceImpl;
import fudan.se.lab4.service.strategy.ProfitStrategy;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.*;

public class ProfitStrategyImplType2 implements ProfitStrategy {
    NumberFormat nf = NumberFormat.getNumberInstance();
    DrinkRepository drinkRepository = new DrinkRepositoryImpl();
//    private PriceService priceService = new PriceServiceImpl();

    public RuleResult profitProcess(RuleContext ruleContext, Rule rule, int max) {
        //TODO 相当于原来的RuleServiceImpl.java里的private RuleResult discountType2(RuleContext ruleContext, Rule rule)打折
        nf.setMaximumFractionDigits(2);
        Order order = ruleContext.getOrder();
        DrinkRepository drinkUtil = new DrinkRepositoryImpl();
        double discount = 0.0;
        //对全体对象全体价格打折
        if (rule.getIsOnlyBasicsDrinks() == 0 && rule.getDiscountRange() == null) {
            discount += ruleContext.getPurePrice() * (1 - rule.getProfit());
        } else {
            //对某些特定商品打折
            List<RuleRepositoryImpl.Item> condition = rule.getDiscountRange();
//            int[] num = new int[condition.size()];
//            //先记录订单中出现的所有饮品的数量
            Map<String, Integer> drinkNum = new HashMap<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                String key = orderItem.getName() + "#" + orderItem.getSize();
                if (!drinkNum.containsKey(key)) {
                    drinkNum.put(key, 1);
                } else {
                    drinkNum.put(key, drinkNum.get(key) + 1);
                }
            }
            //计算打折次数
//            int max = Integer.MAX_VALUE;
            Map<String, Integer> require = new HashMap<>();
//
            for (RuleRepositoryImpl.Item require1 : condition) {
//                int index = condition.indexOf(require1);
                for (Drinks drinks : require1.getDrinksList()) {
                    if (drinkNum.containsKey(drinks.getName() + "#" + drinks.getSize())) {
                        require.put(drinks.getName() + "#" + drinks.getSize(), drinkNum.get(drinks.getName() + "#" + drinks.getSize()));
                    }
                }
//                int temp = (int) (num[index] / require1.getNumber());
//                if (temp < max) {
//                    //记录最多送多少次
//                    max = temp;
//                }
            }
            if (rule.getFreeDrinks() == null) {

                //开始打折
                List<RuleRepositoryImpl.Item> discountRange = rule.getDiscountRange();
                for (RuleRepositoryImpl.Item processObject : discountRange) {
                    if (processObject.getRequiretType() == 0) {
                        //表示对drinklist里面的所有饮品打折
                        if (processObject.getNumber() == 0) {
                            for (Drinks drinks : processObject.getDrinksList()) {
                                if (rule.getIsOnlyBasicsDrinks() == 1) {
                                    if (drinkNum.containsKey(drinks.getName() + "#" + drinks.getSize())) {
                                        discount +=  drinkRepository.getDrink(drinks.getName()).getPrice() * drinkNum.get(drinks.getName() + "#" + drinks.getSize()) * (1 - rule.getProfit());
                                    }

                                }
                            }
                        }
                    } else {
                        //最多打折几杯
                        //默认按照基础价格从高到低的给 此处就不手动排序了
//                    List<Map.Entry<String, Integer>> sortList = new ArrayList<Map.Entry<String, Integer>>(require.entrySet());
//                    Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
//                        @Override
//                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                            if (rule.getIsOnlyBasicsDrinks() == 1) {
//                                return (int) drinkUtil.getDrink(o2.getKey().split("#")[0]).getPrice() * 100 - (int) drinkUtil.getDrink(o1.getKey().split("#")[0]).getPrice() * 100;
//                            }
//                            return 1;
//                        }
//                    });
//
                        int remain = (int) (max * processObject.getNumber());
                        for (Drinks drinks : processObject.getDrinksList()) {
//                        int remain = (int)(max*processObject.getNumber());
                            if (rule.getIsOnlyBasicsDrinks() == 1) {
//                                if(drinkNum.get(drinks)!=null){
//                                    discount += drinks.getPrice() * drinkNum.get(drinks) * (1 - rule.getProfit());
//                                }
                                if (remain > 0) {
                                    if (require.containsKey(drinks.getName() + "#" + drinks.getSize())) {
                                        if (require.get(drinks.getName() + "#" + drinks.getSize()) - remain > 0) {
                                            discount += drinkRepository.getDrink(drinks.getName()).getPrice() * remain * (1 - rule.getProfit());
                                            break;
                                        } else {
                                            discount += drinkRepository.getDrink(drinks.getName()).getPrice() * require.get(drinks.getName() + "#" + drinks.getSize()) * (1 - rule.getProfit());
                                            remain -= require.get(drinks.getName() + "#" + drinks.getSize());
                                        }
                                    }

                                } else {
                                    break;
                                }

                            }
//                        else {
//
//                            if (rule.isCanAdd()) {
//                                if (drinkNum.get(drinks.getName()+"#"+drinks.getSize())!=null){
//                                    discount += drinks.cost() * drinkNum.get(drinks.getName()+"#"+drinks.getSize()) * max * (1 - rule.getProfit());
//                                }
//                            } else {
//
//                                if (remain > 0) {
//                                    if (drinkNum.get(drinks.getName()+"#"+drinks.getSize())!=null){
//                                        if (drinkNum.get(drinks.getName()+"#"+drinks.getSize()) - remain > 0) {
//                                            discount += drinks.cost() * remain * (1 - rule.getProfit());
//                                            break;
//                                        } else {
//                                            discount += drinks.cost() * drinkNum.get(drinks.getName()+"#"+drinks.getSize()) * (1 - rule.getProfit());
//                                            remain -= drinkNum.get(drinks.getName()+"#"+drinks.getSize());
//                                        }
//                                    }
//
//                                } else {
//                                    break;
//                                }
//                            }
//                        }
                        }
                    }

                }
            }
        }

        return new RuleResult(rule, Double.parseDouble(nf.format(discount)), MessageFormat.format(EnvironmentContext.getEnvironmentContext().getBundle().getString("RULE_DES_"+rule.getId()),EnvironmentContext.getEnvironmentContext().getCurrentCurrencySymbol()));
    }
}
