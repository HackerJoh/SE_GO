package singleComponents;

public class Move {
    private String description;
    private int boardSize;
    private SingleMove[] singleMoves;
    private int blackPoints;
    private int whitePoints;

    public Move() {
    }

    public Move(SingleMove[] singleMoves, int boardSize, int blackPoints, int whitePoints) {
        description = "";
        this.singleMoves = singleMoves;
        this.boardSize = boardSize;
        this.blackPoints = blackPoints;
        this.whitePoints = whitePoints;
    }

    public Move(SingleMove[] singleMoves, String description, int boardSize, int blackPoints, int whitePoints) {
        this.description = description;
        this.singleMoves = singleMoves;
        this.boardSize = boardSize;
        this.blackPoints = blackPoints;
        this.whitePoints = whitePoints;
    }

    public String getDescription() {
        return description;
    }

    public SingleMove[] getSingleMoves() {
        return singleMoves;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
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
