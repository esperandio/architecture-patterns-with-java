package com.example;

public class OrderLine {
    private String orderId;
    private String sku;
    private int quantity;

    public OrderLine(String orderId, String sku, int quantity) {
        this.orderId = orderId;
        this.sku = sku;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getSku() {
        return this.sku;
    }

    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public boolean equals(Object o) {
        OrderLine orderLine = (OrderLine) o;

        if (orderLine == null) {
            return false;
        }

        return orderLine.orderId == this.orderId
            && orderLine.quantity == this.quantity
            && orderLine.sku == this.sku;
    }
}
