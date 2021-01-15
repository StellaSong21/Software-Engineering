package fudan.se.lab4.service.impl;

import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.dto.Ingredient;
import fudan.se.lab4.dto.OrderItem;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.repository.impl.IngredientRepositoryImpl;
import fudan.se.lab4.service.CalWholeDrinkService;
import fudan.se.lab4.service.LoggerService;
import fudan.se.lab4.service.PriceService;
import fudan.se.lab4.util.DrinkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalWholeDrinkServiceImpl implements CalWholeDrinkService {
    private LoggerService loggerService = new LoggerServiceImpl();
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private PriceService priceService = new PriceServiceImpl();
    public Double wholePrice(OrderItem orderItem){
        double price = 0.0;
        Drinks drinks = new DrinkRepositoryImpl().getDrink(orderItem.getName()); //orderItem.getName()
        drinks.setSize(orderItem.getSize());
        price += priceService.charge(drinks.cost(), EnvironmentContext.getEnvironmentContext().getCurrencyNow()) ;
        if(orderItem.getIngredients() == null){
            logger.info(loggerService.log("INGREDIENT_INVALID"));
            throw new RuntimeException(loggerService.log("INGREDIENT_INVALID"));
        }
        for (Ingredient ingredient : orderItem.getIngredients()) {
            DrinkUtil.isDrinkIngredientValid(ingredient);
            price += priceService.charge(new IngredientRepositoryImpl().getIngredient(ingredient.getName()).getPrice(),EnvironmentContext.getEnvironmentContext().getCurrencyNow()) * ingredient.getNumber();
        }
        return price;
    }
}
