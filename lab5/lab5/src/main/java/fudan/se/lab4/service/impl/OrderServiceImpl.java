package fudan.se.lab4.service.impl;

import fudan.se.lab4.dto.*;
import fudan.se.lab4.service.CalWholeDrinkService;
import fudan.se.lab4.service.OrderService;
import fudan.se.lab4.util.DrinkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private CalWholeDrinkService calWholeDrinkService = new CalWholeDrinkServiceImpl();

    @Override
    public PaymentInfo pay(Order order) {
        double price = getPaymentInfoPrice(order);
        double discount = 0.0;
        List<String> msgs;

        OrderRuleServiceImpl orderRuleService = new OrderRuleServiceImpl();
        PromotionResult promotionResult = orderRuleService.calFinalDiscount(order, price);
        discount = promotionResult.getDiscount();
        msgs = promotionResult.getPromotionType();
        double discountPrice = price - discount;

        return getPaymentInfo(price, discount, discountPrice, msgs);
    }


    private PaymentInfo getPaymentInfo(double price, double discount, double discountPrice, List<String> msgs) {
        return new PaymentInfo(price, discount, discountPrice, msgs);
    }

    private double getPaymentInfoPrice(Order order) {
        DrinkUtil.isOrderValid(order);
        double totalPrice = 0.0;
        for (OrderItem orderItem : order.getOrderItems()) {
            totalPrice += calWholeDrinkService.wholePrice(orderItem);
        }
        return totalPrice;
    }

//    private Drinks getDrinks(String name) {
////        try {
////            Drinks drinks = (Drinks) Class.forName(name).newInstance();
////            return drinks;
////        } catch (ClassNotFoundException e) {
////            logger.info(InfoConstant.FAILED_GET_DRINK);
////            throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
////        } catch (InstantiationException e) {
////            logger.info(InfoConstant.FAILED_GET_DRINK);
////            throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
////        }catch (IllegalAccessException e){
////            logger.info(InfoConstant.FAILED_GET_DRINK);
////            throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
////        }
//
//        switch (name) {
//            case "cappuccino":
//                return new CappuccinoRepositoryImpl().getCappuccino(name);
//            case "espresso":
//                return new EspressoRepositoryImpl().getEspresso(name);
//            case "greenTea":
//                return new GreenTeaRepositoryImpl().getGreenTea(name);
//            case "redTea":
//                return new RedTeaRepositoryImpl().getRedTea(name);
//            default: {
//                logger.info(InfoConstant.FAILED_GET_DRINK);
//                throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
//            }
//        }
//    }
}
