package app.services;

import java.time.LocalDateTime;

import app.domain.Batch;

public class AddBatchService {
    private UnitOfWork unitOfWork;
    
    public AddBatchService(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public void perform(String reference, String sku, int purchasedQuantity) {
        this.perform(reference, sku, purchasedQuantity, null);
    }

    public void perform(String reference, String sku, int purchasedQuantity, LocalDateTime eta) {
        this.unitOfWork.batches().add(new Batch(reference, sku, purchasedQuantity, eta));
        this.unitOfWork.commit();
    }
}
