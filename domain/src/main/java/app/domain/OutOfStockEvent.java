package app.domain;

public record OutOfStockEvent(String sku) implements Event {};
