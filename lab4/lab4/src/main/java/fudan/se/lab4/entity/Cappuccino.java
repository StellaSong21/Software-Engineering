package fudan.se.lab4.entity;


public class Cappuccino extends Coffee {

    @Override
    public double cost() {

        return priceOfSize() + getPrice();
    }


}
