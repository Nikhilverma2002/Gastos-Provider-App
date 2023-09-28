package com.cu.gastosmerchant1.details;

public class CashFreeData {
    String orderId;
    String order_amount;
    String transaction_status;
    String date;
    Boolean gst_paid = true;

    public CashFreeData(String orderId, String order_amount, String transaction_status, String date) {
        this.orderId = orderId;
        this.order_amount = order_amount;
        this.transaction_status = transaction_status;
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
