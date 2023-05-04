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
    }
}