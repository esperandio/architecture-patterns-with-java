package app.persistence;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;

import app.domain.Product;
import app.domain.ProductRepository;

public class HibernateProductRepository implements ProductRepository {
    private final Session session;

    public HibernateProductRepository(Session session) {
        this.session = session;
    }

    @Override
    public List<Product> list() {
        return this.session.createQuery("FROM Product", Product.class).list();
    }

    @Override
    public Optional<Product> get(String sku) {
        Product product = this.session.get(Product.class, sku);

        if (product == null) {
            return Optional.empty();
        }

        return Optional.of(product);
    }

    @Override
    public void add(Product product) {
        this.session.persist(product);
    }
    
}
