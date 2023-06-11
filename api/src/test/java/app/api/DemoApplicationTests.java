package app.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import app.persistence.HibernateSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private AllocateController controller;

    @Test
    void healthcheck() {
        assertThat(controller.healthcheck()).asString().contains("ok");
    }

    @Test
    void happyPathReturns201AndAllocatedBatch() {
        var session = new HibernateSessionFactory().create();

        session.beginTransaction();

        session.doWork(connection -> {
            connection.prepareStatement("DELETE FROM Batches").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity) VALUES ('batch-001', 'SMALL-TABLE', 20)").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity, Eta) VALUES ('batch-002', 'SMALL-TABLE', 20, '2023-07-11 11:13:14')").executeUpdate();
            connection.prepareStatement("INSERT INTO Batches (Reference, Sku, PurchasedQuantity, Eta) VALUES ('batch-003', 'SMALL-TABLE', 20, '2023-09-11 12:13:14')").executeUpdate();
        });

        session.getTransaction().commit();

        var response = controller.allocate(new Allocate("order-005", "SMALL-TABLE", 1));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("batch-001");
    }
}
