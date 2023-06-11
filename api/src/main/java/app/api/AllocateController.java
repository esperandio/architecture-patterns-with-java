package app.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import app.services.AllocateService;
import app.persistence.HibernateBatchRepository;
import app.persistence.HibernateSessionFactory;

record HealthCheck(boolean ok) { }
record Allocate(String orderId, String sku, int qtd) { }

@RestController
public class AllocateController {
    @GetMapping("/healthcheck")
    public HealthCheck healthcheck() {
        return new HealthCheck(true);
    }

    @PostMapping("allocate")
    public ResponseEntity<String> allocate(@RequestBody Allocate allocate) {
        var session = new HibernateSessionFactory().create();

        var repository = new HibernateBatchRepository(session);
        var service = new AllocateService(repository);

        session.beginTransaction();

        String batchReference = service.perform(allocate.orderId(), allocate.sku(), allocate.qtd());

        session.getTransaction().commit();

        if (batchReference.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(batchReference, HttpStatus.CREATED);
    }
}