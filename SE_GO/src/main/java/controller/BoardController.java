package controller;


import controller.guiComponents.HLine;
import controller.guiComponents.Stone;
import controller.guiComponents.VLine;
import javafx.application.HostServices;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.GoModel;
import org.kordamp.bootstrapfx.BootstrapFX;

import singleComponents.*;

import java.io.File;
import java.io.IOException;

public class BoardController {
    private int boardSize;
    private int handicap;
    private final double stoneRatio = 0.8;
    public GoModel model;

    public int getBoardSize() {
        return boardSize;
    }

    @FXML
    private Button btn_exit;

    @FXML
    private ImageView img_forward;

    @FXML
    private ImageView img_backward;

    @FXML
    private Button btn_pass;

    @FXML
    private Button btn_saveGame;

    @FXML
    private Button btn_surrender;

    @FXML
    private GridPane gp_bigGrid;

    @FXML
    private GridPane gp_boardGrid;

    @FXML
    private TextField txt_status;

    @FXML
    private Text txt_blackPoints;

    @FXML
    private Text txt_blackPointsLabel;

    @FXML
    private Text txt_heading;

    @FXML
    private Text txt_whitePoints;

    @FXML
    private Text txt_whitePointsLabel;

    @FXML
    private CheckMenuItem cmi_inspection;


    private int zug = 0;

    public void setZug(int zug) {
        this.zug = zug;
    }


    public void initData(Settings s) throws IOException {
        boardSize = s.getBoardSize();
        handicap = s.getHandicap();
        File loadedFile = s.getLoadedFile();

        model = new GoModel(boardSize);

        model.setBlackPoints(s.getKomi());
        //TODO: Handicap

        createAndConfigurePane();
        updateControllerFromListeners();
        if (loadedFile != null) {
            model.loadGame(loadedFile);
        }
        gridReload();
        setStatusText("WEIß ist am Zug");
    }

    @FXML
    private void onExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onPass(ActionEvent event) {
        zug++;
        if (zug == 2) {
            endGame();
        } else {
            setStatusText(model.getPassColor());
        }
        model.setNoMoves(model.getNoMoves() + 1);
    }

    @FXML
    private void onSave(ActionEvent event) {
        Stage primaryStage = (Stage) btn_surrender.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JSON File", "*.json");
        fileChooser.getExtensionFilters().addAll(extensionFilter);
        File saveFile = fileChooser.showSaveDialog(primaryStage);
        if (saveFile != null)
            model.saveGame(saveFile);
    }

    @FXML
    private void onSurrender(ActionEvent event) {
        System.out.println(model.evaluateGame());
        setStatusText(model.getSurrenderer());
        disableBtns();
    }

    @FXML
    private void onJumpMenu() {
        if (!model.isJumpModeOn()) {
            model.enterJumpMode();
            img_forward.setVisible(true);
            img_backward.setVisible(true);
        } else {
            model.turnOffJumpModeIfOn();
            disableJump();
        }

    }

    @FXML
    private void newGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Menu.fxml"));
        Parent root = loader.load();

        Stage primaryStage = (Stage) btn_surrender.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @FXML
    private void openRules(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Rules.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Regeln");
        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    @FXML
    private void openAbout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/About.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Über");
        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    @FXML
    private void jumpForward() {
        model.setDescriptionFromForward(txt_status.getText());
        model.jumpForward();
        setStatusText(model.getDescriptionFromJump());
        gridReload();
    }

    @FXML
    private void jumpBackward() {
        model.setDescriptionFromBackward(txt_status.getText());
        model.jumpBackward();
        setStatusText(model.getDescriptionFromJump());
        gridReload();
    }

    private void setStatusText(String text) {
        txt_status.setText(text);
    }

    private void disableBtns() {
        onJumpMenu();
        cmi_inspection.setSelected(true);
        btn_pass.setDisable(true);
        btn_surrender.setDisable(true);
        txt_status.setFocusTraversable(false);
    }

    private void endGame() {
        System.out.println(model.evaluateGame());
        setStatusText(model.getWinner());
        disableBtns();
    }

    private void createAndConfigurePane() {
        gp_boardGrid.setGridLinesVisible(false);

        int sceneWidth = (int) gp_boardGrid.getWidth();

        //create upper letters
        for (int i = 0; i < boardSize; i++) {
            Text text = new Text("" + (i + 1));
            gp_boardGrid.add(text, 2, i + 3);
            gp_boardGrid.setHalignment(text, HPos.CENTER);
        }

        //create left Numbers
        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65 + i);
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, i + 3, 2);
            gp_boardGrid.setHalignment(text, HPos.CENTER);
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
            gp_boardGrid.setHalignment(text, HPos.CENTER);
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65 + i);
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, i + 3, boardSize + 3);
            gp_boardGrid.setHalignment(text, HPos.CENTER);
        }

        ColumnConstraints col = new ColumnConstraints();
        col.setMaxWidth(0);
        gp_boardGrid.getColumnConstraints().set(0, col);
        gp_boardGrid.getColumnConstraints().set(1, col);
    }


    private void updateControllerFromListeners() {
        gp_boardGrid.widthProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(gp_boardGrid.getHeight(), newVal.doubleValue());
            radius = radius / (boardSize + 2) / 2;
            for (Node n : gp_boardGrid.getChildren()) {
                if (n instanceof Group) {
                    for (Node n2 : ((Group) n).getChildren()) {
                        if (n2 instanceof Stone) ((Stone) n2).setRadius(radius * stoneRatio);
                        if (n2 instanceof HLine) {
                            HLine l = (HLine) n2;
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

    public void setStone(int xCoord, int yCoord) {
        if (model.turnOffJumpModeIfOn()) {
            disableJump();
            cmi_inspection.setSelected(false);
        }
        model.controllerSetsStone(xCoord, yCoord, txt_status.getText());
        gridReload();
        setStatusText(model.getTurnColor());
        zug = 0;
    }

    public StoneColor getTurn() {
        return model.getTurn();
    }

    public boolean isGameEnded() {
        return model.isGameHasEnded();
    }

    private void disableJump() {
        img_forward.setVisible(false);
        img_backward.setVisible(false);
    }

}
