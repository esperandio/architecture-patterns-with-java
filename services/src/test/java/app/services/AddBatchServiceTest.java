package app.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddBatchServiceTest {
    @Test
    void addBatch() {
        var unitOfWork = new InMemoryUnitOfWork();
        var service = new AddBatchService(unitOfWork);

        service.perform("batch-001", "MINIMALIST-SPOON", 10);

        assertEquals(true, unitOfWork.batches().get("batch-001").isPresent());
    }
}
