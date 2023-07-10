package model.modelComponents;

public class Move {
    private String description;
    private SingleMove[] singleMoves;
    private double blackPoints;
    private double whitePoints;

    public Move() {
    }

    public Move(SingleMove[] singleMoves, double blackPoints, double whitePoints) {
        description = "";
        this.singleMoves = singleMoves;
        this.blackPoints = blackPoints;
        this.whitePoints = whitePoints;
    }

    public Move(SingleMove[] singleMoves, String description, double blackPoints, double whitePoints) {
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

    public double getBlackPoints() {
        return blackPoints;
    }

    public double getWhitePoints() {
        return whitePoints;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (SingleMove singleMove : singleMoves) out.append(singleMove.getColor()).append(" ");
        return out.toString();
    }
}
