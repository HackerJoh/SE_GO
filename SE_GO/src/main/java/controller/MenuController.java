package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.GoView;

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
        GoView game = new GoView();
        String selectedSize = cbx_boardSize.getValue();
        switch (selectedSize) {
            case "19x19" -> game.setBoardSize(19);
            case "13x13" -> game.setBoardSize(13);
            case "9x9" -> game.setBoardSize(9);
        }
        game.setHandicap(sp_handicap.getValue());
        game.setKomi(sp_komi.getValue());
        game.start((Stage) btn_startGame.getScene().getWindow());

    }
}
