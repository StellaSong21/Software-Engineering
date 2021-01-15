package fudan.se.lab4.service;

import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.PromotionResult;

public interface OrderRuleService {
    PromotionResult calFinalDiscount(Order order, double purePrice);
}
