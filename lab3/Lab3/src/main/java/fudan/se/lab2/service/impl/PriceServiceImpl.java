package fudan.se.lab2.service.impl;

import fudan.se.lab2.constant.InfoConstant;
import fudan.se.lab2.entity.Coffee;
import fudan.se.lab2.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Map;

public class PriceServiceImpl implements PriceService {
    private static Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);


    public double cost(Map<Coffee, Integer> order) {
        try {
            if (order == null||order.isEmpty()) {
//            logger.info(InfoConstant.ORDER_NULL);
                throw new RuntimeException(InfoConstant.ORDER_NULL);
            }

            double cost = 0.0;

            for (Map.Entry<Coffee, Integer> coffeeIntegerEntry : order.entrySet()) {
                checkEntry(coffeeIntegerEntry);

                double spent = coffeeIntegerEntry.getKey().cost() * coffeeIntegerEntry.getValue();

                cost += spent;

                logger.info(MessageFormat.format(InfoConstant.ORDER_INFORMATION,
                        coffeeIntegerEntry.getKey().getName(), coffeeIntegerEntry.getKey().getSize(), coffeeIntegerEntry.getValue(), spent));
            }
            return cost;
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            throw e;
        }


    }


    private void checkEntry(Map.Entry<Coffee,Integer> coffeeIntegerEntry) {
        if (coffeeIntegerEntry==null) {
//            logger.info(InfoConstant.ORDER_WRONG);
            throw new RuntimeException(InfoConstant.ORDER_WRONG);
        }

        if (coffeeIntegerEntry.getKey()==null) {
//            logger.info(InfoConstant.ORDER_WRONG);
            throw new RuntimeException(InfoConstant.ORDER_WRONG);
        }
        if (coffeeIntegerEntry.getKey().getName()==null||(!coffeeIntegerEntry.getKey().getName().equals("cappuccino")&&!coffeeIntegerEntry.getKey().getName().equals("espresso"))) {
//            logger.info(InfoConstant.COFFEE_NAME_WRONG);
            throw new RuntimeException(InfoConstant.COFFEE_NAME_WRONG);
        }
        if (coffeeIntegerEntry.getKey().getDescription()==null||(!coffeeIntegerEntry.getKey().getDescription().equals("cappuccino")&&!coffeeIntegerEntry.getKey().getDescription().equals("espresso"))) {
//            logger.info(InfoConstant.COFFEE_DEX_WRONG);
            throw new RuntimeException(InfoConstant.COFFEE_DEX_WRONG);
        }

        if (coffeeIntegerEntry.getKey().cost()<22||coffeeIntegerEntry.getKey().cost()>28) {
//            logger.info(InfoConstant.ORDER_WRONG);
            throw new RuntimeException(InfoConstant.ORDER_WRONG);
        }

        if (!(coffeeIntegerEntry.getValue().compareTo(0) > 0)) {
//            logger.info(InfoConstant.ORDER_INTEGER_NUM);
            throw new RuntimeException(InfoConstant.ORDER_INTEGER_NUM);
        }

    }


}
