package app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.domain.Batch;
import app.domain.BatchRepository;

public class InMemoryBatchRepository implements BatchRepository {
    private List<Batch> batches;

    public InMemoryBatchRepository() {
        this(new ArrayList<Batch>());
    }

    public InMemoryBatchRepository(List<Batch> batches) {
        this.batches = batches;
    }

    @Override
    public List<Batch> list() {
        return batches;
    }

    @Override
    public Optional<Batch> get(String reference) {
        return batches.stream()
            .filter(x -> x.getReference() == reference)
            .findFirst();
    }    
}
