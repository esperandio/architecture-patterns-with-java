package app.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> list();
    Optional<Product> get(String sku);
    void add(Product product);
}
