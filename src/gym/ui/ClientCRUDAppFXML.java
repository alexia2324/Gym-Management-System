package gym.ui;

import gym.service.ClientCascadingService;
import gym.service.ClientService;
import gym.service.SessionService;
import gym.ui.gui.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientCRUDAppFXML extends Application {

    public static ClientCascadingService clientCascadingServiceStatic;
    public static SessionService sessionServiceStatic;
    public static ClientService clientServiceStatic;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set services in MainMenuController
        MainMenuController.setServices(
                clientCascadingServiceStatic,
                sessionServiceStatic,
                clientServiceStatic
        );

        // Load main menu
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gym/ui/gui/MainMenuView.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Gym Management System");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
    }
}