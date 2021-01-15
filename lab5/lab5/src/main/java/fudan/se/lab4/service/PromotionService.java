package fudan.se.lab4.service;

import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.PromotionResult;

//  处理 促销活动的接口
public interface PromotionService {
    PromotionResult chooseRules(Order order, double purePrice);
}
