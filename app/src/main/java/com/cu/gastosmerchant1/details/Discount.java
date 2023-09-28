package com.cu.gastosmerchant1.details;

import java.io.Serializable;

public class Discount implements Serializable {
    private int minBillAmount;
    private int discountPercentage;

    public Discount() {
    }

    public Discount(int minBillAmount, int discountPercentage) {
        this.minBillAmount = minBillAmount;
        this.discountPercentage = discountPercentage;
    }

    public int getMinBillAmount() {
        return minBillAmount;
    }

    public void setMinBillAmount(int minBillAmount) {
        this.minBillAmount = minBillAmount;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
