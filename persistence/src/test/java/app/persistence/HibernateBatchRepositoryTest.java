package app.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.hibernate.Session;

import app.domain.*;

public class HibernateBatchRepositoryTest {
    private Session session;

    public HibernateBatchRepositoryTest() { 
        // Initialize Session Object
        this.session = new HibernateSessionFactory().create();
    }

    @BeforeEach
    public void initEach() {
        this.session.doWork(connection -> {
            connection.prepareStatement("DELETE FROM Batches").executeUpdate();
        });
    }

    @Test
    void canListBatches() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-002', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-003', 'SMALL-TABLE', 20)").executeUpdate();
        });

        this.session.getTransaction().commit();

        var repository = new HibernateBatchRepository(this.session);

        List<Batch> actual = repository.list();
        List<Batch> expected = Arrays.asList(
            new Batch("batch-001", "SMALL-TABLE", 20),
            new Batch("batch-002", "SMALL-TABLE", 20),
            new Batch("batch-003", "SMALL-TABLE", 20)
        );

        assertEquals(3, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void canRetrieveBatchWhenReferenceExists() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
        });

        this.session.getTransaction().commit();

        var repository = new HibernateBatchRepository(this.session);

        Optional<Batch> batch = repository.get("batch-001");

        assertEquals(true, batch.isPresent());
    }

    @Test
    void cannotRetrieveBatchWhenReferenceIsInvalid() {
        this.session.beginTransaction();

        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
        });

        this.session.getTransaction().commit();

        var repository = new HibernateBatchRepository(this.session);

        Optional<Batch> batch = repository.get("invalid-reference");

        assertEquals(false, batch.isPresent());
    }
}
