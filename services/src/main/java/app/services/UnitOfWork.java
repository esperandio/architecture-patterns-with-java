package app.services;

import app.domain.ProductRepository;

public interface UnitOfWork {
    void commit();
    void rollback();
    ProductRepository products();
}
