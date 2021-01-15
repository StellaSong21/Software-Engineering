package fudan.se.lab4.context;

import fudan.se.lab4.dto.Order;

public class OrderContext {
//    private int id;//订单id
    private String language;//订单所用语言
    private String currency;//订单所用货币
    private Order order;
    public OrderContext(String language,String currency,Order order) {
//        this.id=id;
        this.language=language;
        this.currency=currency;
        this.order=order;
//        order.setId(id+"");
    }
//    public int getId() {
//        return id;
//    }

    public String getCurrency() {
        return currency;
    }

    public String getLanguage() {
        return language;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
