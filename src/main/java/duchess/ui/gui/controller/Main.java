package duchess.ui.gui.controller;

import java.io.IOException;
import java.net.URL;

import duchess.ui.Duchess;
import duchess.ui.gui.components.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Sets up the window for the application.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Duchess duchess;
        try {
            duchess = new Duchess("data", "duchess.txt");
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialise task storage", e);
        }

        URL fxmlResource = Main.class.getResource("/view/MainWindow.fxml");
        assert (fxmlResource != null) : "MainWindow.fxml must be present in /resources/view/";
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);

        AnchorPane ap;
        try {
            ap = fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load MainWindow.fxml", e);
        }

        MainWindow mainWindow = fxmlLoader.getController();
        assert (mainWindow != null) : "MainWindow.fxml must provide a MainWindow controller";
        mainWindow.setBot(duchess);

        stage.setScene(new Scene(ap));
        stage.setTitle("Duke");
        stage.setMinWidth(400.0);
        stage.setMinHeight(600.0);
        stage.show();
    }


}
