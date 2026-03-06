package gym.ui.gui;

import gym.domain.Session;
import gym.service.SessionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SessionController {

    private static SessionService sessionService;

    @FXML private TableView<Session> sessionTable;
    @FXML private TableColumn<Session, Integer> idColumn;
    @FXML private TableColumn<Session, Integer> clientIdColumn;
    @FXML private TableColumn<Session, String> dateTimeColumn;
    @FXML private TableColumn<Session, String> descriptionColumn;

    @FXML private TextField idField;
    @FXML private TextField clientIdField;
    @FXML private TextField dateTimeField;
    @FXML private TextField descriptionField;

    private ObservableList<Session> data;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void setSessionService(SessionService svc) {
        sessionService = svc;
    }

    @FXML
    public void initialize() {
        if (sessionService == null) {
            throw new RuntimeException("SessionService not set!");
        }

        // Configure columns
        idColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        clientIdColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getClientId()).asObject());
        dateTimeColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDateTime().format(FORMATTER)));
        descriptionColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDescription()));

        // Selection listener
        sessionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                idField.setText(String.valueOf(newSel.getId()));
                clientIdField.setText(String.valueOf(newSel.getClientId()));
                dateTimeField.setText(newSel.getDateTime().format(FORMATTER));
                descriptionField.setText(newSel.getDescription());
            }
        });

        refresh();
    }

    @FXML
    private void handleAddSession() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int clientId = Integer.parseInt(clientIdField.getText().trim());
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeField.getText().trim(), FORMATTER);

            Session s = new Session(id, clientId, dateTime, descriptionField.getText().trim());
            sessionService.addSession(s);
            showInfo("Session added successfully!");
            handleClearFields();
            refresh();
        } catch (NumberFormatException e) {
            showError("Invalid number format for ID or Client ID!");
        } catch (DateTimeParseException e) {
            showError("Invalid date format! Use: yyyy-MM-dd HH:mm");
        } catch (Exception ex) {
            showError("Add Error: " + ex.getMessage());
        }
    }

    @FXML
    private void handleUpdateSession() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int clientId = Integer.parseInt(clientIdField.getText().trim());
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeField.getText().trim(), FORMATTER);

            Session s = new Session(id, clientId, dateTime, descriptionField.getText().trim());
            sessionService.updateSession(s);
            showInfo("Session updated successfully!");
            handleClearFields();
            refresh();
        } catch (NumberFormatException e) {
            showError("Invalid number format!");
        } catch (DateTimeParseException e) {
            showError("Invalid date format! Use: yyyy-MM-dd HH:mm");
        } catch (Exception ex) {
            showError("Update Error: " + ex.getMessage());
        }
    }

    @FXML
    private void handleDeleteSession() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            sessionService.removeSession(id);
            showInfo("Session deleted successfully!");
            handleClearFields();
            refresh();
        } catch (NumberFormatException e) {
            showError("Invalid ID format!");
        } catch (Exception ex) {
            showError("Delete Error: " + ex.getMessage());
        }
    }

    @FXML
    private void handleClearFields() {
        idField.clear();
        clientIdField.clear();
        dateTimeField.clear();
        descriptionField.clear();
        sessionTable.getSelectionModel().clearSelection();
    }

    private void refresh() {
        data = FXCollections.observableArrayList(sessionService.getAllSessions());
        sessionTable.setItems(data);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}