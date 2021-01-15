package fudan.se.lab4.dto;

import java.util.ArrayList;
import java.util.List;

public class PromotionResult {
    private List<String> promotionType;
    private double discount;
    public PromotionResult(List<String> promotionType , double discount){
        this.promotionType = promotionType;
        this.discount = discount;
    }

    public List<String> getPromotionType() {
        return promotionType;
    }

    public double getDiscount() {
        return discount;
    }
}
