package gym.ui.gui;

import gym.service.ClientCascadingService;
import gym.service.ClientService;
import gym.service.SessionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainMenuController {

    private static ClientCascadingService clientCascadingService;
    private static SessionService sessionService;
    private static ClientService clientService;

    @FXML private StackPane contentArea;

    public static void setServices(ClientCascadingService cascSvc, SessionService sessSvc, ClientService cliSvc) {
        clientCascadingService = cascSvc;
        sessionService = sessSvc;
        clientService = cliSvc;
    }

    @FXML
    public void initialize() {
        // Load clients view by default
        showClients();
    }

    @FXML
    private void showClients() {
        loadView("/gym/ui/gui/ClientView.fxml", () ->
                ClientController.setClientService(clientCascadingService));
    }

    @FXML
    private void showSessions() {
        loadView("/gym/ui/gui/SessionView.fxml", () ->
                SessionController.setSessionService(sessionService));
    }

    @FXML
    private void showReports() {
        loadView("/gym/ui/gui/ReportsView.fxml", () ->
                ReportsController.setServices(sessionService, clientService));
    }

    private void loadView(String fxmlPath, Runnable setupController) {
        try {
            setupController.run();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load: " + fxmlPath);
        }
    }
}