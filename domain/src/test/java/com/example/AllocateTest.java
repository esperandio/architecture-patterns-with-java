package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllocateTest {
    @Test
    void availableQuantityIsReducedWhenOrderLineIsAllocated() {
        Batch batch = new Batch("batch001", "SMALL-TABLE", 20);
        OrderLine orderLine = new OrderLine("SMALL-TABLE", 2);

        batch.allocate(orderLine);

        assertEquals(18, batch.getAvailableQuantity());
        assertEquals(2, batch.getAllocatedQuantity());
    }

    @Test
    void cannotAllocateIfAvailableSmallerThanRequired() {
        Batch batch = new Batch("batch001", "BLUE-CUSHION", 1);
        OrderLine orderLine = new OrderLine("BLUE-CUSHION", 2);

        batch.allocate(orderLine);

        assertEquals(1, batch.getAvailableQuantity());
        assertEquals(0, batch.getAllocatedQuantity());
    }
}