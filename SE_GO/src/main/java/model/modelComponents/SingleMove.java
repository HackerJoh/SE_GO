package model.modelComponents;

import singleComponents.StoneColor;

public class SingleMove {
    private StoneColor color;
    private  int xCoord;
    private  int yCoord;
    private  boolean setStone;

    public SingleMove(){}

    public SingleMove(StoneColor color, int xCoord, int yCoord, boolean setStone){
        this.color = color;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.setStone = setStone;
    }

    public StoneColor getColor() {
        return color;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public boolean isSetStone() {
        return setStone;
    }

    public void setColor(StoneColor color) {
        this.color = color;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public void setSetStone(boolean setStone) {
        this.setStone = setStone;
    }

    public String toString(){
        return color.toString();
    }
}
