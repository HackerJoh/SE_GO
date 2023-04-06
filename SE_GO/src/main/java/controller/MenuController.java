package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private ComboBox<String> cbx_boardSize;

    @FXML
    private TextField tf_handicap;

    @FXML
    private TextField tf_komi;

    @FXML
    private Button btn_loadGame;

    @FXML
    private Button btn_startGame;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbx_boardSize.getItems().removeAll(cbx_boardSize.getItems());
        cbx_boardSize.getItems().addAll("19x19", "13x13", "9x9");
        cbx_boardSize.getSelectionModel().select("19x19");
    }
}
