package GoApplication;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.Objects;


public class Main extends Application {

    /**
     * The start() method is called when the JavaFX application is launched.
     * @param primaryStage: Transports the actual stage which will be displayed.
     * @throws Exception: Throw exception towards main-method.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load the FXML file that defines the GUI layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Menu.fxml"));
        Parent root = loader.load();

        // Create a new scene and add the GUI layout to it
        Scene scene = new Scene(root);

        // Add the BootstrapFX stylesheet to the scene
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        // Set the scene on the primary stage and display it
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/go_logo.png"))));
        primaryStage.show();

    }

    /**
     * Launch the JavaFX application
     */
    public static void main(String[] args) {
        launch(args);
    }

}
