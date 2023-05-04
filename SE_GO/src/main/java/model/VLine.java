package model;

import javafx.scene.shape.Line;

public class VLine extends Line {
    private final int id;

    public VLine(int id, double startY, double endY){
        super(0, startY, 0, endY);
        this.id = id;
    }

    public int getGoLineId() {
        return id;
    }
}
