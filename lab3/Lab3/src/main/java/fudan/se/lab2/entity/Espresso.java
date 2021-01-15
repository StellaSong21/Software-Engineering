package fudan.se.lab2.entity;


public class Espresso extends Coffee {

    @Override
    public double cost() {
        return priceOfSize() + getPrice();
    }


}
