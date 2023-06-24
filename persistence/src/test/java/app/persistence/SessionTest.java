package app.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.hibernate.Session;
import app.domain.*;

public class SessionTest {
    private Session session;

    public SessionTest() {
        this.session = new HibernateSessionFactory().create();
    }

    @BeforeEach
    public void initEach() {
        this.session.doWork(connection -> {
            connection.prepareStatement("DELETE FROM Products").executeUpdate();
        });
    }

    @Test
    void canRetrieveProducts() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('BLUE-VASE')").executeUpdate();
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('UNCOMFORTABLE-CHAIR')").executeUpdate();
        });

        this.session.getTransaction().commit();

        List<Product> actual = this.session.createQuery("FROM Product", Product.class).list();
        List<Product> expected = Arrays.asList(
            new Product("BLUE-VASE"),
            new Product("SMALL-TABLE"),
            new Product("UNCOMFORTABLE-CHAIR")
        );

        assertEquals(3, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void canRetrieveSpecificProduct() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-001','SMALL-TABLE', 5)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-002','SMALL-TABLE', 5)").executeUpdate();
        });

        this.session.getTransaction().commit();

        assertEquals(10, this.session.get(Product.class, "SMALL-TABLE").getAvailableQuantity());
    }

    @Test
    void canPersistNewProduct() {
        var newProduct = new Product("BLUE-VASE");

        newProduct.addBatch("batch001", "BLUE-VASE", 10, null);

        newProduct.allocate("order-001", "BLUE-VASE", 2);
        newProduct.allocate("order-002", "BLUE-VASE", 2);

        this.session.beginTransaction();
        this.session.persist(newProduct);
        this.session.getTransaction().commit();

        Product product = this.session.get(Product.class, "BLUE-VASE");

        assertEquals(6, product.getAvailableQuantity());
    }

    @Test
    void canModifyAnExistingProductAndPersist1() {
        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-001','SMALL-TABLE', 5)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-002','SMALL-TABLE', 5)").executeUpdate();
        });

        Product product = this.session.get(Product.class, "SMALL-TABLE");

        product.allocate("order-003", "SMALL-TABLE", 8);

        this.session.beginTransaction();
        this.session.persist(product);
        this.session.getTransaction().commit();

        assertEquals(2, this.session.get(Product.class, "SMALL-TABLE").getAvailableQuantity());
    }

    @Test
    void canModifyAnExistingProductAndPersist2() {
        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-001','SMALL-TABLE', 5)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-002','SMALL-TABLE', 5)").executeUpdate();
        });

        Product product = this.session.get(Product.class, "SMALL-TABLE");

        product.deallocate("order-001", "SMALL-TABLE", 5);

        this.session.beginTransaction();
        this.session.persist(product);
        this.session.getTransaction().commit();

        assertEquals(15, this.session.get(Product.class, "SMALL-TABLE").getAvailableQuantity());
    }

    @Test
    void canModifyAnExistingProductAndPersist3() {
        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-001','SMALL-TABLE', 5)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-002','SMALL-TABLE', 5)").executeUpdate();
        });

        Product product = this.session.get(Product.class, "SMALL-TABLE");

        product.deallocate("order-002", "SMALL-TABLE", 5);
        product.allocate("order-003", "SMALL-TABLE", 8);

        this.session.beginTransaction();
        this.session.persist(product);
        this.session.getTransaction().commit();

        assertEquals(7, this.session.get(Product.class, "SMALL-TABLE").getAvailableQuantity());
    }
}
