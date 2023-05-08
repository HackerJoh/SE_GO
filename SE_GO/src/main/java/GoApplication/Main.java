package GoApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;


public class Main extends Application {
    // The start() method is called when the JavaFX application is launched
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
        primaryStage.getIcons().add(new Image("file:go_logo.png"));
        primaryStage.show();
    }
    // The main() method is the entry point of the Java application
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

}
