package app.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.Session;

import app.domain.*;

public class HibernateProductRepositoryTest {
    private Session session;

    public HibernateProductRepositoryTest() {
        this.session = new HibernateSessionFactory().create();
    }

    @BeforeEach
    public void initEach() {
        this.session.doWork(connection -> {
            connection.prepareStatement("DELETE FROM Products").executeUpdate();
        });
    }

    @Test
    void canListProducts() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('BLUE-CUSHION')").executeUpdate();
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('BLUE-VASE')").executeUpdate();
        });

        this.session.getTransaction().commit();

        var repository = new HibernateProductRepository(this.session);

        List<Product> actual = repository.list();
        List<Product> expected = Arrays.asList(
            new Product("BLUE-CUSHION"),
            new Product("BLUE-VASE"),
            new Product("SMALL-TABLE")
        );

        assertEquals(3, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void canRetrieveProductWhenSkuExists() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
        });

        this.session.getTransaction().commit();

        var repository = new HibernateProductRepository(this.session);

        Optional<Product> product = repository.get("SMALL-TABLE");

        assertEquals(true, product.isPresent());
    }

    @Test
    void cannotRetrieveProductWhenSkuIsInvalid() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
        });

        this.session.getTransaction().commit();

        var repository = new HibernateProductRepository(this.session);

        Optional<Product> product = repository.get("invalid-sku");

        assertEquals(false, product.isPresent());
    }

    @Test
    void canPersistNewProduct() {
        var repository = new HibernateProductRepository(this.session);

        this.session.beginTransaction();

        var product = new Product("MINIMALIST-SPOON");

        product.addBatch("speedy-batch", "MINIMALIST-SPOON", 100, LocalDateTime.now());
        product.addBatch("normal-batch", "MINIMALIST-SPOON", 100, LocalDateTime.now().plusDays(1));
        product.addBatch("slow-batch", "MINIMALIST-SPOON", 100, LocalDateTime.now().plusDays(2));

        repository.add(product);

        this.session.getTransaction().commit();

        assertEquals(true, repository.get("MINIMALIST-SPOON").isPresent());
    }
}