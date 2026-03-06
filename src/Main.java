import gym.service.*;
import gym.ui.ClientCRUDApp;
import gym.ui.ConsoleUI;
import gym.util.AppBootstrap;
import javafx.application.Application;
import gym.ui.ClientCRUDAppFXML;

import gym.undo.UndoService;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        UndoService undoService = new UndoService();

        System.out.println("Alege modul de rulare:");
        System.out.println("1 - Consola (CLI)");
        System.out.println("2 - GUI JavaFX");
        System.out.println("3 - GUI JavaFX (FXML)");


        Scanner scanner = new Scanner(System.in);
        System.out.print("Optiunea ta: ");
        String opt = scanner.nextLine();

        switch (opt) {
            case "1":
                // Consola construiește tot (repo, servicii, validatori)
                new ConsoleUI().start();
                break;

            case "2":
                AppBootstrap bootstrap = new AppBootstrap();

                ClientCascadingService cascading =
                        new ClientCascadingService(
                                bootstrap.clientService,
                                bootstrap.sessionService,
                                undoService
                        );

                ClientCRUDApp.clientServiceStatic = cascading;
                Application.launch(ClientCRUDApp.class, args);
                break;
            case "3":
                // GUI cu FXML (assignment nou)
                AppBootstrap bootstrap3 = new AppBootstrap();

                ClientCascadingService cascading3 =
                        new ClientCascadingService(
                                bootstrap3.clientService,
                                bootstrap3.sessionService,
                                undoService
                        );

                ClientCRUDAppFXML.clientCascadingServiceStatic = cascading3;
                ClientCRUDAppFXML.sessionServiceStatic = bootstrap3.sessionService;
                ClientCRUDAppFXML.clientServiceStatic = bootstrap3.clientService;

                Application.launch(ClientCRUDAppFXML.class, args);
                break;

            default:
                System.out.println("Optiune invalida");
        }
    }
}
