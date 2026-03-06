package gym.repository;

import gym.domain.Session;

import java.time.LocalDateTime;

public class SessionTextFileRepository extends TextFileRepository<Integer, Session> {

    public SessionTextFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected Session readEntity(String line) {
        String[] parts = line.split(","); // separatorul este virgula
        int id = Integer.parseInt(parts[0].trim());
        int clientId = Integer.parseInt(parts[1].trim());
        LocalDateTime dateTime = LocalDateTime.parse(parts[2].trim());
        String description = parts[3].trim();

        return new Session(id, clientId, dateTime, description);
    }

    @Override
    protected String writeEntity(Session s) {
        return s.getId() + "," + s.getClientId() + "," + s.getDateTime() + "," + s.getDescription();
    }
}
