package app.services;

import java.util.List;

import app.domain.*;
import static app.domain.AllocationService.allocate;

public class AllocateService {
    private UnitOfWork unitOfWork;

    public AllocateService(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public String perform(String orderId, String sku, int quantity) {
        List<Batch> batches = this.unitOfWork.batches().list();

        var orderLine = new OrderLine(orderId, sku, quantity);

        String batchReference = allocate(orderLine, batches);

        this.unitOfWork.commit();

        return batchReference;
    }
}
