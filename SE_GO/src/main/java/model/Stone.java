package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Stone extends Circle {
    public int id;
    public Stone(int id, int radius, Color c){
        super(radius, c);
        this.id = id;
    }

}
