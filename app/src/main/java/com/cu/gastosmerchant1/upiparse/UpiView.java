package com.cu.gastosmerchant1.upiparse;

public class UpiView {
    private int resourceId;
    private String upiId;
    private String upiName;
    private boolean isPrimary;

    public UpiView(int resourceId, String upiId, String upiName, boolean isPrimary) {
        this.resourceId = resourceId;
        this.upiId = upiId;
        this.upiName = upiName;
        this.isPrimary = isPrimary;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getUpiName() {
        return upiName;
    }

    public void setUpiName(String upiName) {
        this.upiName = upiName;
    }
}
