package fudan.se.lab4.entity;

import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Rule {
    private int groupId; //优惠分组的组别
    private int scope;  //资格范围，为0代表无限制，所有客户全都可以享受，这一部分可以进一步扩展成资格类型和资格配置
    private int profitType; //利益类型,0是满减，1是满赠，2是打折
    private double profit; //优惠 打8折就是0.8 送1杯就是1
    private boolean canAdd;//是否支持累加
    private Date from;
    private Date to;
    private int isOnlyBasicsDrinks;
    private List<RuleRepositoryImpl.Item> discountRange;
    private List<RuleRepositoryImpl.Item> orderCondition;
    private List<RuleRepositoryImpl.Item> freeDrinks; //如果送饮料，送的类型/数量 不送的优惠为null
    private String descirption;

    public Rule(){

    }
    public Rule(int groupId,int scope,int profitType,double profit, boolean canAdd, Date from, Date to, int isOnlyBasicsDrinks, List<RuleRepositoryImpl.Item> orderCondition, List<RuleRepositoryImpl.Item> discountRange, List<RuleRepositoryImpl.Item> freeDrinks, String descirption) {
        this.groupId = groupId;
        this.scope = scope;
        this.profitType = profitType;
        this.profit = profit;
        this.canAdd = canAdd;
        this.from = from;
        this.to = to;
        this.isOnlyBasicsDrinks = isOnlyBasicsDrinks;
        this.discountRange = discountRange;
        this.orderCondition = orderCondition;
        this.isOnlyBasicsDrinks = isOnlyBasicsDrinks;
        this.freeDrinks = freeDrinks;
        this.descirption = descirption;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getScope() {
        return scope;
    }

    public int getProfitType() {
        return profitType;
    }

    public double getProfit() {
        return profit;
    }

    public List<RuleRepositoryImpl.Item> getFreeDrinks() {
        return freeDrinks;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public List<RuleRepositoryImpl.Item> getDiscountRange() {
        return discountRange;
    }

    public List<RuleRepositoryImpl.Item> getOrderCondition() {
        return orderCondition;
    }

    public int getIsOnlyBasicsDrinks() {
        return isOnlyBasicsDrinks;
    }

    public String getDescirption() {
        return descirption;
    }
}
