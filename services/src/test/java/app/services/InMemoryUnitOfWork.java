package app.services;

import app.domain.Product;

import java.util.Arrays;

import app.domain.ProductRepository;

public class InMemoryUnitOfWork implements UnitOfWork {
    private ProductRepository productRepository;
    private boolean committed = false;

    public InMemoryUnitOfWork() {
        var defaultProducts = Arrays.asList(
            new Product("MINIMALIST-SPOON"),
            new Product("SMALL-TABLE")
        );

        this.productRepository = new InMemoryProductRepository(defaultProducts);
    }

    public boolean isCommitted() {
        return this.committed;
    }

    @Override
    public void commit() {
        this.committed = true;
    }

    @Override
    public void rollback() {
        this.committed = false;
    }

    @Override
    public ProductRepository products() {
        return this.productRepository;
    }
    
}
