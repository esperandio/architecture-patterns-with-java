package app.api;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import app.services.AddBatchService;
import app.persistence.HibernateUnitOfWork;

record AddBatchRequest(String reference, String sku, int purchasedQuantity, LocalDateTime eta) { }

@RestController
public class AddBatchController {
    @PostMapping("add_batch")
    public ResponseEntity<String> addBatch(AddBatchRequest request) {
        var unitOfWork = new HibernateUnitOfWork();
        var service = new AddBatchService(unitOfWork);

        service.perform(request.reference(), request.sku(), request.purchasedQuantity(), request.eta());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}