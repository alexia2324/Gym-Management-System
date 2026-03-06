package gym.ui.gui;

import gym.domain.Client;
import gym.domain.Session;
import gym.filter.ClientEmailFilter;
import gym.filter.ClientNameFilter;
import gym.filter.SessionDateFilter;
import gym.filter.SessionDescriptionFilter;
import gym.service.ClientService;
import gym.service.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class ReportsController {

    private static SessionService sessionService;
    private static ClientService clientService;

    @FXML private TextField clientIdField1;
    @FXML private TextField clientIdField2;
    @FXML private TextField startDateField;
    @FXML private TextField endDateField;
    @FXML private TextField clientIdField4;
    @FXML private TextField keywordField;
    @FXML private TextField domainField;
    @FXML private TextField nameFilterField;
    @FXML private TextField emailFilterField;
    @FXML private TextField descFilterField;
    @FXML private TextField dateFilterField;
    @FXML private TextArea resultsArea;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void setServices(SessionService sessSvc, ClientService cliSvc) {
        sessionService = sessSvc;
        clientService = cliSvc;
        System.out.println("✓ ReportsController: Services set");
        System.out.println("  SessionService: " + (sessSvc != null ? "OK" : "NULL"));
        System.out.println("  ClientService: " + (cliSvc != null ? "OK" : "NULL"));
    }

    @FXML
    public void initialize() {
        System.out.println("ReportsController initialize() called");

        if (sessionService == null || clientService == null) {
            String error = "❌ Services not set! SessionService: " + sessionService + ", ClientService: " + clientService;
            System.err.println(error);
            resultsArea.setText(error);
            showError("Services not initialized properly!");
            return;
        }

        resultsArea.setText("Reports ready. Select a report from the options above.");
        System.out.println("✓ ReportsController initialized successfully");
    }

    @FXML
    private void handleSessionsForClient() {
        try {
            if (sessionService == null) {
                showError("SessionService is not available!");
                return;
            }

            String idText = clientIdField1.getText().trim();
            if (idText.isEmpty()) {
                showError("Please enter a Client ID!");
                return;
            }

            int clientId = Integer.parseInt(idText);
            List<Session> sessions = sessionService.reportSessionsForClient(clientId);
            displaySessions(sessions);
        } catch (NumberFormatException e) {
            showError("Invalid Client ID format!");
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handlePhoneByClientId() {
        try {
            if (sessionService == null || clientService == null) {
                showError("Services are not available!");
                return;
            }

            String idText = clientIdField2.getText().trim();
            if (idText.isEmpty()) {
                showError("Please enter a Client ID!");
                return;
            }

            int clientId = Integer.parseInt(idText);
            Optional<String> phone = sessionService.reportPhoneByClientId(clientId, clientService);
            resultsArea.setText(phone.isPresent() ? "Phone: " + phone.get() : "Client not found");
        } catch (NumberFormatException e) {
            showError("Invalid Client ID format!");
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSessionsBetween() {
        try {
            if (sessionService == null) {
                showError("SessionService is not available!");
                return;
            }

            String startText = startDateField.getText().trim();
            String endText = endDateField.getText().trim();

            if (startText.isEmpty() || endText.isEmpty()) {
                showError("Please enter both start and end dates!");
                return;
            }

            LocalDateTime start = LocalDateTime.parse(startText, FORMATTER);
            LocalDateTime end = LocalDateTime.parse(endText, FORMATTER);
            List<Session> sessions = sessionService.reportSessionsBetween(start, end);
            displaySessions(sessions);
        } catch (DateTimeParseException e) {
            showError("Invalid date format! Use: yyyy-MM-dd HH:mm");
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSessionsWithKeyword() {
        try {
            if (sessionService == null) {
                showError("SessionService is not available!");
                return;
            }

            String idText = clientIdField4.getText().trim();
            String keyword = keywordField.getText().trim();

            if (idText.isEmpty() || keyword.isEmpty()) {
                showError("Please enter both Client ID and Keyword!");
                return;
            }

            int clientId = Integer.parseInt(idText);
            List<Session> sessions = sessionService.reportSessionsWithKeyword(clientId, keyword);
            displaySessions(sessions);
        } catch (NumberFormatException e) {
            showError("Invalid Client ID format!");
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleClientsByDomain() {
        try {
            if (sessionService == null || clientService == null) {
                showError("Services are not available!");
                return;
            }

            String domain = domainField.getText().trim();
            if (domain.isEmpty()) {
                showError("Please enter an email domain!");
                return;
            }

            List<Client> clients = sessionService.reportClientsByEmailDomain(domain, clientService);
            displayClients(clients);
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleFilterClientsByName() {
        try {
            if (clientService == null) {
                showError("ClientService is not available!");
                return;
            }

            String name = nameFilterField.getText().trim();
            if (name.isEmpty()) {
                showError("Please enter a name to filter!");
                return;
            }

            List<Client> clients = clientService.filterClients(new ClientNameFilter(name));
            displayClients(clients);
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleFilterClientsByEmail() {
        try {
            if (clientService == null) {
                showError("ClientService is not available!");
                return;
            }

            String domain = emailFilterField.getText().trim();
            if (domain.isEmpty()) {
                showError("Please enter an email domain to filter!");
                return;
            }

            List<Client> clients = clientService.filterClients(new ClientEmailFilter(domain));
            displayClients(clients);
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleFilterSessionsByDesc() {
        try {
            if (sessionService == null) {
                showError("SessionService is not available!");
                return;
            }

            String keyword = descFilterField.getText().trim();
            if (keyword.isEmpty()) {
                showError("Please enter a keyword to filter!");
                return;
            }

            List<Session> sessions = sessionService.filterSessions(new SessionDescriptionFilter(keyword));
            displaySessions(sessions);
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleFilterSessionsByDate() {
        try {
            if (sessionService == null) {
                showError("SessionService is not available!");
                return;
            }

            String dateText = dateFilterField.getText().trim();
            if (dateText.isEmpty()) {
                showError("Please enter a date to filter!");
                return;
            }

            LocalDateTime date = LocalDateTime.parse(dateText + " 00:00", FORMATTER);
            List<Session> sessions = sessionService.filterSessions(new SessionDateFilter(date));
            displaySessions(sessions);
        } catch (DateTimeParseException e) {
            showError("Invalid date format! Use: yyyy-MM-dd");
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void displaySessions(List<Session> sessions) {
        if (sessions == null) {
            resultsArea.setText("Error: No results returned (null)");
            return;
        }

        if (sessions.isEmpty()) {
            resultsArea.setText("No sessions found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(sessions.size()).append(" session(s):\n\n");
        for (Session s : sessions) {
            sb.append(s.toString()).append("\n");
        }
        resultsArea.setText(sb.toString());
    }

    private void displayClients(List<Client> clients) {
        if (clients == null) {
            resultsArea.setText("Error: No results returned (null)");
            return;
        }

        if (clients.isEmpty()) {
            resultsArea.setText("No clients found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(clients.size()).append(" client(s):\n\n");
        for (Client c : clients) {
            sb.append(c.toString()).append("\n");
        }
        resultsArea.setText(sb.toString());
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}