package model;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;


public class GoView {
    private final GoModel model;
    GridPane bigGrid;
    GridPane boardgrid;
    Button btn_cstones;
    int boardSize;
    int sceneWight = 600;
    long zug = 0;

    public GoView(GoModel model) {
        this.model = model;
        boardSize = model.getSize();

        createAndConfigurePane();
        createAndLayoutControls();
        updateControllerFromListeners();
    }

    private void createAndConfigurePane() {
        bigGrid = new GridPane();
        boardgrid = new GridPane();
        boardgrid.setGridLinesVisible(true);
        boardgrid.setStyle("-fx-background-color: #FAEBD7;");

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Stone stone = new Stone(i * boardSize + j, sceneWight / boardSize / 2, Color.TRANSPARENT);
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
                boardgrid.add(stone, j, i);
            }
        }
    }

    private void createAndLayoutControls() {
        btn_cstones = new Button("CSTONES");
        bigGrid.add(btn_cstones, 0, 0);
        bigGrid.add(boardgrid, 1, 0);
    }

    private void updateControllerFromListeners() {
        btn_cstones.setOnMouseClicked(mouseEvent -> {
            model.removeCatchedStones();
            gridReload();
        });

        bigGrid.widthProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(bigGrid.getHeight(), newVal.doubleValue());
            radius = radius / boardSize / 2;
            for (Node n : boardgrid.getChildren()) {
                if (n instanceof Stone) ((Stone) n).setRadius(radius);
            }
        });

        bigGrid.heightProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(bigGrid.getWidth(), newVal.doubleValue());
            radius = radius / boardSize / 2;
            for (Node n : boardgrid.getChildren()) {
                if (n instanceof Stone) ((Stone) n).setRadius(radius);
            }
        });
    }

    public Parent asParent() {
        return bigGrid;
    }

    private void gridReload() {
        for (Node n : boardgrid.getChildren()) {
            if (n instanceof Stone stone) {
                stone.setFill(model.getColorById(stone.id));
            }
        }
    }
}

