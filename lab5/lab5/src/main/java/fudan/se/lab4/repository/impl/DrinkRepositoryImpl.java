package fudan.se.lab4.repository.impl;

import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.service.LoggerService;
import fudan.se.lab4.service.impl.LoggerServiceImpl;
import fudan.se.lab4.util.FileUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DrinkRepositoryImpl implements DrinkRepository {
    private LoggerService loggerService = new LoggerServiceImpl();
    public Drinks getDrink(String name){
        try {
           return stringArrayToObject(FileUtil.readByName(name, FileConstant.DRINKS_CSV));
        } catch (Exception e) {
            if(e.getMessage().equals(MessageFormat.format(loggerService.log("ENTITY_NOT_FOUND"),name))) {
                for(String[] sArr: EnvironmentContext.getEnvironmentContext().getSpecialDrinks()){
                    if(sArr[0].equals(name)){
                        return stringArrayToObject(sArr);
                    }
                }
                throw e;
            }
            throw e;
        }
    }
    private Drinks stringArrayToObject(String[] array) {
        Drinks drink = new Drinks();
        drink.setName(array[0]);
        drink.setDescription(array[1]);
        drink.setPrice(Double.parseDouble(array[2]));
        drink.setSize(Integer.parseInt(array[3]));
        List<Double> costOfSize = new ArrayList<Double>();
        for(int i = 4; i < array.length; i++){
            costOfSize.add(Double.parseDouble(array[i]));
        }
        drink.setCostOfSize(costOfSize);
        return drink;
    }
}
