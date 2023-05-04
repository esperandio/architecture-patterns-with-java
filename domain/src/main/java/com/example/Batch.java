package com.example;

import java.util.ArrayList;

public class Batch {
    private String reference;
    private String sku;
    private int purchasedQuantity;
    private ArrayList<OrderLine> orderLines;

    public Batch(String reference, String sku, int purchasedQuantity) {
        this.reference = reference;
        this.sku = sku;
        this.purchasedQuantity = purchasedQuantity;
        this.orderLines = new ArrayList<OrderLine>();
    }

    public String getReference() {
        return this.reference;
    }

    public String getSku() {
        return this.sku;
    }

    public int getPurchasedQuantity() {
        return this.purchasedQuantity;
    }

    public void allocate(OrderLine orderLine) {
        if (orderLine.getQuantity() > this.getAvailableQuantity()) {
            return;
        }

        this.orderLines.add(orderLine);
    }

    public int getAllocatedQuantity() {
        return this.orderLines.stream().mapToInt((x) -> x.getQuantity()).sum();
    }

    public int getAvailableQuantity() {
        return this.purchasedQuantity - this.getAllocatedQuantity();
    }
}
