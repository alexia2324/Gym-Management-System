package gym.service;

import gym.domain.Client;
import gym.domain.Session;
import gym.filter.AbstractFilter;
import gym.repository.Repository;
import gym.validator.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SessionService {
    private Repository<Integer, Session> repo;
    private Validator<Session> validator;

    public SessionService(Repository<Integer, Session> repo, Validator<Session> validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public void addSession(Session s) {
        validator.validate(s);
        repo.add(s);
    }

    public Optional<Session> getSessionById(int id) {
        return repo.findById(id);
    }

    public boolean removeSession(int id) {
        return repo.remove(id).isPresent();
    }

    public boolean updateSession(Session s) {
        validator.validate(s);
        return repo.update(s).isPresent();
    }

    public List<Session> getAllSessions() {
        return repo.getAll();
    }


    public List<Session> filterSessions(AbstractFilter<Session> filter) {
        return repo.getAll().stream().filter(filter::test).toList();
    }
    // 1. Toate sesiunile unui client
    public List<Session> reportSessionsForClient(int clientId) {
        return repo.getAll().stream()
                .filter(s -> s.getClientId() == clientId)
                .toList();
    }

    // 2. Telefon client prin ClientService
    public Optional<String> reportPhoneByClientId(int id, ClientService clientService) {
        return clientService.getClientById(id).map(Client::getPhone);
    }

    // 3. Sesiuni între două date
    public List<Session> reportSessionsBetween( LocalDateTime start, LocalDateTime end) {
        return repo.getAll().stream()
                // CORECȚIE: Sesiunea trebuie să fie DUPĂ start ȘI ÎNAINTE de end
                .filter(s -> s.getDateTime().isAfter(start) && s.getDateTime().isBefore(end))
                .toList();
    }

    // 4. Sesiuni cu un keyword în descriere
    public List<Session> reportSessionsWithKeyword(int clientId, String keyword) {
        return repo.getAll().stream()
                .filter(s -> s.getClientId() == clientId)
                .filter(s -> s.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // 5. Clienți după domeniu email
    public List<Client> reportClientsByEmailDomain(String domain, ClientService clientService) {
        return clientService.getAllClients().stream()
                .filter(c -> c.getEmail().endsWith(domain))
                .toList();
    }

    public Repository<Integer, Session> getRepo() {
        return repo;
    }
}
