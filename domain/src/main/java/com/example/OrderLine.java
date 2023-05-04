package com.example;

public class OrderLine {
    private String sku;
    private int quantity;

    public OrderLine(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
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

        return orderLine.quantity == this.quantity
            && orderLine.sku == this.sku;
    }
}
