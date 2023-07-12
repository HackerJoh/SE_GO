package model.modelComponents;

/**
 * Class represents the Coordinates of a stone.
 * Is needed for example in the calculation of Stonegroups
 */
public class Point {
    public int x,y;

    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    public String toString(){
        return x+":"+y+" ";
    }
}
