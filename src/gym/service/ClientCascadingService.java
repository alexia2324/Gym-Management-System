package gym.service;

import gym.domain.Client;
import gym.domain.Session;
import gym.filter.AbstractFilter;
import gym.undo.ActionComposite;
import gym.undo.ActionRemove;
import gym.undo.ActionUpdate;
import gym.undo.UndoService;

import java.util.List;
import java.util.Optional;

public class ClientCascadingService {
    private ClientService clientService;
    private SessionService sessionService;
    private UndoService undoService;


    public ClientCascadingService(ClientService clientService, SessionService sessionService,UndoService undoService) {
        this.clientService = clientService;
        this.sessionService = sessionService;
        this.undoService = undoService;
    }

    // Delete client and all their sessions
    public boolean removeClientCascading(int clientId) {

        ActionComposite composite = new ActionComposite();

        // 1. ștergem sesiunile
        sessionService.reportSessionsForClient(clientId)
                .forEach(s -> {
                    sessionService.removeSession(s.getId());
                    composite.addAction(
                            new ActionRemove<>(sessionService.getRepo(), s)
                    );
                });

        // 2. ștergem clientul
        Client removedClient =
                clientService.getClientById(clientId).orElseThrow();

        boolean result = clientService.removeClient(clientId);

        composite.addAction(
                new ActionRemove<>(clientService.getRepo(), removedClient)
        );
        undoService.recordAction(composite);

        return result;
    }



    // Other CRUD operations can delegate to clientService
    public void addClient(Client c) {
        clientService.addClient(c);
    }

    public Optional<Client> getClientById(int id) {
        // Modificat pentru a returna Optional<Client>
        return clientService.getClientById(id);
    }

    // Metoda removeClient nu este necesară aici, deoarece se folosește removeClientCascading,
    // dar o menținem pe cea de delegare (care este esențial identică cu removeClient din ClientService)
    public boolean removeClient(int id) {
        return clientService.removeClient(id);
    }

    public boolean updateClient(Client c) {
        // preluăm clientul vechi
        Client oldClient = clientService.getClientById(c.getId())
                .orElseThrow(() -> new RuntimeException("Clientul nu există"));

        boolean result = clientService.updateClient(c);

        // Înregistrăm acțiunea de update
        undoService.recordAction(new ActionUpdate<>(clientService.getRepo(), oldClient, c));

        return result;
    }


    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    // Adăugată metoda filterClients pentru a se alinia cu ClientService
    public List<Client> filterClients(AbstractFilter<Client> filter) {
        return clientService.filterClients(filter);
    }
    public void undo() {
        undoService.undo();
    }

    public void redo() {
        undoService.redo();
    }

}
