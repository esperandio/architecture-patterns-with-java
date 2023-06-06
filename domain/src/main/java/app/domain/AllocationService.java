package app.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AllocationService {
    public static String allocate(OrderLine orderLine, List<Batch> batches) {
        Optional<Batch> batch = batches.stream()
            .sorted((x, y) -> x.getEta().orElse(LocalDate.now()).compareTo(y.getEta().orElse(LocalDate.now())))
            .filter((x) -> x.canAllocate(orderLine))
            .findFirst();
        
        if (batch.isEmpty()) {
            return "";
        }

        batch.get().allocate(orderLine);

        return batch.get().getReference();
    }
}
