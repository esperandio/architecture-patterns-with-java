package app.persistence;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Batch> get(String reference) {
        Batch batch = this.session.get(Batch.class, reference);

        if (batch == null) {
            return Optional.empty();
        }

        return Optional.of(batch);
    }
}
