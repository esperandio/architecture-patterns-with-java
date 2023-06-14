package app.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class AllocateServiceTest {
    private InMemoryUnitOfWork unitOfWork;
    private AddBatchService addBatchService;

    public AllocateServiceTest() {
        this.unitOfWork = new InMemoryUnitOfWork();
        this.addBatchService = new AddBatchService(this.unitOfWork);
    }

    @Test
    void allocateReturnsReference() {
        this.addBatchService.perform("speedy-batch", "MINIMALIST-SPOON", 100, LocalDateTime.now());
        this.addBatchService.perform("normal-batch", "MINIMALIST-SPOON", 100, LocalDateTime.now().plusDays(1));
        this.addBatchService.perform("slow-batch", "MINIMALIST-SPOON", 100, LocalDateTime.now().plusDays(2));
        
        var service = new AllocateService(this.unitOfWork);

        var batchReference = service.perform("order001", "MINIMALIST-SPOON", 10);

        assertEquals("speedy-batch", batchReference);
    }

    @Test
    void allocateReturnsEmptyStringWhenUnableToAllocateOrderLine() {
        var service = new AllocateService(this.unitOfWork);

        var batchReference = service.perform("order001", "MINIMALIST-SPOON", 10);

        assertEquals("", batchReference);
    }
}
