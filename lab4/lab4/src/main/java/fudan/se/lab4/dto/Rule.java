package fudan.se.lab4.dto;

import fudan.se.lab4.entity.Drinks;

import java.util.ArrayList;

public class Rule {
    private int groupId; //优惠分组的组别
    private int scope;  //资格范围，为0代表无限制，所有客户全都可以享受，这一部分可以进一步扩展成资格类型和资格配置
    private ArrayList<Drinks> oriented; //对象范围，此类优惠规则针对某类对象，这部分可以扩展为对象范围和对象配置，如只有大杯型的卡布奇诺买几送几等，全场优惠为null
    private int profitType; //利益类型,0是满减，1是满赠，2是打折
    private int condition; //优惠中需要满足的额度，如满200减10中额度为200
    private int discountRange; //参与优惠的范围
    private double profit; //优惠 打8折就是0.8 送1杯就是1
    private ArrayList<Drinks> freeDrinks; //如果送饮料，送的类型 不送的优惠为null
    private boolean canAdd;//是否支持累加
    public Rule(int groupId,int scope,int profitType,int condition,int discountRange,double profit,boolean canAdd,ArrayList<Drinks> oriented,ArrayList<Drinks> freeDrinks) {
        this.groupId = groupId;
        this.scope = scope;
        this.profitType = profitType;
        this.condition = condition;
        this.discountRange = discountRange;
        this.profit = profit;
        this.canAdd = canAdd;
        this.oriented = oriented;
        this.freeDrinks = freeDrinks;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getScope() {
        return scope;
    }

    public ArrayList<Drinks> getOriented() {
        return oriented;
    }

    public int getProfitType() {
        return profitType;
    }

    public int getCondition() {
        return condition;
    }

    public int getDiscountRange() {
        return discountRange;
    }

    public double getProfit() {
        return profit;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public ArrayList<Drinks> getFreeDrinks() {
        return freeDrinks;
    }
}
