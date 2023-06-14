package singleComponents;

public class SingleMove {
    private final StoneColor color;
    private final int xCoord;
    private final int yCoord;
    private final boolean setStone;

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
}
