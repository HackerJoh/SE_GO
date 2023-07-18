package controller;


import controller.guiComponents.HLine;
import controller.guiComponents.Stone;
import controller.guiComponents.VLine;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.GoModel;
import model.gameStatistics.GameStatistics;
import org.kordamp.bootstrapfx.BootstrapFX;
import singleComponents.Settings;
import singleComponents.StoneColor;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class BoardController {
    private int boardSize;
    private final double stoneRatio = 0.8;
    public GoModel model;
    private int zug = 0;

    /**
     * Define the GUI elements as FXML-variables.
     */
    @FXML
    private ImageView img_forward;

    @FXML
    private ImageView img_backward;

    @FXML
    private Button btn_pass;

    @FXML
    private Button btn_surrender;

    @FXML
    private GridPane gp_boardGrid;

    @FXML
    private TextField txt_status;

    @FXML
    private Text txt_blackPoints;

    @FXML
    private Text txt_whitePoints;

    @FXML
    private CheckMenuItem cmi_inspection;

    /**
     * When exit-Button is pressed, quit the program.
     */
    @FXML
    private void onExit() {
        System.exit(0);
    }

    /**
     * Check if the users passed right behind each other and let the game end if this is the case.
     */
    @FXML
    private void onPass() {
        zug++;
        if (zug == 2) {
            endGame();
        } else {
            setStatusText(model.getPassColor());
        }
        model.setNoMoves(model.getNoMoves() + 1);
    }

    /**
     * Open a FileChooser to select where the file should be saved when clicking on save-button (only .json allowed).
     */
    @FXML
    private void onSave() throws IOException {
        Stage primaryStage = (Stage) btn_surrender.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JSON File", "*.json");
        fileChooser.getExtensionFilters().addAll(extensionFilter);
        File saveFile = fileChooser.showSaveDialog(primaryStage);
        if (saveFile != null)
            model.saveGame(saveFile);
    }

    /**
     * When surrender-Button is clicked, the opponent wins. Game points gets still counted.
     */
    @FXML
    private void onSurrender() {
        System.out.println(model.evaluateGame());
        setStatusText(model.getSurrenderer());
        disableBtns();
    }

    /**
     * Method handles the menu item "Inspektionsmodus" which contains a check.
     */
    @FXML
    private void onJumpMenu() {
        if (!model.isInspectionModeOn()) {
            model.enterInspectionMode();
            img_forward.setVisible(true);
            img_backward.setVisible(true);
        } else {
            model.turnOffInspectionModeIfOn();
            disableJump();
        }
    }

    /**
     * Stage will be changed to menu when user wants to play a new game.
     *
     * @throws IOException: load()-method IOException gets thrown towards Main-class.
     */
    @FXML
    private void newGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Menu.fxml"));
        Parent root = loader.load();

        Stage primaryStage = (Stage) btn_surrender.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    //TODO: auf Alert umbauen

    /**
     * Open a new Stage to display the Rules FXML.
     *
     * @throws Exception: Exception gets thrown towards Main-class.
     */
    @FXML
    private void openRules() throws Exception {
        Stage stage = new Stage();
        Application application = new Application() {
            @Override
            public void start(Stage stage) {
                ButtonType open = new ButtonType("Open", ButtonBar.ButtonData.OK_DONE);
                ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Regeln", open, close);
                a.setTitle("Regeln");
                a.setHeaderText("Alle Regeln im Überblick:");
                a.setContentText("https://de.wikipedia.org/wiki/Go-Regeln");
                Optional<ButtonType> result = a.showAndWait();
                if (result.get() == open) {
                    getHostServices().showDocument("https://de.wikipedia.org/wiki/Go-Regeln");
                }
            }
        };
        application.start(stage);
    }

    /**
     * Open a new Stage to display the About FXML.
     */
    @FXML
    private void openAbout() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Über");
        a.setHeaderText("JKU GO");
        a.setContentText("Developed 2023 by:\n\nJohannes Hacker\nMoritz Neuwirth\nJulian Lumetsberger\n\nKurs: PR Software Engineering");
        a.show();
    }

    /**
     * Change status text and game progress to one step forward.
     */
    @FXML
    private void jumpForward() {
        model.setDescriptionWhenJumping(txt_status.getText());
        model.jumpForward();
        setStatusText(model.getDescriptionFromJump());
        gridReload();
    }

    /**
     * Change status text and game progress to one step backward.
     */
    @FXML
    private void jumpBackward() {
        model.setDescriptionWhenJumping(txt_status.getText());
        model.jumpBackward();
        setStatusText(model.getDescriptionFromJump());
        gridReload();
    }

    /**
     * Method to create the board and model and configure the game settings for the BoardController.
     *
     * @param s: Settings from the menu get transferred to the BoardController.
     * @throws IOException: Exception from loadGame gets thrown to Main.
     */
    public void initData(Settings s) throws IOException {
        boardSize = s.getBoardSize();
        model = new GoModel(boardSize);
        model.setHandicap(s.getHandicap(), s.getKomi());

        createAndConfigurePane();
        updateControllerFromListeners();

        //If User loads a game from a file then we call the load method from the model
        File loadedFile = s.getLoadedFile();
        if (loadedFile != null) {
            model.loadGame(loadedFile);
        }

        gridReload();
        setStatusText(model.getTurnColor());
    }

    /**
     * @return: return boardSize to Stone class to set the location of a new stone.
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * When clicked on a point at the grid, place a stone in the color of the actual player.
     *
     * @param xCoord: x-coordinate where stone will be placed in the grid.
     * @param yCoord: y-coordinate where stone will be placed in the grid.
     */
    public void setStone(int xCoord, int yCoord) {
        // Stones can only be set when inspection mode is turned off
        if (model.turnOffInspectionModeIfOn()) {
            disableJump();
            cmi_inspection.setSelected(false);
        }
        model.controllerSetsStone(xCoord, yCoord, txt_status.getText());
        gridReload();
        setStatusText(model.getTurnColor());
        zug = 0;
    }

    /**
     * Change the status text on the mid bottom of the board.
     *
     * @param text: Text which will be displayed
     */
    private void setStatusText(String text) {
        txt_status.setText(text);
    }

    /**
     * When game ends, evaluate the points and the winner.
     */
    private void endGame() {
        GameStatistics endGame = model.evaluateGame();
        System.out.println(endGame);
        if (endGame.winner() == StoneColor.BLACK)
            setStatusText("SCHWARZ gewinnt!");
        else if (endGame.winner() == StoneColor.WHITE)
            setStatusText("WEIß gewinnt!");
        else
            setStatusText("Unentschieden!");
        disableBtns();
    }

    /**
     * Game buttons must not be clicked after game has ended.
     */
    private void disableBtns() {
        onJumpMenu();
        cmi_inspection.setSelected(true);
        btn_pass.setDisable(true);
        btn_surrender.setDisable(true);
        txt_status.setFocusTraversable(false);
    }

    /**
     * Stone class must know which player turn it is to give the right hover effect.
     *
     * @return: StoneColor enum from actual player.
     */
    public StoneColor getTurn() {
        return model.getTurn();
    }

    /**
     * Stone class must know when game has ended to stop hover and set actions.
     *
     * @return: boolean if game is still active.
     */
    public boolean isGameNotEnded() {
        return !model.isGameHasEnded();
    }

    /**
     * Disable the inspection mode by vanishing the arrow-buttons.
     */
    private void disableJump() {
        img_forward.setVisible(false);
        img_backward.setVisible(false);
    }

    /**
     * Draw the basic go-board based on the board size set in the menu.
     */
    private void createAndConfigurePane() {
        gp_boardGrid.setGridLinesVisible(false);

        int sceneWidth = (int) gp_boardGrid.getWidth();

        //create upper letters
        for (int i = 0; i < boardSize; i++) {
            Text text = new Text("" + (i + 1));
            gp_boardGrid.add(text, 2, i + 3);
        }

        //create left Numbers
        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65 + i);
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, i + 3, 2);
        }

        //create the Stones and the Lines for the board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int id = i * boardSize + j;
                Group group = new Group();
                int stoneRadius = sceneWidth / (boardSize + 2) / 2;
                Stone stone = new Stone(id, stoneRadius * stoneRatio, this);
                HLine newLineH;
                VLine newLineV;

                if (j == 0) {
                    newLineH = new HLine(id, 0, stoneRadius);
                } else if (j == boardSize - 1) {
                    newLineH = new HLine(id, -stoneRadius, 0);
                } else {
                    newLineH = new HLine(id, -stoneRadius, stoneRadius);
                }

                if (i == 0) {
                    newLineV = new VLine(id, 0, stoneRadius);
                } else if (i == boardSize - 1) {
                    newLineV = new VLine(id, -stoneRadius, 0);
                } else {
                    newLineV = new VLine(id, -stoneRadius, stoneRadius);
                }
                group.getChildren().add(newLineH);
                group.getChildren().add(newLineV);
                group.getChildren().add(stone);
                gp_boardGrid.add(group, j + 3, i + 3);
            }
        }

        for (int i = 0; i < boardSize; i++) {
            Text text = new Text("" + (i + 1));
            gp_boardGrid.add(text, boardSize + 3, i + 3);
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65 + i);
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, i + 3, boardSize + 3);
        }

        ColumnConstraints col = new ColumnConstraints();
        col.setMaxWidth(0);
        gp_boardGrid.getColumnConstraints().set(0, col);
        gp_boardGrid.getColumnConstraints().set(1, col);
    }


    /**
     * Adjust the height and the with of the GUI-elements when resizing the window
     */
    private void updateControllerFromListeners() {
        gp_boardGrid.widthProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(gp_boardGrid.getHeight(), newVal.doubleValue());
            radius = radius / (boardSize + 2) / 2;
            for (Node n : gp_boardGrid.getChildren()) {
                if (n instanceof Group) {
                    for (Node n2 : ((Group) n).getChildren()) {
                        if (n2 instanceof Stone) ((Stone) n2).setRadius(radius * stoneRatio);
                        if (n2 instanceof HLine l) {
                            if (l.getGoLineId() % boardSize == 0) {
                                l.setEndX(radius);
                            } else if (l.getGoLineId() % boardSize == boardSize - 1) {
                                l.setStartX(-radius);
                            } else {
                                l.setStartX(-radius);
                                l.setEndX(radius);
                            }
                        }
                    }
                }
            }
        });

        gp_boardGrid.heightProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(gp_boardGrid.getWidth(), newVal.doubleValue());
            radius = radius / (boardSize + 2) / 2;
            for (Node n : gp_boardGrid.getChildren()) {
                if (n instanceof Group) {
                    for (Node n2 : ((Group) n).getChildren()) {
                        if (n2 instanceof Stone) ((Stone) n2).setRadius(radius * stoneRatio);
                        if (n2 instanceof VLine) {
                            VLine l = (VLine) n2;
                            if (l.getGoLineId() / boardSize == 0) {
                                l.setEndY(radius);
                            } else if (l.getGoLineId() / boardSize == boardSize - 1) {
                                l.setStartY(-radius);
                            } else {
                                l.setStartY(-radius);
                                l.setEndY(radius);
                            }

                        }
                    }
                }
            }
        });
    }

    /**
     * Update the View when changes are made on the board.
     */
    private void gridReload() {
        for (Node n : gp_boardGrid.getChildren()) {
            if (n instanceof Group) {
                for (Node n2 : ((Group) n).getChildren()) {
                    if (n2 instanceof Stone) {
                        ((Stone) n2).setFill(model.getColorById(((Stone) n2).id));
                        ((Stone) n2).setUsed(model.getUsedById(((Stone) n2).id));
                    }
                }
            }
        }
        txt_whitePoints.setText("" + model.getWhitePoints());
        txt_blackPoints.setText("" + model.getBlackPoints());
    }
}
