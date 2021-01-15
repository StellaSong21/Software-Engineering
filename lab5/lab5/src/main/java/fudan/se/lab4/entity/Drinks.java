package fudan.se.lab4.entity;

import java.util.List;

public class Drinks {
    private String name;
    private String description;
    private double price;
    private int size;
    private List<Double> costOfSize;

    public String getName() {
        return name;
    }
    public int getSize() {
        return size;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public List<Double> getCostOfSize() {
        return costOfSize;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setSizeInSaleRule(int size){
        this.size = size;
    }
    public void setCostOfSize(List<Double> costOfSize) {
        this.costOfSize = costOfSize;
    }
    public double cost() {
        return priceOfSize() + getPrice();
    }
    public double priceOfSize() {
        return costOfSize.get(this.size-1);
    }

}
