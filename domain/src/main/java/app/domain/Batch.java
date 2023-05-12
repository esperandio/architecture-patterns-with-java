package app.domain;

import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate;

public class Batch {
    private String reference;
    private String sku;
    private int purchasedQuantity;
    private Optional<LocalDate> eta;
    private ArrayList<OrderLine> orderLines;

    public Batch(String reference, String sku, int purchasedQuantity) {
        this(reference, sku, purchasedQuantity, Optional.empty());
    }

    public Batch(String reference, String sku, int purchasedQuantity, Optional<LocalDate> eta) {
        this.reference = reference;
        this.sku = sku;
        this.purchasedQuantity = purchasedQuantity;
        this.eta = eta;
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

    public Optional<LocalDate> getEta() {
        return this.eta;
    }

    public int getAllocatedQuantity() {
        return this.orderLines.stream().mapToInt((x) -> x.getQuantity()).sum();
    }

    public int getAvailableQuantity() {
        return this.purchasedQuantity - this.getAllocatedQuantity();
    }

    public boolean canAllocate(OrderLine orderLine) {
        if (
            orderLine.getQuantity() > this.getAvailableQuantity()
            || this.hasOrderLine(orderLine)
            || orderLine.getSku() != this.sku
        ) {
            return false;
        }

        return true;

    }

    private boolean hasOrderLine(OrderLine orderLine) {
        return this.orderLines.stream().filter((x) -> x.equals(orderLine)).count() > 0;
    }

    public void allocate(OrderLine orderLine) {
        if (!this.canAllocate(orderLine)) {
            return;
        }

        this.orderLines.add(orderLine);
    }
}
