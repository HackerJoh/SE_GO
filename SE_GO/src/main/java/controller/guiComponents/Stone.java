package controller.guiComponents;

import controller.BoardController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import singleComponents.StoneColor;

public class Stone extends Circle {
    private static final Color hoverColorBlack = Color.gray(0.3, 0.7);
    private static final Color hoverColorWhite = new Color(1, 1, 1, 0.85);

    public int id;
    private boolean isUsed;
    private final BoardController controller;


    public Stone(int id, double radius, BoardController controller){
        super(radius, Color.TRANSPARENT);
        this.id = id;
        this.isUsed = false;
        addSetListener();
        addHoverListener();
        this.controller = controller;
    }

    private void addSetListener(){
        this.setStroke(Color.TRANSPARENT);
        this.setOnMouseClicked(mouseEvent -> {
            if(!isUsed && controller.isGameNotEnded()){
                controller.setStone(id/controller.getBoardSize(), id%controller.getBoardSize());
            }
        });
    }

    private void addHoverListener(){
        this.setOnMouseEntered(mouseEvent -> {
            if(!isUsed && controller.isGameNotEnded()){
                if(controller.getTurn() == StoneColor.BLACK){
                    this.setFill(hoverColorBlack);
                }else{
                    this.setFill(hoverColorWhite);
                }
            }
        });

        this.setOnMouseExited(mouseEvent -> {
            if(!isUsed){
                this.setFill(Color.TRANSPARENT);
            }
        });
    }

    public void setUsed(boolean isUsed){
        this.isUsed = isUsed;
    }
}
