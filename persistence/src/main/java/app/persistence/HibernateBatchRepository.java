package app.persistence;

import java.util.List;

import org.hibernate.Session;

import app.domain.Batch;
import app.domain.BatchRepository;

public class HibernateBatchRepository implements BatchRepository {
    private Session session;

    public HibernateBatchRepository(Session session) {
        this.session = session;
    }

    @Override
    public List<Batch> list() {
        return this.session.createQuery("FROM Batch", Batch.class).list();
    }
}
