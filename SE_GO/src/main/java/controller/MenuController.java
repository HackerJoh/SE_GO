package controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import singleComponents.MoveList;
import singleComponents.Settings;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    protected ComboBox<String> cbx_boardSize;

    @FXML
    protected Spinner<Integer> sp_handicap;

    @FXML
    protected Spinner<Double> sp_komi;

    @FXML
    protected Button btn_loadGame;

    @FXML
    protected Button btn_startGame;

    @FXML
    protected Label lbl_boardSize;

    @FXML
    protected Label lbl_handicap;

    @FXML
    protected Label lbl_komi;

    @FXML
    protected Text txt_copyright;

    @FXML
    protected Text txt_heading;

    @FXML
    protected Text txt_subheading;

    private HostServices hostServices;
    private Settings s = new Settings();
    private boolean fileLoaded = false;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public HostServices getHostServices() {
        return hostServices;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sp_handicap.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9));
        sp_komi.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 0, 0.5));

        cbx_boardSize.getItems().removeAll(cbx_boardSize.getItems());
        cbx_boardSize.getItems().addAll("19x19", "13x13", "9x9");
        cbx_boardSize.getSelectionModel().select("13x13");

    }

    public void onLoad() throws IOException {
        Stage primaryStage = (Stage) btn_startGame.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File loadedFile = fileChooser.showOpenDialog(primaryStage);
        MoveList list = new MoveList();
        int size = list.getSizeFromFile(loadedFile);
        s.setLoadedFile(loadedFile);
        s.setBoardSize(size);
        fileLoaded = true;
        startGame();
    }

    public void startGame() throws IOException {
        if (!fileLoaded) {
            String boardSize = cbx_boardSize.getValue();
            switch (boardSize) {
                case "19x19" -> s.setBoardSize(19);
                case "13x13" -> s.setBoardSize(13);
                default -> s.setBoardSize(9);
            }
            s.setHandicap(sp_handicap.getValue());
            s.setKomi(sp_komi.getValue());
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Board_2.fxml"));
        Parent root = loader.load();

        Stage primaryStage = (Stage) btn_startGame.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene);
        BoardController controller = loader.getController();
        controller.setHostServices(hostServices);
        controller.initData(s);

        primaryStage.show();
    }

}
