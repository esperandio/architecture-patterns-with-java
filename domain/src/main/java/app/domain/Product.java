package app.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

record AllocatedEvent(String orderId, String sku, int qty, String batchReference) implements Event {};

public class Product {
    private String sku;
    private Instant version;
    private List<Batch> batches = new ArrayList<Batch>();
    private final List<Event> domainEvents = new ArrayList<Event>();

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

    public long getVersion() {
        return this.version.toEpochMilli();
    }

    public int getAvailableQuantity() {
        return this.batches.stream().mapToInt(x -> x.getAvailableQuantity()).sum();
    }

    public ReadOnlyList<Event> getDomainEvents() {
        return new ReadOnlyList<Event>(this.domainEvents);
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

        String batchReference = batch.get().getReference();

        this.domainEvents.add(new AllocatedEvent(orderId, sku, quantity, batchReference));

        this.version = Instant.now();

        return batchReference;
    }

    public void deallocate(String orderId, String sku, int quantity) {
        var orderLine = new OrderLine(orderId, sku, quantity);

        Optional<Batch> batch = this.batches.stream()
            .filter((x) -> x.hasOrderLine(orderLine))
            .findFirst();
        
        if (batch.isEmpty()) {
            return;
        }

        batch.get().deallocate(orderLine);

        this.version = Instant.now();
    }

    public void addBatch(String reference, String sku, int purchasedQuantity, LocalDateTime eta) {
        var batch = new Batch(reference, sku, purchasedQuantity, eta);

        this.batches.add(batch);

        this.version = Instant.now();
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
