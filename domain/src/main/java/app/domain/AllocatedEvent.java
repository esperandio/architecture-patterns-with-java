package app.domain;

public record AllocatedEvent(String orderId, String sku, int qty, String batchReference) implements Event {};
