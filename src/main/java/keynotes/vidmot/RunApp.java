package keynotes.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class RunApp extends Application {

    /**
     * The start() method initializes and displays the UI by loading the keys-view.fxml file, setting the scene, and showing the stage.
     * It also sets up event handlers for key presses and releases.
     * @param stage the primary stage of the UI
     * @throws IOException if the keys-view.fxml file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("keys-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 310);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        stage.setTitle("KeyNotes");
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        Controller controller = fxmlLoader.getController();
        scene.setOnKeyPressed(controller::onKeyPressed);
        scene.setOnKeyReleased(controller::onKeyReleased);
        stage.show();
    }

    /**
     * The main() method launches the JavaFX application by calling the launch() method of the Application class.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}