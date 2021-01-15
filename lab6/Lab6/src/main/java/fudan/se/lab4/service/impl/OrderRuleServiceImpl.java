package fudan.se.lab4.service.impl;

import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.PromotionResult;
import fudan.se.lab4.service.OrderRuleService;

//TODO
public class OrderRuleServiceImpl implements OrderRuleService{
    public PromotionResult calFinalDiscount(Order order, double purePrice) {
        try {
            PromotionServiceImpl promotionService = new PromotionServiceImpl();
            return promotionService.chooseRules(order, purePrice);
        }catch (RuntimeException e){
            throw e;
        }

    }
}
