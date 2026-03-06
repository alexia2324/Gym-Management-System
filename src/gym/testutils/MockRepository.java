package gym.testutils;

import gym.domain.Identifiable;
import gym.exceptions.RepositoryException;
import gym.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class MockRepository<ID, E extends Identifiable<ID>> implements Repository<ID, E> {

    private final Map<ID, E> storage = new HashMap<>();

    public boolean throwExceptionOnAdd = false;
    public boolean throwExceptionOnRemove = false;
    public boolean throwExceptionOnUpdate = false;

    public void clearAndLoad(List<E> initialData) {
        storage.clear();
        for (E entity : initialData) {
            storage.put(entity.getId(), entity);
        }
    }

    @Override
    public void add(E entity) {
        if (throwExceptionOnAdd) throw new RepositoryException("Mock forced add exception");
        if (storage.containsKey(entity.getId())) {
            throw new RepositoryException("Duplicate ID");
        }
        storage.put(entity.getId(), entity);
    }

    @Override
    public Optional<E> remove(ID id) {
        if (throwExceptionOnRemove)
            throw new RepositoryException("Mock forced remove exception");
        // dacă nu există, returnează Optional.empty() fără a arunca
        return Optional.ofNullable(storage.remove(id));
    }



    @Override
    public Optional<E> update(E entity) {
        if (throwExceptionOnUpdate) throw new RepositoryException("Mock forced update exception");
        if (!storage.containsKey(entity.getId())) {
            throw new RepositoryException("Entity not found for update"); // important pentru test
        }
        storage.put(entity.getId(), entity);
        return Optional.of(entity);
    }





    @Override
    public Optional<E> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }


    @Override
    public List<E> getAll() {
        if (throwExceptionOnAdd) {
            throw new RepositoryException("Mock forced getAll exception");
        }
        return new ArrayList<>(storage.values());
    }

    @Override
    public Iterator<E> iterator() {
        return storage.values().iterator();
    }
}