package gym.repository;

import gym.domain.Identifiable;
import gym.exceptions.RepositoryException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TextFileRepository<ID, E extends Identifiable<ID>> extends InMemoryRepository<ID, E> {
    protected String fileName;

    public TextFileRepository(String fileName) {
        this.fileName = fileName;
        loadFromFile();
    }

    protected abstract E readEntity(String line);
    protected abstract String writeEntity(E entity);

    private void loadFromFile() {
        File file = new File(fileName);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // ignoră liniile goale
                E entity = readEntity(line);
                storage.put(entity.getId(), entity);
            }
        } catch (IOException e) {
            throw new RepositoryException("Error reading file: " + fileName, e);
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (E entity : storage.values()) {
                writer.write(writeEntity(entity));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Error writing to file: " + fileName, e);
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


    @Override
    public List<E> getAll() {
        return new ArrayList<>(storage.values());
    }
}
