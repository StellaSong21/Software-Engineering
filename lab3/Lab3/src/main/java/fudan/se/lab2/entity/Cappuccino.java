package fudan.se.lab2.entity;


public class Cappuccino extends Coffee {

    @Override
    public double cost() {

        return priceOfSize() + getPrice();
    }


}
