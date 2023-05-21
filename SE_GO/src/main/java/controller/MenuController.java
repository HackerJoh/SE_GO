package controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import singleComponents.Settings;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private ComboBox<String> cbx_boardSize;

    @FXML
    private Spinner<Integer> sp_handicap;

    @FXML
    private Spinner<Double> sp_komi;

    @FXML
    private Button btn_loadGame;

    @FXML
    private Button btn_startGame;

    @FXML
    private Label lbl_boardSize;

    @FXML
    private Label lbl_handicap;

    @FXML
    private Label lbl_komi;

    @FXML
    private Text txt_copyright;

    @FXML
    private Text txt_heading;

    @FXML
    private Text txt_subheading;

    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices ;
    }

    public HostServices getHostServices() {
        return hostServices ;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sp_handicap.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9));
        sp_komi.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 0, 0.5));

        cbx_boardSize.getItems().removeAll(cbx_boardSize.getItems());
        cbx_boardSize.getItems().addAll("19x19", "13x13", "9x9");
        cbx_boardSize.getSelectionModel().select("13x13");

    }

    public void startGame() throws IOException {
        Settings s = new Settings();
        String boardSize = cbx_boardSize.getValue();
        switch (boardSize) {
            case "19x19" -> s.setBoardSize(19);
            case "13x13" -> s.setBoardSize(13);
            default -> s.setBoardSize(9);
        }
        s.setHandicap(sp_handicap.getValue());
        s.setKomi(sp_komi.getValue());

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
