package fudan.se.lab4.context;

import fudan.se.lab4.dto.Order;
import fudan.se.lab4.entity.Rule;

public class RuleContext {
    //商品列表
    private Order order;
    //客户id
//    private int userId;
    //优惠券码
//    private int codeCoup;
    //优惠规则
    private Rule rule;
    //初始价格
    private double purePrice;
    public RuleContext(Order order, Rule rule, double purePrice) {
        this.order=order;
//        this.userId=userId;
//        this.codeCoup=codeCoup;
        this.rule = rule;
        this.purePrice = purePrice;
    }

//    public int getCodeCoup() {
//        return codeCoup;
//    }
//
//    public int getUserId() {
//        return userId;
//    }

    public Order getOrder() {
        return order;
    }

    public Rule getRule() {
        return rule;
    }

    public double getPurePrice() {
        return purePrice;
    }
}
