package fudan.se.lab4.entity;

public class RedTea extends Tea {
    @Override
    public double cost() {
        return priceOfSize() + getPrice();
    }
}
