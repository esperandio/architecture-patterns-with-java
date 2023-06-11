package app.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AllocationService {
    public static String allocate(OrderLine orderLine, List<Batch> batches) {
        Optional<Batch> batch = batches.stream()
            .sorted((x, y) -> x.getEta().orElse(LocalDateTime.now()).compareTo(y.getEta().orElse(LocalDateTime.now())))
            .filter((x) -> x.canAllocate(orderLine))
            .findFirst();
        
        if (batch.isEmpty()) {
            return "";
        }

        batch.get().allocate(orderLine);

        return batch.get().getReference();
    }
}
