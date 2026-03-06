package gym.service;

import gym.domain.Client;
import gym.filter.AbstractFilter;
import gym.repository.Repository;
import gym.validator.Validator;
import java.util.List;
import java.util.Optional;

public class ClientService {
    private Repository<Integer, Client> repo;
    private Validator<Client> validator;

    public ClientService(Repository<Integer, Client> repo, Validator<Client> validator) {
        this.repo = repo;
        this.validator = validator;

    }

    public void addClient(Client c) {
        validator.validate(c);
        repo.add(c);
    }

    public Optional<Client> getClientById(int id) {
        return repo.findById(id);
    }

    public boolean removeClient(int id) {
        return repo.remove(id).isPresent();
    }

    public boolean updateClient(Client c) {
        validator.validate(c);
        return repo.update(c).isPresent();
    }

    public List<Client> getAllClients() {
        return repo.getAll();
    }


    public List<Client> filterClients(AbstractFilter<Client> filter) {
        return repo.getAll().stream().filter(filter::test).toList();
    }

    public Repository<Integer, Client> getRepo() {
        return repo;
    }
}
