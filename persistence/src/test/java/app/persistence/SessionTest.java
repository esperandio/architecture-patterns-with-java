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
    void canRetrieveBatches() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-002', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-003', 'SMALL-TABLE', 20)").executeUpdate();
        });

        this.session.getTransaction().commit();

        List<Batch> actual = this.session.createQuery("FROM Batch", Batch.class).list();
        List<Batch> expected = Arrays.asList(
            new Batch("batch-001", "SMALL-TABLE", 20),
            new Batch("batch-002", "SMALL-TABLE", 20),
            new Batch("batch-003", "SMALL-TABLE", 20)
        );

        assertEquals(3, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void canRetrieveSpecificBatch() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Products (Sku) VALUES ('SMALL-TABLE')").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-001','SMALL-TABLE', 5)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-002','SMALL-TABLE', 5)").executeUpdate();
        });

        this.session.getTransaction().commit();

        assertEquals(10, this.session.get(Batch.class, "batch-001").getAllocatedQuantity());
    }

    @Test
    void canPersistNewBatch() {
        var product = new Product("BLUE-VASE");

        product.addBatch("batch001", "BLUE-VASE", 10, null);

        product.allocate("order-001", "BLUE-VASE", 2);
        product.allocate("order-002", "BLUE-VASE", 2);

        this.session.beginTransaction();
        this.session.persist(product);
        this.session.getTransaction().commit();

        List<Batch> batches = this.session.createQuery("FROM Batch", Batch.class).list();

        assertEquals(1, batches.size());
        assertEquals(4, batches.get(0).getAllocatedQuantity());
    }

    @Test
    void canModifyAnExistingBatchAndPersist1() {
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

        assertEquals(18, this.session.get(Batch.class, "batch-001").getAllocatedQuantity());
    }

    @Test
    void canModifyAnExistingBatchAndPersist2() {
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

        assertEquals(5, this.session.get(Batch.class, "batch-001").getAllocatedQuantity());
    }

    @Test
    void canModifyAnExistingBatchAndPersist3() {
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
