package app.services;

import java.util.Optional;

import app.domain.*;

public class AllocateService {
    private final UnitOfWork unitOfWork;

    public AllocateService(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public String perform(String orderId, String sku, int quantity) {
        Optional<Product> product = this.unitOfWork.products().get(sku);

        if (product.isEmpty()) {
            return "";
        }

        String batchReference = product.get().allocate(orderId, sku, quantity);

        this.unitOfWork.commit();

        return batchReference;
    }
}
