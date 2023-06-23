package app.services;

import java.time.LocalDateTime;
import java.util.Optional;

import app.domain.Product;

public class AddBatchService {
    private UnitOfWork unitOfWork;
    
    public AddBatchService(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public void perform(String reference, String sku, int purchasedQuantity) {
        this.perform(reference, sku, purchasedQuantity, null);
    }

    public void perform(String reference, String sku, int purchasedQuantity, LocalDateTime eta) {
        Product product = this.getProduct(sku);

        product.addBatch(reference, sku, purchasedQuantity, eta);

        this.unitOfWork.commit();
    }

    private Product getProduct(String sku) {
        Optional<Product> product = this.unitOfWork.products().get(sku);

        if (product.isPresent()) {
            return product.get();
        }

        var newProduct = new Product(sku);

        this.unitOfWork.products().add(newProduct);

        return newProduct;
    }
}
