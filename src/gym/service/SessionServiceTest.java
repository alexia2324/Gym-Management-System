package gym.service;

import gym.domain.Session;
import gym.exceptions.RepositoryException;
import gym.exceptions.ValidationException;
import gym.testutils.MockRepository;
import gym.validator.SessionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionServiceTest {
    private SessionService sessionService;
    private MockRepository<Integer, Session> mockRepo;
    private Session s1;

    @BeforeEach
    void setUp() {
        mockRepo = new MockRepository<>();
        SessionValidator validator = new SessionValidator();
        sessionService = new SessionService(mockRepo, validator);

        LocalDateTime future = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        s1 = new Session(1, 101, future, "Upper body training");

        mockRepo.clearAndLoad(Arrays.asList(s1));

        mockRepo.throwExceptionOnAdd = false;
        mockRepo.throwExceptionOnRemove = false;
        mockRepo.throwExceptionOnUpdate = false;
    }

    @Test
    void addSession_Success() {
        LocalDateTime future = LocalDateTime.now().plusDays(2);
        Session s2 = new Session(2, 102, future, "Lower body workout");
        sessionService.addSession(s2);
        assertEquals(2, sessionService.getAllSessions().size());
        assertTrue(sessionService.getSessionById(2).isPresent());
    }

    @Test
    void addSession_ValidationFails_ThrowsValidationException() {
        Session invalid = new Session(3, 103, LocalDateTime.now().minusDays(1), "Past session");
        assertThrows(ValidationException.class, () -> sessionService.addSession(invalid));
        assertEquals(1, sessionService.getAllSessions().size());
    }

    @Test
    void addSession_RepositoryFails_ThrowsRepositoryException() {
        LocalDateTime future = LocalDateTime.now().plusDays(2);
        Session s2 = new Session(2, 102, future, "Lower body workout");
        mockRepo.throwExceptionOnAdd = true;
        assertThrows(RepositoryException.class, () -> sessionService.addSession(s2));
    }

    @Test
    void removeSession_Success() {
        sessionService.removeSession(1);
        assertFalse(sessionService.getSessionById(1).isPresent());
    }

    @Test
    void removeSession_RepositoryFails_ThrowsRepositoryException() {
        mockRepo.throwExceptionOnRemove = true;
        assertThrows(RepositoryException.class, () -> sessionService.removeSession(1));
    }

    @Test
    void updateSession_Success() {
        LocalDateTime future = LocalDateTime.now().plusDays(5);
        Session updated = new Session(1, 101, future, "New description for session 1");
        sessionService.updateSession(updated);
        assertEquals("New description for session 1", sessionService.getSessionById(1).orElseThrow().getDescription());
    }

    @Test
    void updateSession_ValidationFails_ThrowsValidationException() {
        Session invalidUpdate = new Session(1, 101, s1.getDateTime(), "");
        assertThrows(ValidationException.class, () -> sessionService.updateSession(invalidUpdate));
        assertEquals("Upper body training", sessionService.getSessionById(1).orElseThrow().getDescription());
    }

    @Test
    void updateSession_RepositoryFails_ThrowsRepositoryException() {
        LocalDateTime future = LocalDateTime.now().plusDays(5);
        Session updated = new Session(1, 101, future, "New description for session 1");
        mockRepo.throwExceptionOnUpdate = true;
        assertThrows(RepositoryException.class, () -> sessionService.updateSession(updated));
    }

    @Test
    void findById_Success_and_NotFound() {
        assertTrue(sessionService.getSessionById(1).isPresent());
        assertFalse(sessionService.getSessionById(99).isPresent());
    }

    @Test
    void getAllSessions_Success() {
        assertEquals(1, sessionService.getAllSessions().size());
    }

    @Test
    void filterSessions_Success() {
        List<Session> filtered = sessionService.filterSessions(s -> s.getDescription().contains("body"));
        assertEquals(1, filtered.size());
    }

    @Test
    void filterSessions_RepositoryFails_ThrowsRepositoryException() {
        mockRepo.throwExceptionOnAdd = true;
        assertThrows(RepositoryException.class, () -> sessionService.filterSessions(s -> true));
    }
}
