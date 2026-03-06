package gym.repository;

import gym.domain.Identifiable;
import java.util.*;
import java.util.Iterator;
import gym.exceptions.RepositoryException;

public class InMemoryRepository<ID, E extends Identifiable<ID>> implements Repository<ID, E>, Iterable<E> {
    protected Map<ID, E> storage = new HashMap<>();

    @Override
    public void add(E entity) {
        if (storage.containsKey(entity.getId()))
            throw new RepositoryException("Entity with ID " + entity.getId() + " already exists!");
        storage.put(entity.getId(), entity);
    }

    @Override
    public Optional<E> remove(ID id) {
        E removed = storage.remove(id);
        if (removed == null)
            throw new RepositoryException("Cannot remove: entity with ID " + id + " not found!");
        return Optional.of(removed);
    }

    @Override
    public Optional<E> update(E entity) {
        E updated = storage.replace(entity.getId(), entity);
        if (updated == null)
            throw new RepositoryException("Cannot update: entity with ID " + entity.getId() + " does not exist!");
        return Optional.of(updated);
    }

    @Override
    public Optional<E> findById(ID id) {
        E found = storage.get(id);
        if (found == null)
            throw new RepositoryException("Entity with ID " + id + " not found!");
        return Optional.of(found);
    }

    @Override
    public List<E> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Iterator<E> iterator() {
        return storage.values().iterator();
    }

}
