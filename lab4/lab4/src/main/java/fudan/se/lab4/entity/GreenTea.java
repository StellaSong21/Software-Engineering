package fudan.se.lab4.entity;

public class GreenTea extends Tea {
    @Override
    public double cost() {
        return priceOfSize() + getPrice();
    }
}
