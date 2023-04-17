package com.example.se_go;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Stone extends Circle {
    int id;
    public Stone(int id, int radius, Color c){
        super(radius, c);
        this.id = id;
    }
}
