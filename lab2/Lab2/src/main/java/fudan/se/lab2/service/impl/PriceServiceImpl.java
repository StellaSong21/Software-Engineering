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
        double cost = 0.0;
        //多杯最终价格=单杯最终价格*杯数
        for (Map.Entry<Coffee, Integer> coffeeIntegerEntry : order.entrySet()) {
            checkInteger(coffeeIntegerEntry.getValue());

            double spent = coffeeIntegerEntry.getKey().cost() * coffeeIntegerEntry.getValue();
            cost += spent;
            logger.info(MessageFormat.format(InfoConstant.ORDER_INFORMATION,
                    coffeeIntegerEntry.getKey().getName(), coffeeIntegerEntry.getKey().getSize(), coffeeIntegerEntry.getValue(), spent));
        }
        return cost;
    }


    private void checkInteger(Integer integer) {
        if (!(integer.compareTo(0) > 0)) {
            logger.info(InfoConstant.ORDER_NEGATIVE_NUM);
            throw new RuntimeException();
        }
    }


}
