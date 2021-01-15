package fudan.se.lab4.entity;

import fudan.se.lab4.service.LoggerService;
import fudan.se.lab4.service.impl.LoggerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class Coffee extends Drinks {

    private String name;
    private String description;
    private double price;
    private int size;
    private List<Double> costOfSize;
    private static Logger logger = LoggerFactory.getLogger(Coffee.class);
    private LoggerService loggerService = new LoggerServiceImpl();
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

    public double priceOfSize() {
        //big-size3-$6 middle-size2-$4 small-size1-$2
        if (getSize() <= 0 || getSize() >= 4) {
            logger.info(loggerService.log("SIZE_INVALID"));
            throw new RuntimeException(loggerService.log("SIZE_INVALID"));
        }
        return getSize() * 2.0;
    }

    public abstract double cost();

    private void failToCreate() {
        logger.info(loggerService.log("COFFEE_FAILED_CREATE"));
        throw new RuntimeException(loggerService.log("COFFEE_FAILED_CREATE"));
    }
}
