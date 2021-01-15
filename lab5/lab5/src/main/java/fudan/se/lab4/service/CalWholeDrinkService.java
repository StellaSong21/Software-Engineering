package fudan.se.lab4.service;

import fudan.se.lab4.dto.OrderItem;


public interface CalWholeDrinkService {
    /**
     *
     * @param orderItem 订单内单个商品
     * @return 换算后按当前货币种类计算好的价格
     */
    Double wholePrice(OrderItem orderItem);
}
