package app.domain;

import java.util.List;
import java.util.Optional;

public interface BatchRepository {
    List<Batch> list();
    Optional<Batch> get(String reference);
    void add(Batch batch);
}
