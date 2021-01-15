package fudan.se.lab4.util;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.dto.Order;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.dto.Ingredient;
import fudan.se.lab4.repository.impl.CappuccinoRepositoryImpl;
import fudan.se.lab4.repository.impl.EspressoRepositoryImpl;
import fudan.se.lab4.repository.impl.GreenTeaRepositoryImpl;
import fudan.se.lab4.repository.impl.RedTeaRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrinkUtil {
    private static Logger logger = LoggerFactory.getLogger(DrinkUtil.class);
    public static Drinks getDrinks(String name) {
        if(name ==null){
            logger.info(InfoConstant.FAILED_GET_DRINK);
            throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
        }
        switch (name) {
            case "cappuccino":
                return new CappuccinoRepositoryImpl().getCappuccino(name);
            case "espresso":
                return new EspressoRepositoryImpl().getEspresso(name);
            case "greenTea":
                return new GreenTeaRepositoryImpl().getGreenTea(name);
            case "redTea":
                return new RedTeaRepositoryImpl().getRedTea(name);
            default: {
                logger.info(InfoConstant.FAILED_GET_DRINK);
                throw new RuntimeException(InfoConstant.FAILED_GET_DRINK);
            }
        }
    }

    public static void isDrinkIngredientValid(Ingredient ingredient) {
        if (ingredient.getNumber() < 0 || ingredient.getName() == null || !(ingredient.getName().equals("milk") || ingredient.getName().equals("chocolate") || ingredient.getName().equals("cream") || ingredient.getName().equals("sugar"))) {
            logger.info(InfoConstant.INVALID_INGREDIENT);
            throw new RuntimeException(InfoConstant.INVALID_INGREDIENT);
        }
    }

    public static void isOrderValid(Order order) {
        if (order == null || order.getOrderItems() == null || order.getOrderItems().size() == 0) {
            logger.info(InfoConstant.ORDER_WRONG);
            throw new RuntimeException(InfoConstant.ORDER_WRONG);
        }
    }

    //对size检测已经在之前就有了
    public static void isDrinksValid(Drinks drinks) {
        if (drinks.getPrice() <= 0) {
            logger.info(InfoConstant.ORDER_WRONG);
            throw new RuntimeException(InfoConstant.ORDER_WRONG);
        }
    }
}
