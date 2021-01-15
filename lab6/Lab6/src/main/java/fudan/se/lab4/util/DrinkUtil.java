package fudan.se.lab4.util;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.dto.Ingredient;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.service.LoggerService;
import fudan.se.lab4.service.impl.LoggerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrinkUtil {
//    private static Logger logger = LoggerFactory.getLogger(DrinkUtil.class);
//    private static LoggerService loggerService = new LoggerServiceImpl();
    public static void isDrinkIngredientValid(Ingredient ingredient) {
        if (ingredient.getNumber() < 0 || ingredient.getName() == null || !(ingredient.getName().equals("milk") || ingredient.getName().equals("chocolate") || ingredient.getName().equals("cream") || ingredient.getName().equals("sugar"))) {
//            LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(InfoConstant.INGREDIENT_INVALID);
            throw new RuntimeException(InfoConstant.INGREDIENT_INVALID);
        }
    }

    public static void isOrderValid(Order order) {
        if (order == null || order.getOrderItems() == null || order.getOrderItems().size() == 0) {
//            LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(InfoConstant.ORDER_WRONG);
            throw new RuntimeException(InfoConstant.ORDER_WRONG);
        }
    }

    //对size检测已经在之前就有了
    public static void isDrinksValid(Drinks drinks) {
        if (drinks.getPrice() <= 0) {
//            LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(loggerService.log("ORDER_WRONG"));
            throw new RuntimeException(InfoConstant.ORDER_WRONG);
        }
    }
}
