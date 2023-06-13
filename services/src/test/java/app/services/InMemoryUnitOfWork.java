package app.services;

import app.domain.BatchRepository;

public class InMemoryUnitOfWork implements UnitOfWork {
    private BatchRepository batchRepository;

    public InMemoryUnitOfWork() {
        this.batchRepository = new InMemoryBatchRepository();
    }    

    @Override
    public void commit() {}

    @Override
    public void rollback() {}

    @Override
    public BatchRepository batches() {
        return this.batchRepository;
    }
    
}
