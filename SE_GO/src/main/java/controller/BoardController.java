package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.GoModel;
import model.Stone;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    private int boardSize = 13;
    private GoModel model;

    @FXML
    private GridPane gp_bigGrid;

    @FXML
    private GridPane gp_boardGrid;

    @FXML
    private VBox vbx_sideMenu;

    private int sceneWidth;
    int zug = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new GoModel(13);
        createAndConfigurePane();
        createAndLayoutControls();
        updateControllerFromListeners();
    }

    private void createAndConfigurePane() {
        sceneWidth = 438;
        gp_boardGrid.setGridLinesVisible(true);
        gp_boardGrid.setStyle("-fx-background-color: #FAEBD7;");
        //gp_bigGrid.add(vbx_sideMenu, 0, 0);

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
