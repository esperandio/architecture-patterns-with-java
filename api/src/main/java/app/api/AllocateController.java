package app.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import app.services.AllocateService;
import app.services.UnitOfWork;

record AllocateRequest(String orderId, String sku, int qtd) { }

@RestController
public class AllocateController {
    private final UnitOfWork unitOfWork;

    public AllocateController(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @PostMapping("allocate")
    public ResponseEntity<String> allocate(AllocateRequest request) {
        var service = new AllocateService(this.unitOfWork);

        String batchReference = service.perform(request.orderId(), request.sku(), request.qtd());

        if (batchReference.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(batchReference, HttpStatus.CREATED);
    }
}