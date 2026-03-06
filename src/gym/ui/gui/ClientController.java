package gym.ui.gui;

import gym.domain.Client;
import gym.service.ClientCascadingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller pentru ClientView.fxml.
 * Gestioneaza operatiunile CRUD pentru Clienti.
 */
public class ClientController {

    private static ClientCascadingService clientService;

    // FXML fields - au fost eliminate referintele la butoane (addButton, etc.)
    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, Integer> idColumn;
    @FXML private TableColumn<Client, String> nameColumn;
    @FXML private TableColumn<Client, String> emailColumn;
    @FXML private TableColumn<Client, String> phoneColumn;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    private ObservableList<Client> data;

    /**
     * Seteaza serviciul static, apelat din MainApp inainte de incarcarea FXML.
     */
    public static void setClientService(ClientCascadingService svc) {
        clientService = svc;
    }

    @FXML
    public void initialize() {
        if (clientService == null) {
            throw new RuntimeException("ClientCascadingService nu este setat. Apelati ClientController.setClientService(...) inainte de incarcarea FXML.");
        }

        // 1. Configurarea coloanelor TableView
        idColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        emailColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        // CORECȚIE: Acesta este fix locul unde apărea eroarea. Acum folosim getPhone().
        phoneColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));

        // 2. Listener pentru selectarea rândurilor (populează câmpurile pentru Update/Delete)
        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(String.valueOf(newSelection.getId()));
                nameField.setText(newSelection.getName());
                emailField.setText(newSelection.getEmail());
                // CORECȚIE: Folosim getPhone() pentru a popula campul de editare.
                phoneField.setText(newSelection.getPhone());
            }
        });

        // 3. Incarcarea datelor
        refresh();
    }

    @FXML
    private void handleAddClient() {
        try {
            int id = Integer.parseInt(idField.getText());
            Client c = new Client(id, nameField.getText(), emailField.getText(), phoneField.getText());
            clientService.addClient(c);
            handleClearFields();
            refresh();
        } catch (Exception ex) {
            showError("Eroare Adăugare: " + ex.getMessage());
        }
    }

    @FXML
    private void handleUpdateClient() {
        try {
            int id = Integer.parseInt(idField.getText());
            Client c = new Client(id, nameField.getText(), emailField.getText(), phoneField.getText());
            clientService.updateClient(c);
            handleClearFields();
            refresh();
        } catch (Exception ex) {
            showError("Eroare Modificare: " + ex.getMessage());
        }
    }

    @FXML
    private void handleDeleteClient() {
        try {
            int id = Integer.parseInt(idField.getText());
            // Folosim removeClientCascading pentru a sterge si sesiunile asociate
            clientService.removeClientCascading(id);
            handleClearFields();
            refresh();
        } catch (Exception ex) {
            showError("Eroare Ștergere: " + ex.getMessage());
        }
    }

    @FXML
    private void handleClearFields() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        clientTable.getSelectionModel().clearSelection();
    }

    private void refresh() {
        data = FXCollections.observableArrayList(clientService.getAllClients());
        clientTable.setItems(data);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
    @FXML
    private void handleUndo() {
        try {
            clientService.undo();
            refresh();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleRedo() {
        try {
            clientService.redo();
            refresh();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

}