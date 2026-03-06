package gym.repository;

import gym.domain.Identifiable;
import java.util.List;
import java.util.Iterator;
import java.util.Optional;

public interface Repository<ID, E extends Identifiable<ID>>extends Iterable<E> {
    void add(E entity);
    Optional<E> remove(ID id);
    Optional<E> update(E entity);
    Optional<E> findById(ID id);
    List<E> getAll();
}
