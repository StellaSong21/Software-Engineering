package fudan.se.lab4.service.impl;

import com.sun.tools.corba.se.idl.constExpr.Or;
import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.context.OrderContext;
import fudan.se.lab4.dto.*;
import fudan.se.lab4.service.CalWholeDrinkService;
import fudan.se.lab4.service.OrderService;
import fudan.se.lab4.util.DrinkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
//    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private CalWholeDrinkService calWholeDrinkService = new CalWholeDrinkServiceImpl();

    @Override
    public PaymentInfo pay(OrderContext orderContext) {//
        try {
            EnvironmentContext environmentContext=EnvironmentContext.getEnvironmentContext();
//            order.setId(environmentContext.createAnOrder()+"");
            environmentContext.changeLanguage(orderContext.getLanguage());
            environmentContext.changeCurrency(orderContext.getCurrency());
            Order order=orderContext.getOrder();
            DrinkUtil.isOrderValid(order);
            order.setId(environmentContext.createAnOrder()+"");
            double price = getPaymentInfoPrice(order);
            double discount = 0.0;
            List<String> msgs;

            OrderRuleServiceImpl orderRuleService = new OrderRuleServiceImpl();
            PromotionResult promotionResult = orderRuleService.calFinalDiscount(order, price);
            discount = promotionResult.getDiscount();
            msgs = promotionResult.getPromotionType();
            double discountPrice = price - discount;



            String idStr = order.getId();
            String totalPriceStr = price + EnvironmentContext.getEnvironmentContext().getCurrentCurrencySymbol();
            String discountPriceStr = discount + EnvironmentContext.getEnvironmentContext().getCurrentCurrencySymbol();
            String finalPriceStr = discountPrice + EnvironmentContext.getEnvironmentContext().getCurrentCurrencySymbol();
            String msgsStr = "";
            for(String s:msgs){
                msgsStr += s+";";
            }
            if("".equals(msgsStr)){
                msgsStr = "empty";
            }

            LoggerServiceImpl.getLoggerService().log(MessageFormat.format(InfoConstant.LOG_SUCC_FORMAT,idStr,totalPriceStr,discountPriceStr,finalPriceStr,msgsStr),InfoConstant.LOGGER_SUCCESS_STATUS);
            return getPaymentInfo(price, discount, discountPrice, msgs);
        }catch (Exception e){
            if(orderContext.getOrder() != null){
                LoggerServiceImpl.getLoggerService().log(MessageFormat.format(InfoConstant.LOG_FAIL_FORMAT,orderContext.getOrder().getId(),e.getMessage()),InfoConstant.LOGGER_FAIL_STATUS);
            }else {
                LoggerServiceImpl.getLoggerService().log(e.getMessage(),InfoConstant.LOGGER_FAIL_STATUS);
            }
            return null; //TODO ??
        }

    }


    private PaymentInfo getPaymentInfo(double price, double discount, double discountPrice, List<String> msgs) {
        return new PaymentInfo(price, discount, discountPrice, msgs);
    }

    private double getPaymentInfoPrice(Order order) {
        double totalPrice = 0.0;
        for (OrderItem orderItem : order.getOrderItems()) {
            totalPrice += calWholeDrinkService.wholePrice(orderItem);
        }
        return totalPrice;
    }

}
