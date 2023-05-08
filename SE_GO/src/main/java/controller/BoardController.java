package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.*;
import model.singleComponents.HLine;
import model.singleComponents.Settings;
import model.singleComponents.Stone;
import model.singleComponents.VLine;

public class BoardController {
    private int boardSize;
    private int handicap;
    private double komi;
    int sceneWidth = 0;
    double stoneRatio = 0.8;
    private GoModel model;

    @FXML
    private Button btn_exit;

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

    int zug = 0;

    public void initData(Settings s) {
        boardSize = s.getBoardSize();
        handicap = s.getHandicap();
        komi = s.getKomi();

        model = new GoModel(boardSize);

        createAndConfigurePane();
        createAndLayoutControls();
        updateControllerFromListeners();
    }

    @FXML
    void onExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onPass(ActionEvent event) {
        zug++;
        if(zug == 2){
            //TODO: Spiel vorbei...
        }
    }

    @FXML
    void onSave(ActionEvent event) {

    }

    @FXML
    void onSurrender(ActionEvent event) {

    }

    private void createAndConfigurePane() {
        gp_boardGrid.setGridLinesVisible(false);

        gp_boardGrid.setStyle("-fx-background-color: #FAEBD7;");
        gp_bigGrid.setStyle("-fx-background-color: #FAEBD7;");

        this.sceneWidth = (int) gp_boardGrid.getWidth();

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (49 + (i % 9));
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, 0, i + 1);
            //gp_boardGrid.setHalignment(text, HPos.CENTER);
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65 + i);
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, i + 1, 0);
            //gp_boardGrid.setHalignment(text, HPos.CENTER);
        }

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int id = i * boardSize + j;
                Group group = new Group();
                int stoneRadius = sceneWidth / (boardSize + 2) / 2;
                Stone stone = new Stone(id, stoneRadius * stoneRatio, model);
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
                gp_boardGrid.add(group, j + 1, i + 1);
            }
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (49 + (i % 9));
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, boardSize + 1, i + 1);
            //gp_boardGrid.setHalignment(text, HPos.CENTER);
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65 + i);
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, i + 1, boardSize + 1);
            //gp_boardGrid.setHalignment(text, HPos.CENTER);
        }
    }

    private void createAndLayoutControls() {
        //gp_bigGrid.add(gp_boardGrid, 1, 0);
        //btn_cstones = new Button("CSTONES");
        //bigGrid.add(btn_cstones, 0, 0);
    }

    private void updateControllerFromListeners() {
        /*btn_cstones.setOnMouseClicked(mouseEvent -> {
            model.removeCatchedStones();
            gridReload();
        });*/

        gp_bigGrid.widthProperty().addListener((obs, oldVal, newVal) -> {
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

        gp_bigGrid.heightProperty().addListener((obs, oldVal, newVal) -> {
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
            if (n instanceof Stone stone) {
                stone.setFill(model.getColorById(stone.id));
            }
        }
    }


}
