package fudan.se.lab4.service.strategy.impl;

import fudan.se.lab4.context.RuleContext;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;
import fudan.se.lab4.service.LoggerService;
import fudan.se.lab4.service.PriceService;
import fudan.se.lab4.service.impl.LoggerServiceImpl;
import fudan.se.lab4.service.impl.OrderServiceImpl;
import fudan.se.lab4.service.impl.PriceServiceImpl;
import fudan.se.lab4.service.strategy.TargetStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class TargetStrategyImpl implements TargetStrategy {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private LoggerService loggerService = new LoggerServiceImpl();

    public int isValid(RuleContext ruleContext, Rule rule) {
        if (rule.getFrom() == null && rule.getOrderCondition() == null)
            return 1;
        return min(isTimeValid(new Date(), rule.getFrom(), rule.getTo())
                , isOrderConditionValid(ruleContext, rule.getOrderCondition(), rule.isCanAdd(), rule.getProfitType()));
    }

    private int isTimeValid(Date current, Date from, Date to) {
        if (from != null && to != null) {
            return (current == from || current == to || (current.after(from) && current.before(to))) ? 1 : -1;
        }
        return Integer.MAX_VALUE;
    }

    private int isOrderConditionValid(RuleContext ruleContext, List<RuleRepositoryImpl.Item> orderCondition, Boolean canAdd, int profitType) {
        int result = Integer.MAX_VALUE;
        if (orderCondition != null) {
            for (RuleRepositoryImpl.Item require : orderCondition) {
                switch (require.getRequiretType()) {
                    case 0:
                        result = min(result, isPriceValid(ruleContext.getPurePrice(), require.getNumber(), require.getDrinksList(), canAdd));
                        break;
                    case 1:
                        result = min(result, isDrinksNumValid(ruleContext.getOrder(), require.getDrinksList(), require.getNumber(), canAdd, profitType));
                        break;
                    default:
                        logger.info(loggerService.log("PROMOTION_TYPE_NOT_EXISTED"));
                        return -1;
                }
                if (result == -1)
                    return -1;
            }
        }
        return result;
    }

    private int isPriceValid(double purePrice, double priceCondition, List<Drinks> drinksList, boolean canAdd) {
        if (drinksList != null)
            return -1;
        return (purePrice >= priceCondition) ? (canAdd ? (int) purePrice / (int) priceCondition : 1) : -1;
    }

    private int isDrinksNumValid(Order order, List<Drinks> drinksList, double number, boolean canAdd, int profitType) {
        number = (profitType == 1) ? number + 1 : number;
        if (drinksList == null) {
            return (order.getOrderItems().size() >= (int) number) ? (canAdd ? order.getOrderItems().size() / ((int) number) : 1) : -1;
        }
        int realNumber = 0;
        for (OrderItem orderItem : order.getOrderItems()) {
            realNumber += contain(drinksList, orderItem) ? 1 : 0;
        }
        return realNumber >= (int) number ? (canAdd ? realNumber / (int) number : 1) : -1;
    }

    private int min(int x, int y) {
        return (x > y) ? y : x;
    }

    private boolean contain(List<Drinks> drinksList, OrderItem orderItem) {
        for (Drinks drinks1 : drinksList) {
            if (drinks1.getName().equals(orderItem.getName())) {
                if (drinks1.getSize() == orderItem.getSize())
                    return true;
            }
        }
        return false;
    }
}
