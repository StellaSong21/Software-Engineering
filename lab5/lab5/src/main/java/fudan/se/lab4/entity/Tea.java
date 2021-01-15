package fudan.se.lab4.entity;

import fudan.se.lab4.service.LoggerService;
import fudan.se.lab4.service.impl.LoggerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class Tea extends Drinks {
    private static Logger logger = LoggerFactory.getLogger(Tea.class);
    private LoggerService loggerService = new LoggerServiceImpl();
    private String name;
    private String description;
    private double price;
    private int size;
    private List<Double> costOfSize;
    public void setDescription(String description) {
        if (!"".equals(description)) {
            this.description = description;
        } else {
            failToCreate();
        }

    }

    public void setSize(int size) {
        if (size >= 1 && size <= 3) {
            this.size = size;
        } else {
            logger.info(loggerService.log("SIZE_INVALID"));
            throw new RuntimeException(loggerService.log("SIZE_INVALID"));
        }
    }

    public void setName(String name) {
        if (!"".equals(name)) {
            this.name = name;
        } else {
            failToCreate();
        }

    }

    public void setPrice(double price) {
        this.price = price;
    }

    public abstract double cost();

    public double priceOfSize() {
        switch (getSize()){
            case 1: return 2;
            case 2: return 4;
            case 3: return 5;
            default:{
                logger.info(loggerService.log("SIZE_INVALID"));
                throw new RuntimeException(loggerService.log("SIZE_INVALID"));
            }
        }
    }

    private void failToCreate() {
        logger.info(loggerService.log("TEA_FAILED_CREATE"));
        throw new RuntimeException(loggerService.log("TEA_FAILED_CREATE"));
    }
}
