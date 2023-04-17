package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.GoModel;
import model.Settings;
import model.Stone;

public class BoardController {
    private int boardSize;
    private int handicap;
    private double komi;
    private GoModel model;

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

    private void createAndConfigurePane() {
        int sceneWidth = (int) gp_boardGrid.getWidth();
        gp_boardGrid.setGridLinesVisible(true);
        gp_boardGrid.setStyle("-fx-background-color: #FAEBD7;");

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Stone stone = new Stone(i * boardSize + j, sceneWidth / boardSize / 2, Color.TRANSPARENT);
                stone.setStroke(Color.TRANSPARENT);
                stone.setOnMouseClicked(mouseEvent -> {
                    if (Color.TRANSPARENT.equals(stone.getFill())) {
                        if (zug % 2 == 0) {

                            stone.setFill(Color.BLACK);
                            model.setStone(stone.id, -1);
                        } else {
                            stone.setFill(Color.WHITE);
                            model.setStone(stone.id, 1);
                        }
                        System.out.println(model);
                        zug++;
                    }
                });
                gp_boardGrid.add(stone, j, i);
            }
        }
    }

    private void createAndLayoutControls() {
        //btn_cstones = new Button("CSTONES");
        //bigGrid.add(btn_cstones, 0, 0);
    }

    private void updateControllerFromListeners() {
        /*btn_cstones.setOnMouseClicked(mouseEvent -> {
            model.removeCatchedStones();
            gridReload();
        });*/

        gp_bigGrid.widthProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(gp_bigGrid.getHeight(), newVal.doubleValue());
            radius = radius / boardSize / 2;
            for (Node n : gp_boardGrid.getChildren()) {
                if (n instanceof Stone) ((Stone) n).setRadius(radius);
            }
        });

        gp_bigGrid.heightProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(gp_bigGrid.getWidth(), newVal.doubleValue());
            radius = radius / boardSize / 2;
            for (Node n : gp_boardGrid.getChildren()) {
                if (n instanceof Stone) ((Stone) n).setRadius(radius);
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
