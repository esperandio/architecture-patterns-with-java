package app.domain;

import java.util.*;
import java.time.LocalDateTime;

public class Batch {
    private String reference;
    private String sku;
    private int purchasedQuantity;
    private Optional<LocalDateTime> eta;
    private List<OrderLine> orderLines;

    protected Batch() {}

    public Batch(String reference, String sku, int purchasedQuantity) {
        this(reference, sku, purchasedQuantity, Optional.empty());
    }

    public Batch(String reference, String sku, int purchasedQuantity, Optional<LocalDateTime> eta) {
        this(reference, sku, purchasedQuantity, eta, new ArrayList<OrderLine>());
    }

    public Batch(String reference, String sku, int purchasedQuantity, Optional<LocalDateTime> eta, List<OrderLine> orderLines) {
        this.reference = reference;
        this.sku = sku;
        this.purchasedQuantity = purchasedQuantity;
        this.eta = eta;
        this.orderLines = orderLines;
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

    public Optional<LocalDateTime> getEta() {
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
            || !orderLine.getSku().equals(this.sku)
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

    public void deallocate(OrderLine orderLine) {
        if (!this.hasOrderLine(orderLine)) {
            return;
        }

        this.orderLines.remove(orderLine);
    }

    @Override
    public boolean equals(Object o) {
        Batch batch = (Batch) o;

        if (batch == null) {
            return false;
        }

        return batch.reference.equals(this.reference);
    }
}
