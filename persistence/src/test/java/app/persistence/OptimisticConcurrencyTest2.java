package app.persistence;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import app.domain.Product;
import jakarta.persistence.OptimisticLockException;

@Disabled
public class OptimisticConcurrencyTest2 {
    private Session session;
    private HibernateUnitOfWork unitOfWork;
    
    public OptimisticConcurrencyTest2() {
        this.session = new HibernateSessionFactory().create();
        this.unitOfWork = new HibernateUnitOfWork(this.session);
    }

    @BeforeEach
    public void initEach() {
        this.session.doWork(connection -> {
            connection.prepareStatement("INSERT IGNORE INTO Products (Sku) VALUES ('CONCURRENCY-TEST')").executeUpdate();
            connection.prepareStatement("INSERT IGNORE INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'CONCURRENCY-TEST', 100)").executeUpdate();
            connection.prepareStatement("DELETE FROM OrderLines WHERE OrderId = 'order-002'").executeUpdate();
        });
    }

    @Test
    public void testTransaction() throws InterruptedException {
        Optional<Product> product = this.unitOfWork.products().get("CONCURRENCY-TEST");

        product.get().allocate("order-002", "CONCURRENCY-TEST", 10);

        Thread.sleep(8000);

        assertThrows(OptimisticLockException.class, () -> {
            this.unitOfWork.commit();
        });
    }
}
