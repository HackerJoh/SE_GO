package model;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;


public class GoView {
    private final GoModel model;
    GridPane bigGrid;
    GridPane boardgrid;
    int boardSize;
    int sceneWight = 600;

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
            for (int j = 0; j < boardSize; j++) {
                int id = i * boardSize + j;
                Group group = new Group();
                Stone stone = new Stone(id, sceneWight / boardSize / 2, model);
                GoLine newLineH;
                GoLine newLineV;

                if(j == 0 ){
                    newLineH = new GoLine(id,0, 0, sceneWight/boardSize/2, 0);
                }else if(j == boardSize -1){
                    newLineH = new GoLine(id,-sceneWight/boardSize/2, 0, 0, 0);
                }else {
                    newLineH = new GoLine(id, -sceneWight/boardSize/2, 0, sceneWight/boardSize/2, 0);
                }

                if(i == 0 ){
                    newLineV = new GoLine(id, 0, 0, 0, sceneWight/boardSize/2);
                }else if(i == boardSize-1){
                    newLineV = new GoLine(id,0, -sceneWight/boardSize/2, 0, 0);
                }else{
                    newLineV = new GoLine(id,0, -sceneWight/boardSize/2, 0, sceneWight/boardSize/2);
                }

                group.getChildren().add(stone);
                group.getChildren().add(newLineH);
                group.getChildren().add(newLineV);
                boardgrid.add(group, j, i);
            }
        }
    }

    private void createAndLayoutControls() {
        //btn_cstones = new Button("CSTONES");
        //bigGrid.add(btn_cstones, 0, 0);
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
            radius = radius / boardSize / 2;
            for (Node n : boardgrid.getChildren()) {
                if(n instanceof Group){
                    for(Node n2: ((Group) n).getChildren()){
                        if(n2 instanceof Stone) ((Stone) n2).setRadius(radius);
                        /*if(n2 instanceof GoLine){
                            Line l = (GoLine) n2;
                            if(l.getEndY() == 0 && l.getStartY() == 0){
                                if(l.getStartX() == 0){
                                    l.setEndX(radius);
                                }else if(l.getEndX() == 0){
                                    l.setStartX(-radius);
                                }else{

                                    l.setStartX(-radius);
                                    l.setEndX(radius);
                                }
                            }
                        }*/
                    }
                }
            }
        });

        bigGrid.heightProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(bigGrid.getWidth(), newVal.doubleValue());
            radius = radius / boardSize / 2;
            for (Node n : boardgrid.getChildren()) {
                if(n instanceof Group){
                    for(Node n2: ((Group) n).getChildren()){
                        if(n2 instanceof Stone) ((Stone) n2).setRadius(radius);
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

