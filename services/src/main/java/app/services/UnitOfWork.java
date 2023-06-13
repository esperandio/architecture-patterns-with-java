package app.services;

import app.domain.BatchRepository;

public interface UnitOfWork {
    void commit();
    void rollback();
    BatchRepository batches();
}
