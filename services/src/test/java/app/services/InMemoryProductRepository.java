package app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.domain.Product;
import app.domain.ProductRepository;

public class InMemoryProductRepository implements ProductRepository {
    private List<Product> products;

    public InMemoryProductRepository() {
        this(new ArrayList<Product>());
    }

    public InMemoryProductRepository(List<Product> products) {
        this.products = products;
    }

    @Override
    public List<Product> list() {
        return this.products;
    }

    @Override
    public Optional<Product> get(String sku) {
        return this.products.stream()
            .filter(x -> x.getSku() == sku)
            .findFirst();
    }

    @Override
    public void add(Product product) {
        this.products.add(product);
    }
    
}
