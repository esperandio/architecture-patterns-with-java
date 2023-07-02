package app.api;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import app.services.AddBatchService;
import app.services.UnitOfWork;

record AddBatchRequest(String reference, String sku, int purchasedQuantity, LocalDateTime eta) { }

@RestController
public class AddBatchController {
    private final UnitOfWork unitOfWork;

    public AddBatchController(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @PostMapping("add_batch")
    public ResponseEntity<String> addBatch(AddBatchRequest request) {
        var service = new AddBatchService(this.unitOfWork);

        service.perform(request.reference(), request.sku(), request.purchasedQuantity(), request.eta());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}