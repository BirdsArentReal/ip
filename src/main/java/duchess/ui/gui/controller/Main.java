package duchess.ui.gui.controller;

import java.io.IOException;

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));

            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setBot(
                    new Duchess("data", "duchess.txt")
            );

            stage.setTitle("Duke");
            stage.setMinWidth(400.0);
            stage.setMinHeight(600.0);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
