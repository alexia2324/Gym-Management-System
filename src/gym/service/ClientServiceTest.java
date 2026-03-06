package gym.service;

import gym.domain.Client;
import gym.exceptions.RepositoryException;
import gym.exceptions.ValidationException;
import gym.testutils.MockRepository;
import gym.validator.ClientValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServiceTest {
    private ClientService clientService;
    private MockRepository<Integer, Client> mockRepo;
    private Client c1;

    @BeforeEach
    void setUp() {
        mockRepo = new MockRepository<>();
        ClientValidator validator = new ClientValidator();
        clientService = new ClientService(mockRepo, validator);

        c1 = new Client(1, "Ana Maria", "ana@yahoo.com", "0740123456");
        mockRepo.clearAndLoad(Arrays.asList(c1));

        mockRepo.throwExceptionOnAdd = false;
        mockRepo.throwExceptionOnRemove = false;
        mockRepo.throwExceptionOnUpdate = false;
    }

    @Test
    void addClient_Success() {
        Client c2 = new Client(2, "Ion Popescu", "ion@gmail.com", "0722123456");
        clientService.addClient(c2);
        assertEquals(2, clientService.getAllClients().size());
        assertTrue(clientService.getClientById(2).isPresent());
    }

    @Test
    void addClient_ValidationFails_ThrowsValidationException() {
        Client invalid = new Client(3, "A", "a@a", "1");
        assertThrows(ValidationException.class, () -> clientService.addClient(invalid));
        assertEquals(1, clientService.getAllClients().size());
    }

    @Test
    void addClient_RepositoryFails_ThrowsRepositoryException() {
        Client c2 = new Client(2, "Test Fail", "fail@e.com", "0711111111");
        mockRepo.throwExceptionOnAdd = true;
        assertThrows(RepositoryException.class, () -> clientService.addClient(c2));
    }

    @Test
    void addClient_DuplicateId_ThrowsRepositoryException() {
        Client cDuplicate = new Client(1, "Alt Client", "alt@email.com", "0700000000");
        assertThrows(RepositoryException.class, () -> clientService.addClient(cDuplicate));
    }

    @Test
    void removeClient_Success() {
        clientService.removeClient(1);
        assertFalse(clientService.getClientById(1).isPresent());
    }

    @Test
    void removeClient_NotFound_DoesNotThrow() {
        assertDoesNotThrow(() -> clientService.removeClient(99));
        assertEquals(1, clientService.getAllClients().size());
    }

    @Test
    void removeClient_RepositoryFails_ThrowsRepositoryException() {
        mockRepo.throwExceptionOnRemove = true;
        assertThrows(RepositoryException.class, () -> clientService.removeClient(1));
    }

    @Test
    void updateClient_Success() {
        Client updated = new Client(1, "Ana Updated", "new@mail.com", "9999999999");
        clientService.updateClient(updated);
        assertEquals("Ana Updated", clientService.getClientById(1).orElseThrow().getName());
    }

    @Test
    void updateClient_ValidationFails_ThrowsValidationException() {
        Client invalidUpdate = new Client(1, "Valid Name", "invalid_email", "0700000000");
        assertThrows(ValidationException.class, () -> clientService.updateClient(invalidUpdate));
        assertEquals("ana@yahoo.com", clientService.getClientById(1).orElseThrow().getEmail());
    }

    @Test
    void updateClient_NotFound_ThrowsRepositoryException() {
        Client nonExistent = new Client(99, "No Exist", "no@e.com", "0000000000");
        assertThrows(RepositoryException.class, () -> clientService.updateClient(nonExistent));
    }

    @Test
    void updateClient_RepositoryFails_ThrowsRepositoryException() {
        Client updated = new Client(1, "Ana Updated", "new@mail.com", "9999999999");
        mockRepo.throwExceptionOnUpdate = true;
        assertThrows(RepositoryException.class, () -> clientService.updateClient(updated));
    }

    @Test
    void findById_Success_and_NotFound() {
        assertTrue(clientService.getClientById(1).isPresent());
        assertFalse(clientService.getClientById(99).isPresent());
    }

    @Test
    void getAllClients_Success() {
        assertEquals(1, clientService.getAllClients().size());
    }

    @Test
    void filterClients_Success() {
        Client c2 = new Client(2, "Ion Popescu", "ion@gmail.com", "0722123456");
        mockRepo.add(c2);

        List<Client> filtered = clientService.filterClients(c -> c.getName().contains("Pop"));
        assertEquals(1, filtered.size());
        assertEquals("Ion Popescu", filtered.get(0).getName());
    }

    @Test
    void filterClients_RepositoryFails_ThrowsRepositoryException() {
        mockRepo.throwExceptionOnAdd = true;
        assertThrows(RepositoryException.class, () -> clientService.filterClients(c -> true));
    }
}
