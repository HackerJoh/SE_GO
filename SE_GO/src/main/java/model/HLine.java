package model;

import javafx.scene.shape.Line;

public class HLine extends Line {
    private final int id;

    public HLine(int id, double startX, double endX){
        super(startX, 0, endX, 0);
        this.id = id;
    }

    public int getGoLineId() {
        return id;
    }
}
