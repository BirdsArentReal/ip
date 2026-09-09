package duchess.ui.gui.components;

import duchess.ui.Duchess;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Displays the main window the user views and interacts with.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;

    @FXML
    private AnchorPane inputContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Duchess duchess;

    @FXML
    private void initialize() {
        assert (scrollPane != null) : "scrollPane must be injected from MainWindow.fxml";
        assert (dialogContainer != null) : "dialogContainer must be injected from MainWindow.fxml";
        assert (inputContainer != null) : "inputContainer must be injected from MainWindow.fxml";
        assert (userInput != null) : "userInput must be injected from MainWindow.fxml";
        assert (sendButton != null) : "sendButton must be injected from MainWindow.fxml";

        this.scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Sets the bot to the instance specified.
     * Additionally, greets the user, once the
     * bot is successfully set.
     */
    public void setBot(Duchess d) {
        assert (d != null) : "The application requires the bot to be set";

        this.duchess = d;
        this.greet();
    }

    /**
     * Greets the user when the application is opened.
     */
    private void greet() {
        DialogBox greeting = DialogBox.getDuchessDialogBox(
                "Hello! I am " + Duchess.NAME + ". \n"
                        + "What can I do for you?"
        );
        this.dialogContainer.getChildren().add(greeting);
    }

    @FXML
    private void handleUserInput() {
        assert (this.duchess != null) : "Must set bot before handling user input";

        String input = this.userInput.getText();
        String response = this.duchess.respondTo(input);

        /* Display in dialog boxes. */
        DialogBox userText = DialogBox.getUserDialogBox(
                input
        );
        DialogBox dukeResponse = DialogBox.getDuchessDialogBox(
                response
        );

        this.dialogContainer.getChildren().addAll(
                userText,
                dukeResponse
        );

        this.userInput.clear();

        /* Close the application if the user says bye. */
        if (response.equals(Duchess.getExitMessage())) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1.0));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
