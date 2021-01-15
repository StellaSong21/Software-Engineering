package fudan.se.lab4.entity;

public abstract class Drinks {
    protected String name;
    protected String description;
    protected double price;
    protected int size;



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
    }//允许size为0

    public abstract double cost();
}
