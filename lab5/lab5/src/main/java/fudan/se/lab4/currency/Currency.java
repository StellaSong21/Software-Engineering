package fudan.se.lab4.currency;

public class Currency {
    private String type;
    private String symbol;
    private double exrate;//汇率与人民币的转换

    public  Currency(String type, String symbol, double exrate) {
        this.type = type;
        this.symbol = symbol;
        this.exrate = exrate;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getExrate() {
        return exrate;
    }

    public void setExrate(double exrate) {
        this.exrate = exrate;
    }
}