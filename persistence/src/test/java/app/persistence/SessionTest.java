package app.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

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
}
