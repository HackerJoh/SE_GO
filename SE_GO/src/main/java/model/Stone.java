package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Stone extends Circle {
    private static Color hoverColorBlack = Color.gray(0.3, 0.7);
    private static Color hoverColorWhite = Color.gray(0.8, 0.5);

    int id;
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

    private void addSetListener(){
        this.setStroke(Color.TRANSPARENT);
        this.setOnMouseClicked(mouseEvent -> {
            if(!isUsed){
                if(model.getZug() % 2 == 0){
                    this.setFill(Color.BLACK);
                    model.setStone(this.id, -1);
                }else {
                    this.setFill(Color.WHITE);
                    model.setStone(this.id, 1);
                }
                model.increaseZug();
                this.isUsed = true;
            }
        });
    }

    private void addHoverListener(){
        this.setOnMouseEntered(mouseEvent -> {
            if(!isUsed){
                if(model.getZug() % 2 == 0){
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
}
