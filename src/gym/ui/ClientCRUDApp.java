package gym.ui;

import gym.domain.Client;
import gym.service.ClientCascadingService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ClientCRUDApp extends Application {

    public static ClientCascadingService clientServiceStatic;  // <-- AI NEVOIE DE ASTA

    private ClientCascadingService clientService;
    private ObservableList<Client> clients;

    public ClientCRUDApp() {
        this.clientService = clientServiceStatic;
        this.clients = FXCollections.observableArrayList(clientService.getAllClients());
    }

    @Override
    public void start(Stage primaryStage) {
        ListView<Client> clientListView = new ListView<>(clients);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();

        idField.setPromptText("ID");
        nameField.setPromptText("Name");
        emailField.setPromptText("Email");
        phoneField.setPromptText("Phone");

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        HBox fields = new HBox(10, idField, nameField, emailField, phoneField);
        HBox buttons = new HBox(10, addButton, updateButton, deleteButton);

        VBox root = new VBox(10, clientListView, fields, buttons);

        // Add client
        addButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                Client c = new Client(id, nameField.getText(), emailField.getText(), phoneField.getText());
                clientService.addClient(c);
                clients.setAll(clientService.getAllClients());
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        // Update client
        updateButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                Client c = new Client(id, nameField.getText(), emailField.getText(), phoneField.getText());
                clientService.updateClient(c);
                clients.setAll(clientService.getAllClients());
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        // Delete client
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                clientService.removeClientCascading(id);
                clients.setAll(clientService.getAllClients());
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.setTitle("Client CRUD GUI");
        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
