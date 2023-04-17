package model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GoApplication extends Application {
    private final int size;

    public GoApplication(int size) {
        this.size = size;
    }

    @Override
    public void start(Stage stage) {

        GoModel model = new GoModel(size);
        GoView view = new GoView(model);

        Scene scene = new Scene(view.asParent(), 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
