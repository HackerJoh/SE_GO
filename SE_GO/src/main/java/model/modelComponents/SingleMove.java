package model.modelComponents;

import singleComponents.StoneColor;

/**
 * Class which stores the set/delete of a Single Stone in a Move
 */
public class SingleMove {
    private StoneColor color;
    private  int xCoord;
    private  int yCoord;
    private  boolean isSetStone;

    public SingleMove(){}

    public SingleMove(StoneColor color, int xCoord, int yCoord, boolean isSetStone){
        this.color = color;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.isSetStone = isSetStone;
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
        return isSetStone;
    }

    //Setter are needed for the Jackson Object Mapper (they are necassary)
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
        this.isSetStone = setStone;
    }

    public String toString(){
        return color.toString();
    }
}
