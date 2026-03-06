package gym.repository;

import gym.domain.Identifiable;
import gym.exceptions.RepositoryException;

import java.io.*;
import java.util.Map;
import java.util.Optional;

public class BinaryFileRepository<ID, E extends Identifiable<ID> & Serializable> extends InMemoryRepository<ID, E> {
    private String fileName;

    public BinaryFileRepository(String fileName) {
        this.fileName = fileName;
        loadFromFile();
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(fileName);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            storage = (Map<ID, E>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException("Error reading binary file: " + fileName, e);
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            throw new RepositoryException("Error writing binary file: " + fileName, e);
        }
    }

    @Override
    public void add(E entity) {
        super.add(entity);
        saveToFile();
    }

    @Override
    public Optional<E> remove(ID id) {
        Optional<E> result = super.remove(id);
        saveToFile();
        return result;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> result = super.update(entity);
        saveToFile();
        return result;
    }

}
