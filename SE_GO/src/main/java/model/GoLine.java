package model;

import javafx.scene.shape.Line;

public class GoLine extends Line {
    private final int id;

    public GoLine(int id, double startX, double startY, double endX, double endY){
        super(startX, startY, endX, endY);
        this.id = id;
    }

    public int getGoLineId() {
        return id;
    }
}
