package app.persistence;

import org.hibernate.Session;

import app.domain.ProductRepository;
import app.services.UnitOfWork;

public class HibernateUnitOfWork implements UnitOfWork {
    private Session session;
    private ProductRepository productRepository;

    public HibernateUnitOfWork() {
        this(new HibernateSessionFactory().create());
    }

    public HibernateUnitOfWork(Session session) {
        this.session = session;
        this.productRepository = new HibernateProductRepository(this.session);

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
    public ProductRepository products() {
        return this.productRepository;
    }
}
