package app.services;

import java.util.List;

import app.domain.*;
import static app.domain.AllocationService.allocate;

public class AllocateService {
    private BatchRepository repository;

    public AllocateService(BatchRepository repository) {
        this.repository = repository;
    }

    public String perform(String orderId, String sku, int quantity) {
        List<Batch> batches = this.repository.list();

        var orderLine = new OrderLine(orderId, sku, quantity);

        String batchReference = allocate(orderLine, batches);

        return batchReference;
    }
}
