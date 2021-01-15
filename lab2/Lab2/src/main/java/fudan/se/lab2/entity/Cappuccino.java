package fudan.se.lab2.entity;


public class Cappuccino extends Coffee {

    @Override
    public double cost() {
        //最终价格=咖啡价格+杯型价格
        return priceOfSize() + getPrice();
    }


}
