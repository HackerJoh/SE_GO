package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.GoApplication;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sp_handicap.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9));
        sp_komi.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 0, 0.5));

        cbx_boardSize.getItems().removeAll(cbx_boardSize.getItems());
        cbx_boardSize.getItems().addAll("19x19", "13x13", "9x9");
        cbx_boardSize.getSelectionModel().select("19x19");

    }

    public void startGame() {
        GoApplication game;
        String selectedSize = cbx_boardSize.getValue();
        switch (selectedSize) {
            case "13x13" -> game = new GoApplication(13);
            case "9x9" -> game = new GoApplication(9);
            default -> game = new GoApplication(19);
        }
        game.start((Stage) btn_startGame.getScene().getWindow());

    }
}
