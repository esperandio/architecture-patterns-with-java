package app.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import app.services.AllocateService;
import app.persistence.HibernateBatchRepository;
import app.persistence.HibernateSessionFactory;

record HealthCheckResponse(boolean ok) { }

record AllocateRequest(String orderId, String sku, int qtd) { }

@RestController
public class AllocateController {
    @PostMapping("allocate")
    public ResponseEntity<String> allocate(AllocateRequest request) {
        var session = new HibernateSessionFactory().create();

        var repository = new HibernateBatchRepository(session);
        var service = new AllocateService(repository);

        session.beginTransaction();

        String batchReference = service.perform(request.orderId(), request.sku(), request.qtd());

        session.getTransaction().commit();

        if (batchReference.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(batchReference, HttpStatus.CREATED);
    }
}