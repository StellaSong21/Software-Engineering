package fudan.se.lab4.entity;

import fudan.se.lab4.constant.InfoConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Tea extends Drinks {
    private static Logger logger = LoggerFactory.getLogger(Tea.class);

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
            logger.info(InfoConstant.INVALID_SIZE);
            throw new RuntimeException(InfoConstant.INVALID_SIZE);
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

    double priceOfSize() {
        switch (getSize()){
            case 1: return 2;
            case 2: return 4;
            case 3: return 5;
            default:{
                logger.info(InfoConstant.INVALID_SIZE);
                throw new RuntimeException(InfoConstant.INVALID_SIZE);
            }
        }
    }

    private void failToCreate() {
        logger.info(InfoConstant.CREATE_TEA_FAILED);
        throw new RuntimeException(InfoConstant.CREATE_TEA_FAILED);
    }
}
