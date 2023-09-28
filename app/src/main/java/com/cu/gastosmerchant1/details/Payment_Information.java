package com.cu.gastosmerchant1.details;

import java.io.Serializable;

public class Payment_Information implements Serializable {
    // dataclass for the payment;
    String upiId;
    String rawString;
    String upiName;
    boolean isPrimary;
    String merchantId;

    public Payment_Information() {
    }

    public Payment_Information(String upiId, String rawString, String upiName, boolean isPrimary, String merchantId) {
        this.upiId = upiId;
        this.rawString = rawString;
        this.upiName = upiName;
        this.isPrimary = isPrimary;
        this.merchantId = merchantId;
    }

    public Payment_Information(String upiId, String rawString, String upiName, boolean isPrimary) {
        this.upiId = upiId;
        this.rawString = rawString;
        this.upiName = upiName;
        this.isPrimary = isPrimary;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getRawString() {
        return rawString;
    }

    public void setRawString(String rawString) {
        this.rawString = rawString;
    }

    public String getUpiName() {
        return upiName;
    }

    public void setUpiName(String upiName) {
        this.upiName = upiName;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}