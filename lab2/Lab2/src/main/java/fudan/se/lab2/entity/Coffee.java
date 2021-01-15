package fudan.se.lab2.entity;

import fudan.se.lab2.constant.InfoConstant;
import fudan.se.lab2.service.impl.PriceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Coffee {
    private String name;
    private String description;
    private double price;
    private int size;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public abstract double cost();


    //返回杯型价格
    protected double priceOfSize() {
        //大杯size3价格6 中杯size2价格4 小杯size1价格2
        checkSize();
        return getSize() * 2.0;
    }

    private static Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);
    //检测杯型取值
    protected void checkSize() {
        if (getSize() <= 0 || getSize() >= 4) {
            setSize(3);
            logger.info(InfoConstant.INVALID_SIZE);
        }
    }
}
