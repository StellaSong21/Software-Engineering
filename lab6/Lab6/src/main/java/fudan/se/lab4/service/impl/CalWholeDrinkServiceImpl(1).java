package fudan.se.lab4.service.impl;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.dto.Ingredient;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.repository.impl.IngredientRepositoryImpl;
import fudan.se.lab4.service.CalWholeDrinkService;
import fudan.se.lab4.service.LoggerService;
//import fudan.se.lab4.service.PriceService;
import fudan.se.lab4.util.DrinkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalWholeDrinkServiceImpl implements CalWholeDrinkService {
//    private LoggerService loggerService = new LoggerServiceImpl();
//    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
//    private PriceService priceService = new PriceServiceImpl();
    public Double wholePrice(OrderItem orderItem){
        double price = 0.0;
        Drinks drinks = new DrinkRepositoryImpl().getDrink(orderItem.getName()); //orderItem.getName()
        drinks.setSize(orderItem.getSize());
        price += drinks.cost() ;
        if(orderItem.getIngredients() == null){
            LoggerServiceImpl.getLoggerService().log(InfoConstant.INGREDIENT_INVALID,InfoConstant.LOGGER_FAIL_STATUS);
            throw new RuntimeException(InfoConstant.INGREDIENT_INVALID);
        }
        for (Ingredient ingredient : orderItem.getIngredients()) {
            DrinkUtil.isDrinkIngredientValid(ingredient);
            price += (new IngredientRepositoryImpl().getIngredient(ingredient.getName()).getPrice()) * ingredient.getNumber();
        }
        return price;
    }
}
