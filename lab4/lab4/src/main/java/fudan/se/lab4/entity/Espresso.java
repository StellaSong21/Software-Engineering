package fudan.se.lab4.entity;


public class Espresso extends Coffee {

    @Override
    public double cost() {
        return priceOfSize() + getPrice();
    }


}
