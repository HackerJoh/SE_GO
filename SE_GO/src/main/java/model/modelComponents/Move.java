package model.modelComponents;

public class Move {
    private String description;
    private SingleMove[] singleMoves;
    private int blackPoints;
    private int whitePoints;

    public Move() {
    }

    public Move(SingleMove[] singleMoves, int blackPoints, int whitePoints) {
        description = "";
        this.singleMoves = singleMoves;
        this.blackPoints = blackPoints;
        this.whitePoints = whitePoints;
    }

    public Move(SingleMove[] singleMoves, String description, int blackPoints, int whitePoints) {
        this.description = description;
        this.singleMoves = singleMoves;
        this.blackPoints = blackPoints;
        this.whitePoints = whitePoints;
    }

    public String getDescription() {
        return description;
    }

    public SingleMove[] getSingleMoves() {
        return singleMoves;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSingleMoves(SingleMove[] singleMoves) {
        this.singleMoves = singleMoves;
    }

    public void setBlackPoints(int blackPoints) {
        this.blackPoints = blackPoints;
    }

    public void setWhitePoints(int whitePoints) {
        this.whitePoints = whitePoints;
    }

    public int getBlackPoints() {
        return blackPoints;
    }

    public int getWhitePoints() {
        return whitePoints;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (SingleMove singleMove : singleMoves) out.append(singleMove.getColor()).append(" ");
        return out.toString();
    }
}
