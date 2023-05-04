package model;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;


public class GoView {
    private final GoModel model;
    GridPane bigGrid;
    GridPane boardgrid;
    int boardSize;
    int sceneWight = 600;
    double stoneRatio = 0.8;

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
        boardgrid.setGridLinesVisible(false);
        boardgrid.setStyle("-fx-background-color: #FAEBD7;");


        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (49+(i%9));
            Text text = new Text(Character.toString(vertical));
            boardgrid.add(text, 0, i+1);
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65+i);
            Text text = new Text(Character.toString(vertical));
            boardgrid.add(text, i+1, 0);
        }

        for (int i = 0; i < boardSize ; i++) {
            for (int j = 0; j < boardSize; j++) {
                int id = i * boardSize + j;
                Group group = new Group();
                int stoneRadius = sceneWight / (boardSize +2) / 2;
                Stone stone = new Stone(id, stoneRadius * stoneRatio, model);
                HLine newLineH;
                VLine newLineV;

                if(j == 0 ){
                    newLineH = new HLine(id,0, stoneRadius);
                }else if(j == boardSize -1){
                    newLineH = new HLine(id,-stoneRadius, 0);
                }else {
                    newLineH = new HLine(id, -stoneRadius, stoneRadius);
                }

                if(i == 0 ){
                    newLineV = new VLine(id, 0, stoneRadius);
                }else if(i == boardSize-1){
                    newLineV = new VLine(id,-stoneRadius, 0);
                }else{
                    newLineV = new VLine(id, -stoneRadius, stoneRadius);
                }
                group.getChildren().add(newLineH);
                group.getChildren().add(newLineV);
                group.getChildren().add(stone);
                boardgrid.add(group, j+1, i+1);
            }
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (49+(i%9));
            Text text = new Text(Character.toString(vertical));
            boardgrid.add(text, boardSize +1 , i+1);
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65+i);
            Text text = new Text(Character.toString(vertical));
            boardgrid.add(text, i+1, boardSize + 1);
        }
    }

    private void createAndLayoutControls() {
        bigGrid.add(boardgrid, 1, 0);
        //btn_cstones.setMinWidth(100);
    }

    private void updateControllerFromListeners() {
        /*btn_cstones.setOnMouseClicked(mouseEvent -> {
            model.removeCatchedStones();
            gridReload();
        });*/

        bigGrid.widthProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(bigGrid.getHeight(), newVal.doubleValue());
            radius = radius / (boardSize +2) / 2;
            for (Node n : boardgrid.getChildren()) {
                if(n instanceof Group){
                    for(Node n2: ((Group) n).getChildren()){
                        if(n2 instanceof Stone) ((Stone) n2).setRadius(radius*stoneRatio);
                        if(n2 instanceof HLine){
                            HLine l = (HLine) n2;
                            if(l.getGoLineId() % boardSize == 0){
                                l.setEndX(radius);
                            }else if(l.getGoLineId() % boardSize == boardSize -1){
                                l.setStartX(-radius);
                            }else{
                                l.setStartX(-radius);
                                l.setEndX(radius);
                            }
                        }
                    }
                }
            }
        });

        bigGrid.heightProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(bigGrid.getWidth(), newVal.doubleValue());
            radius = radius / (boardSize+2) / 2;
            for (Node n : boardgrid.getChildren()) {
                if(n instanceof Group){
                    for(Node n2: ((Group) n).getChildren()){
                        if(n2 instanceof Stone) ((Stone) n2).setRadius(radius*stoneRatio);
                        if(n2 instanceof VLine){
                            VLine l = (VLine) n2;
                            if(l.getGoLineId() / boardSize == 0){
                                l.setEndY(radius);
                            }else if(l.getGoLineId() / boardSize == boardSize -1){
                                l.setStartY(-radius);
                            }else{
                                l.setStartY(-radius);
                                l.setEndY(radius);
                            }

                        }
                    }
                }
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

    public GridPane getBoardgrid() {
        return boardgrid;
    }
}

