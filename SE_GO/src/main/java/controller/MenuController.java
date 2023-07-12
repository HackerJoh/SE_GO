package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.modelComponents.MoveList;
import org.kordamp.bootstrapfx.BootstrapFX;
import singleComponents.Settings;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private final Settings s = new Settings();
    private boolean fileLoaded = false;
    /**
     * Define the GUI elements as FXML-variables.
     */
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

    /**
     * On startup set the min, max and step parameters from the spinner fields.
     * Further set the options for the boardSize-dropdown and select as default 13x13.
     *
     * @param location:  default parameter from super method
     * @param resources: default parameter from super method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sp_handicap.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9));
        sp_komi.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10, 0, 0.5));

        cbx_boardSize.getItems().removeAll(cbx_boardSize.getItems());
        cbx_boardSize.getItems().addAll("19x19", "13x13", "9x9");
        cbx_boardSize.getSelectionModel().select("13x13");

    }

    /**
     * Open a FileChooser to let the user select a game file (.json) to load the specified game.
     *
     * @throws IOException: Throw given Exception towards Main.
     */
    @FXML
    private void onLoad() throws IOException {
        Stage primaryStage = (Stage) btn_startGame.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JSON File", "*.json");
        fileChooser.getExtensionFilters().addAll(extensionFilter);
        File loadedFile = fileChooser.showOpenDialog(primaryStage);

        //start game only if a correct file is chosen
        if (loadedFile != null) {
            int size = MoveList.getSizeFromFile(loadedFile);
            s.setLoadedFile(loadedFile);
            s.setBoardSize(size);
            fileLoaded = true;
            startGame();
        }
    }

    /**
     * Save chosen boardSize, handicap and komi into settings object and load it into BoardController, finally start the board Stage.
     *
     * @throws IOException: Throw given Exception towards Main.
     */
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Board.fxml"));
        Parent root = loader.load();

        Stage primaryStage = (Stage) btn_startGame.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene);
        BoardController controller = loader.getController();
        controller.initData(s);

        primaryStage.show();
    }

}
