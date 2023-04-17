package model;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class GoView extends Application {
    GoModel goModel;
    GridPane bigGrid;
    GridPane boardgrid;
    int boardSize;
    double komi;
    int handicap;
    int sceneHeight = 600;
    int sceneWight = 600;
    long zug = 0;

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setKomi(double komi) {
        this.komi = komi;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    @Override
    public void start(Stage stage) {
        System.out.println("board size: " + boardSize);
        System.out.println("handicap: " + handicap);
        System.out.println("komi: " + komi);

        goModel = new GoModel(boardSize);
        bigGrid = new GridPane();
        boardgrid = new GridPane();
        boardgrid.setGridLinesVisible(true);
        boardgrid.setStyle("-fx-background-color: #FAEBD7;");

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Stone stone = new Stone(i * boardSize + j, sceneWight / boardSize / 2, Color.TRANSPARENT);
                stone.setStroke(Color.TRANSPARENT);
                stone.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (Color.TRANSPARENT.equals(stone.getFill())) {
                            if (zug % 2 == 0){

                                stone.setFill(Color.BLACK);
                                goModel.setStone(stone.id, -1);
                            }
                            else {
                                stone.setFill(Color.WHITE);
                                goModel.setStone(stone.id, 1);
                            }
                            System.out.println(goModel);
                            zug++;
                        }
                    }
                });
                boardgrid.add(stone, j, i);
            }
        }

        Button button = new Button("CSTONES");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                goModel.removeCatchedStones();
                gridReload();
            }
        });
        bigGrid.add(button, 0, 0);
        bigGrid.add(boardgrid, 1, 0);

        // create the scene and add the grid to it
        Scene scene = new Scene(bigGrid, sceneHeight, sceneWight);
        stage.setScene(scene);
        stage.setTitle("Go Board");
        stage.show();

        // resize the circles when the grid is resized
        bigGrid.widthProperty().addListener((obs, oldVal, newVal) -> {
            double radius;
            if (bigGrid.getHeight() < newVal.doubleValue()) radius = bigGrid.getHeight();
            else radius = newVal.doubleValue();
            radius = radius / boardSize / 2;
            for (Node n : boardgrid.getChildren()) {
                if (n instanceof Stone) ((Stone) n).setRadius(radius);
            }
        });

        bigGrid.heightProperty().addListener((obs, oldVal, newVal) -> {
            double radius;
            if (bigGrid.getWidth() < newVal.doubleValue()) radius = bigGrid.getWidth();
            else radius = newVal.doubleValue();
            radius = radius / boardSize / 2;
            for (Node n : boardgrid.getChildren()) {
                if (n instanceof Stone) ((Stone) n).setRadius(radius);
            }
        });
    }

    private void gridReload(){
        for (Node n : boardgrid.getChildren()) {
            if (n instanceof Stone){
                Stone stone = (Stone) n;
                stone.setFill(goModel.getColorById(stone.id));
            }
        }
    }
}

