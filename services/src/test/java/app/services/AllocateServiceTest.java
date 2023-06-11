package app.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import app.domain.Batch;

public class AllocateServiceTest {
    @Test
    void allocateReturnsReference() {
        var earliest = new Batch(
            "speedy-batch", 
            "MINIMALIST-SPOON", 
            100,
            Optional.of(LocalDateTime.now())
        );

        var medium = new Batch(
            "normal-batch", 
            "MINIMALIST-SPOON", 
            100,
            Optional.of(LocalDateTime.now().plusDays(1))
        );

        var latest = new Batch(
            "slow-batch", 
            "MINIMALIST-SPOON", 
            100,
            Optional.of(LocalDateTime.now().plusDays(2))
        );
        
        var repository = new InMemoryBatchRepository(Arrays.asList(latest, medium, earliest));
        var service = new AllocateService(repository);

        var batchReference = service.perform("order001", "MINIMALIST-SPOON", 10);

        assertEquals("speedy-batch", batchReference);
    }

    @Test
    void allocateReturnsEmptyStringWhenUnableToAllocateOrderLine() {
        var repository = new InMemoryBatchRepository();
        var service = new AllocateService(repository);

        var batchReference = service.perform("order001", "MINIMALIST-SPOON", 10);

        assertEquals("", batchReference);
    }
}
