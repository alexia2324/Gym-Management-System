package gym.ui;

import gym.domain.Client;
import gym.domain.Session;
import gym.exceptions.RepositoryException;
import gym.exceptions.ValidationException;
import gym.filter.ClientEmailFilter;
import gym.filter.ClientNameFilter;
import gym.filter.SessionDateFilter;
import gym.filter.SessionDescriptionFilter;
import gym.repository.BinaryFileRepository;
import gym.repository.ClientDBRepository;
import gym.repository.ClientTextFileRepository;
import gym.repository.Repository;
import gym.repository.SessionDBRepository;
import gym.repository.SessionTextFileRepository;
import gym.service.ClientService;
import gym.service.SessionService;
import gym.util.DBInitializer;
import gym.validator.ClientValidator;
import gym.validator.SessionValidator;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Scanner;

public class ConsoleUI {
    // Acum folosim direct ClientService în loc de ClientCascadingService
    private ClientService clientService;
    private SessionService sessionService;

    private final Scanner scanner = new Scanner(System.in);

    // Constructor pentru Dependency Injection
    public ConsoleUI(ClientService clientService, SessionService sessionService) {
        this.clientService = clientService;
        this.sessionService = sessionService;
    }

    public ConsoleUI() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/settings.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println(" Eroare la citirea settings.properties: " + e.getMessage());
            return;
        }

        String repoType = props.getProperty("Repository").trim();
        String location = props.getProperty("Location", "").trim();
        String dbUser = props.getProperty("DB_User", "").trim();
        String dbPass = props.getProperty("DB_Password", "").trim();

        String clientConfig = props.getProperty("Clients").replace("\"", "").trim();
        String sessionConfig = props.getProperty("Sessions").replace("\"", "").trim();

        Repository<Integer, Client> clientRepo;
        Repository<Integer, Session> sessionRepo;

        try {
            if (repoType.equalsIgnoreCase("binary")) {
                clientRepo = new BinaryFileRepository<>(clientConfig);
                sessionRepo = new BinaryFileRepository<>(sessionConfig);
                System.out.println(" Repository: Fișiere Binare");
            } else if (repoType.equalsIgnoreCase("text")) {
                clientRepo = new ClientTextFileRepository(clientConfig);
                sessionRepo = new SessionTextFileRepository(sessionConfig);
                System.out.println(" Repository: Fișiere Text");
            } else if (repoType.equalsIgnoreCase("database")) {
                DBInitializer.init(location);
                clientRepo = new ClientDBRepository(location, dbUser, dbPass);
                sessionRepo = new SessionDBRepository(location, dbUser, dbPass);
                System.out.println(" Repository: Bază de Date (" + location + ")");
            } else {
                throw new RuntimeException("Tip de repository necunoscut: " + repoType);
            }
        } catch (RuntimeException e) {
            System.err.println(" Eroare la inițializarea repository-ului: " + e.getMessage());
            return;
        }

        // Inițializare servicii
        this.clientService = new ClientService(clientRepo, new ClientValidator());
        this.sessionService = new SessionService(sessionRepo, new SessionValidator());

        // Date de test
        incarcaDateTest();
    }

    private void incarcaDateTest() {
        try {
            if (this.clientService.getAllClients().isEmpty()) {
                this.clientService.addClient(new Client(1, "Alex Popescu", "alex.p@email.com", "0720111222"));
                this.clientService.addClient(new Client(2, "Maria Ionescu", "maria.i@email.com", "0720333444"));
                this.clientService.addClient(new Client(3, "Ioana Vasile", "ioana.v@email.com", "0720555666"));
            }
            if (this.sessionService.getAllSessions().isEmpty()) {
                this.sessionService.addSession(new Session(101, 1, LocalDateTime.of(2025, 11, 5, 18, 0), "Antrenament picioare"));
                this.sessionService.addSession(new Session(102, 2, LocalDateTime.of(2025, 11, 6, 10, 0), "Clasa de Yoga"));
            }
        } catch (Exception e) {
            System.err.println(" Eroare la adăugarea datelor de test: " + e.getMessage());
        }
    }

    public void start() {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Manage Clients");
            System.out.println("2. Manage Sessions");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> manageClients();
                    case 2 -> manageSessions();
                    case 0 -> { return; }
                    default -> System.out.println("⚠ Invalid option!");
                }
            } catch (NumberFormatException e) {
                System.err.println(" Invalid input! Enter a number.");
            }
        }
    }

    private void manageClients() {
        System.out.println("\n--- CLIENT MANAGEMENT ---");
        System.out.println("1. Add client | 2. Delete client | 3. Update client | 4. Show all");
        System.out.println("5. Filter by name | 6. Filter by email domain");
        System.out.print("Option: ");
        try {
            int opt = Integer.parseInt(scanner.nextLine());
            switch (opt) {
                case 1 -> {
                    System.out.print("ID: "); int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Name: "); String name = scanner.nextLine();
                    System.out.print("Email: "); String email = scanner.nextLine();
                    System.out.print("Phone: "); String phone = scanner.nextLine();
                    clientService.addClient(new Client(id, name, email, phone));
                    System.out.println(" Client added!");
                }
                case 2 -> {
                    System.out.print("ID to delete: "); int id = Integer.parseInt(scanner.nextLine());
                    // Acum folosim removeClient simplu (fără cascadă)
                    if(clientService.removeClient(id)) System.out.println(" Client deleted!");
                    else System.out.println(" Client not found!");
                }
                case 3 -> {
                    System.out.print("ID: "); int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Name: "); String name = scanner.nextLine();
                    System.out.print("New Email: "); String email = scanner.nextLine();
                    System.out.print("New Phone: "); String phone = scanner.nextLine();
                    clientService.updateClient(new Client(id, name, email, phone));
                    System.out.println(" Client updated!");
                }
                case 4 -> clientService.getAllClients().forEach(System.out::println);
                case 5 -> {
                    System.out.print("Name substring: ");
                    clientService.filterClients(new ClientNameFilter(scanner.nextLine())).forEach(System.out::println);
                }
                case 6 -> {
                    System.out.print("Email domain: ");
                    clientService.filterClients(new ClientEmailFilter(scanner.nextLine())).forEach(System.out::println);
                }
            }
        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }

    private void manageSessions() {
        System.out.println("\n--- SESSION MANAGEMENT ---");
        System.out.println("1. Add | 2. Delete | 3. Update | 4. Show all | 5. Filter Desc | 6. Filter Date");
        System.out.println("7. Sessions for Client | 8. Phone by Client ID | 9. Between Dates | 10. Keyword | 11. Domain Report");
        System.out.print("Option: ");
        try {
            int opt = Integer.parseInt(scanner.nextLine());
            switch (opt) {
                case 1 -> {
                    System.out.print("ID: "); int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Client ID: "); int cId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Year: "); int y = Integer.parseInt(scanner.nextLine());
                    System.out.print("Month: "); int m = Integer.parseInt(scanner.nextLine());
                    System.out.print("Day: "); int d = Integer.parseInt(scanner.nextLine());
                    System.out.print("Desc: "); String desc = scanner.nextLine();
                    sessionService.addSession(new Session(id, cId, LocalDateTime.of(y, m, d, 12, 0), desc));
                }
                case 2 -> {
                    System.out.print("ID: "); int id = Integer.parseInt(scanner.nextLine());
                    sessionService.removeSession(id);
                }
                case 4 -> sessionService.getAllSessions().forEach(System.out::println);
                case 7 -> {
                    System.out.print("Client ID: "); int id = Integer.parseInt(scanner.nextLine());
                    sessionService.reportSessionsForClient(id).forEach(System.out::println);
                }
                case 8 -> {
                    System.out.print("Client ID: "); int id = Integer.parseInt(scanner.nextLine());
                    // Folosim direct clientService-ul nostru
                    sessionService.reportPhoneByClientId(id, this.clientService)
                            .ifPresentOrElse(System.out::println, () -> System.out.println("Not found"));
                }
                case 9 -> {
                    System.out.println("Start Date (Y M D):");
                    LocalDateTime start = LocalDateTime.of(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), 0, 0);
                    System.out.println("End Date (Y M D):");
                    LocalDateTime end = LocalDateTime.of(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), 23, 59);
                    scanner.nextLine();
                    sessionService.reportSessionsBetween(start, end).forEach(System.out::println);
                }
                case 11 -> {
                    System.out.print("Domain: ");
                    sessionService.reportClientsByEmailDomain(scanner.nextLine(), this.clientService)
                            .forEach(System.out::println);
                }
                // ... restul switch-ului se mentine similar
            }
        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }
}