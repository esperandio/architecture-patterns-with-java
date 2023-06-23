package app.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Product {
    private String sku;
    private List<Batch> batches;

    protected Product() {}

    public Product(String sku) {
        this(sku, new ArrayList<Batch>());
    }

    public Product(String sku, List<Batch> batches) {
        this.sku = sku;
        this.batches = batches;
    }

    public String getSku() {
        return this.sku;
    }

    public String allocate(String orderId, String sku, int quantity) {
        var orderLine = new OrderLine(orderId, sku, quantity);

        Optional<Batch> batch = this.batches.stream()
            .sorted((x, y) -> x.getEta().orElse(LocalDateTime.now()).compareTo(y.getEta().orElse(LocalDateTime.now())))
            .filter((x) -> x.canAllocate(orderLine))
            .findFirst();

        if (batch.isEmpty()) {
            return "";
        }

        batch.get().allocate(orderLine);

        return batch.get().getReference();
    }

    public void addBatch(String reference, String sku, int purchasedQuantity, LocalDateTime eta) {
        var batch = new Batch(reference, sku, purchasedQuantity, eta);

        this.batches.add(batch);
    }

    @Override
    public boolean equals(Object o) {
        Product product = (Product) o;

        if (product == null) {
            return false;
        }

        return product.sku.equals(this.sku);
    }
}
