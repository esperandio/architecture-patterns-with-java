package app.persistence;

import org.hibernate.Session;

import app.domain.BatchRepository;
import app.services.UnitOfWork;

public class HibernateUnitOfWork implements UnitOfWork {
    private Session session;
    private BatchRepository batchRepository;

    public HibernateUnitOfWork() {
        this.session = new HibernateSessionFactory().create();
        this.batchRepository = new HibernateBatchRepository(this.session);

        this.session.beginTransaction();
    }

    @Override
    public void commit() {
        this.session.getTransaction().commit();
    }

    @Override
    public void rollback() {
        this.session.getTransaction().rollback();
    }

    @Override
    public BatchRepository batches() {
        return this.batchRepository;
    }
}
