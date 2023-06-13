package singleComponents;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.GoModel;

public class Stone extends Circle {
    private static final Color hoverColorBlack = Color.gray(0.3, 0.7);
    private static final Color hoverColorWhite = new Color(1, 1, 1, 0.85);

    public int id;
    private GoModel model;
    private boolean isUsed;


    public Stone(int id, double radius, GoModel model){
        super(radius, Color.TRANSPARENT);
        this.id = id;
        this.isUsed = false;
        this.model = model;
        addSetListener();
        addHoverListener();
    }

    public Stone(int id, int radius, Color c){
        super(radius, c);
        this.id = id;
    }

    private void addSetListener(){
        this.setStroke(Color.TRANSPARENT);
        this.setOnMouseClicked(mouseEvent -> {
            if(!isUsed && !model.isGameHasEnded()){
                if(model.getNoMoves() % 2 == 0){
                    this.setFill(Color.BLACK);
                    model.setStone(this.id, StoneColor.BLACK);
                }else {
                    this.setFill(Color.WHITE);
                    model.setStone(this.id, StoneColor.WHITE);
                }
                model.increaseTurn();
                this.isUsed = true;
            }
        });
    }

    private void addHoverListener(){
        this.setOnMouseEntered(mouseEvent -> {
            if(!isUsed && !model.isGameHasEnded()){
                if(model.getNoMoves() % 2 == 0){
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
