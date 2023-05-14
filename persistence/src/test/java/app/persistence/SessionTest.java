package app.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import app.domain.*;

public class SessionTest {
    private Session session;

    public SessionTest() {
        var configuration = new Configuration();

        configuration.configure("hibernate.cfg.xml");

        // Create Session Factory
        SessionFactory sessionFactory = configuration.buildSessionFactory();
 
        // Initialize Session Object
        this.session = sessionFactory.openSession();
    }

    @BeforeEach
    public void initEach() {
        this.session.doWork(connection -> {
            connection.prepareStatement("DELETE FROM Batches").executeUpdate();
        });
    }

    @Test
    void testCanRetrieveBatches() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
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
    void TestCanRetrieveSpecificBatch() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-001','SMALL-TABLE', 5)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-002','SMALL-TABLE', 5)").executeUpdate();
        });

        this.session.getTransaction().commit();

        Optional<Batch> batch = this.session.createQuery("FROM Batch", Batch.class)
            .list()
            .stream()
            .findFirst();

        assertTrue(batch.isPresent());
        assertEquals(10, batch.get().getAllocatedQuantity());
    }

    @Test
    void testCanPersistNewBatch() {
        var batch = new Batch("batch001", "BLUE-VASE", 10);

        batch.allocate(new OrderLine("order-001", "BLUE-VASE", 2));
        batch.allocate(new OrderLine("order-002", "BLUE-VASE", 2));

        this.session.beginTransaction();
        this.session.persist(batch);
        this.session.getTransaction().commit();

        List<Batch> batches = this.session.createQuery("FROM Batch", Batch.class).list();

        assertEquals(1, batches.size());
        assertEquals(4, batches.get(0).getAllocatedQuantity());
    }

    @Test
    void testCanModifyAnExistingBatchAndPersist1() {
        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-001','SMALL-TABLE', 5)").executeUpdate();
            connection.prepareStatement("INSERT INTO OrderLines (BatchReference, OrderId, Sku, Quantity) VALUES ('batch-001', 'order-002','SMALL-TABLE', 5)").executeUpdate();
        });

        var batch = this.session.get(Batch.class, "batch-001");

        batch.allocate(new OrderLine("order-003", "SMALL-TABLE", 8));

        this.session.beginTransaction();
        this.session.persist(batch);
        this.session.getTransaction().commit();

        assertEquals(18, this.session.get(Batch.class, "batch-001").getAllocatedQuantity());
    }
}
