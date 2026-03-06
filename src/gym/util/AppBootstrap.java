package gym.util;

import gym.domain.Client;
import gym.domain.Session;
import gym.repository.*;
import gym.service.*;
import gym.validator.*;

import java.io.FileInputStream;
import java.util.Properties;

public class AppBootstrap {

    public ClientService clientService;
    public SessionService sessionService;

    public AppBootstrap() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("src/settings.properties"));

            String repoType = props.getProperty("Repository").trim();
            String clientConfig = props.getProperty("Clients").replace("\"", "").trim();
            String sessionConfig = props.getProperty("Sessions").replace("\"", "").trim();
            String location = props.getProperty("Location").trim();
            String dbUser = props.getProperty("DB_User").trim();
            String dbPass = props.getProperty("DB_Password").trim();

            Repository<Integer, Client> clientRepo;
            Repository<Integer, Session> sessionRepo;

            switch (repoType.toLowerCase()) {
                case "binary" -> {
                    clientRepo = new BinaryFileRepository<>(clientConfig);
                    sessionRepo = new BinaryFileRepository<>(sessionConfig);
                }
                case "text" -> {
                    clientRepo = new ClientTextFileRepository(clientConfig);
                    sessionRepo = new SessionTextFileRepository(sessionConfig);
                }
                case "database" -> {
                    DBInitializer.init(location);
                    clientRepo = new ClientDBRepository(location, dbUser, dbPass);
                    sessionRepo = new SessionDBRepository(location, dbUser, dbPass);
                }
                default -> throw new RuntimeException("Invalid repo type");
            }

            ClientValidator cv = new ClientValidator();
            SessionValidator sv = new SessionValidator();

            clientService = new ClientService(clientRepo, cv);
            sessionService = new SessionService(sessionRepo, sv);

        } catch (Exception e) {
            throw new RuntimeException("Bootstrap failed: " + e.getMessage());
        }
    }
}
