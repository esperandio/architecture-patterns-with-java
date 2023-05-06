package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static com.example.AllocationService.allocate;

public class AllocateTest {
    @Test
    void availableQuantityIsReducedWhenOrderLineIsAllocated() {
        var batch = new Batch("batch001", "SMALL-TABLE", 20);
        var orderLine = new OrderLine("order001", "SMALL-TABLE", 2);

        batch.allocate(orderLine);

        assertEquals(18, batch.getAvailableQuantity());
        assertEquals(2, batch.getAllocatedQuantity());
    }

    @Test
    void cannotAllocateIfAvailableSmallerThanRequired() {
        var batch = new Batch("batch001", "BLUE-CUSHION", 1);
        var orderLine = new OrderLine("order001", "BLUE-CUSHION", 2);

        batch.allocate(orderLine);

        assertEquals(1, batch.getAvailableQuantity());
        assertEquals(0, batch.getAllocatedQuantity());
    }

    @Test
    void cannotAllocateTheSameOrderLineTwice() {
        var batch = new Batch("batch001", "BLUE-VASE", 10);

        var orderLine1 = new OrderLine("order001", "BLUE-VASE", 2);
        var orderLine2 = new OrderLine("order001", "BLUE-VASE", 2);

        batch.allocate(orderLine1);
        batch.allocate(orderLine2);

        assertEquals(8, batch.getAvailableQuantity());
        assertEquals(2, batch.getAllocatedQuantity());
    }

    @Test
    void prefersCurrentStockBatchesToShipment() {
        var inStockBatch = new Batch("in-stock-batch", "RETRO-CLOCK", 100);
        var shipmentBatch = new Batch(
            "shipment-batch", 
            "RETRO-CLOCK", 
            100, 
            Optional.of(LocalDate.now().plusDays(2))
        );

        var orderLine = new OrderLine("order001", "BLUE-CUSHION", 10);

        allocate(orderLine, Arrays.asList(shipmentBatch, inStockBatch));

        assertEquals(90, inStockBatch.getAvailableQuantity());
        assertEquals(100, shipmentBatch.getAvailableQuantity());
    }

    @Test
    void prefersEarlierBatches() {
        var earliest = new Batch(
            "speedy-batch", 
            "MINIMALIST-SPOON", 
            100,
            Optional.of(LocalDate.now())
        );

        var medium = new Batch(
            "normal-batch", 
            "MINIMALIST-SPOON", 
            100,
            Optional.of(LocalDate.now().plusDays(1))
        );

        var latest = new Batch(
            "slow-batch", 
            "MINIMALIST-SPOON", 
            100,
            Optional.of(LocalDate.now().plusDays(2))
        );

        var orderLine = new OrderLine("order-001", "MINIMALIST-SPOON", 10);

        allocate(orderLine, Arrays.asList(latest, medium, earliest));

        assertEquals(90, earliest.getAvailableQuantity());
        assertEquals(100, medium.getAvailableQuantity());
        assertEquals(100, latest.getAvailableQuantity());
    }
}