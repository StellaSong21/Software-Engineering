package fudan.se.lab4.service.impl;

import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.PromotionResult;
import fudan.se.lab4.service.OrderRuleService;

public class OrderRuleServiceImpl {
    public PromotionResult calFinalDiscount(Order order, double purePrice) {
        PromotionServiceImpl promotionService = new PromotionServiceImpl();
        return promotionService.chooseRules(order, purePrice);
    }
}
