package app.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDateTime;
import java.util.*;

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
    void allocatedEventIsRecordedWhenOrderLineIsAllocated() {
        var batch = new Batch("batch001", "RETRO-CLOCK", 20);
        var product = new Product("RETRO-CLOCK", Arrays.asList(batch));

        product.allocate("order001", "RETRO-CLOCK", 2);

        assertEquals(1, product.getDomainEvents().size());
        assertInstanceOf(AllocatedEvent.class, product.getDomainEvents().getElementAt(0));
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
    void cannotAllocateIfSkuDoNotMatch() {
        var batch = new Batch("batch001", "UNCOMFORTABLE-CHAIR", 100);
        var orderLine = new OrderLine("order001", "EXPENSIVE-TOASTER", 2);

        batch.allocate(orderLine);

        assertEquals(100, batch.getAvailableQuantity());
        assertEquals(0, batch.getAllocatedQuantity());
    }

    @Test
    void prefersCurrentStockBatchesToShipment() {
        var inStockBatch = new Batch("in-stock-batch", "RETRO-CLOCK", 100);
        var shipmentBatch = new Batch(
            "shipment-batch", 
            "RETRO-CLOCK", 
            100, 
            LocalDateTime.now().plusDays(2)
        );

        var product = new Product("RETRO-CLOCK", Arrays.asList(shipmentBatch, inStockBatch));

        var batchReference = product.allocate("order001", "RETRO-CLOCK", 10);

        assertEquals("in-stock-batch", batchReference);
        assertEquals(90, inStockBatch.getAvailableQuantity());
        assertEquals(100, shipmentBatch.getAvailableQuantity());
    }

    @Test
    void prefersEarlierBatches() {
        var earliest = new Batch(
            "speedy-batch", 
            "MINIMALIST-SPOON", 
            100,
            LocalDateTime.now()
        );

        var medium = new Batch(
            "normal-batch", 
            "MINIMALIST-SPOON", 
            100,
            LocalDateTime.now().plusDays(1)
        );

        var latest = new Batch(
            "slow-batch", 
            "MINIMALIST-SPOON", 
            100,
            LocalDateTime.now().plusDays(2)
        );

        var product = new Product("MINIMALIST-SPOON", Arrays.asList(latest, medium, earliest));

        var batchReference = product.allocate("order-001", "MINIMALIST-SPOON", 10);

        assertEquals("speedy-batch", batchReference);
        assertEquals(90, earliest.getAvailableQuantity());
        assertEquals(100, medium.getAvailableQuantity());
        assertEquals(100, latest.getAvailableQuantity());
    }

    @Test
    void availableQuantityIsIncreasedWhenOrderLineIsDeallocated() {
        var product = new Product("SMALL-TABLE");

        product.addBatch("batch-001", "SMALL-TABLE", 20, LocalDateTime.now());
        product.allocate("order-001", "SMALL-TABLE", 2);

        assertEquals(18, product.getAvailableQuantity());

        product.deallocate("order-001", "SMALL-TABLE", 2);

        assertEquals(20, product.getAvailableQuantity());
    }
}
