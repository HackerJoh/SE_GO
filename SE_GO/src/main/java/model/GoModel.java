package model;

import javafx.scene.paint.Color;
import singleComponents.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoModel {
    private final StoneColor[][] boardArray;
    private final int size;
    private final MoveList moveList;
    private int noMoves;
    private boolean gameHasEnded = false;
    private double whitePoints;
    private double blackPoints;
    private boolean jumpModeOn = false;
    private int jumpCounter;

    private List<Point> islandPoints = new ArrayList<>();

    public GoModel(int size) {
        this.size = size;
        this.boardArray = getNeutralBoardArray();
        this.noMoves = 0;
        this.whitePoints = 0;
        this.blackPoints = 0;
        this.moveList = new MoveList();
    }

    public void setNoMoves(int noMoves) {
        this.noMoves = noMoves;
    }

    public StoneColor getTurn() {
        if (noMoves % 2 == 1) {
            return StoneColor.BLACK;
        } else {
            return StoneColor.WHITE;
        }
    }

    public int getSize() {
        return size;
    }

    public int getNoMoves() {
        return noMoves;
    }

    public void setWhitePoints(double whitePoints) {
        this.whitePoints = whitePoints;
    }

    public void setBlackPoints(double blackPoints) {
        this.blackPoints = blackPoints;
    }

    public boolean isGameHasEnded() {
        return gameHasEnded;
    }

    public void setGameHasEnded(boolean gameHasEnded) {
        this.gameHasEnded = gameHasEnded;
    }

    public void increaseTurn() {
        this.noMoves++;
    }

    /*protected void setStone(int id, StoneColor color) {
        turnOffJumpModeIfOn();
        int xCord = id / size;
        int yCord = id % size;
        boardArray[xCord][yCord] = color;
        SingleMove setMove;
        if (color == StoneColor.BLACK) {
            setMove = new SingleMove(StoneColor.BLACK, xCord, yCord, true);
        } else if (color == StoneColor.WHITE) {
            setMove = new SingleMove(StoneColor.WHITE, xCord, yCord, true);
        } else {
            setMove = new SingleMove(StoneColor.NEUTRAL, xCord, yCord, true);
        }
        //check after each move if somebody captured something / cought stones
        this.checkAllStonesIfTheyHaveLiberties(setMove);
    }*/

    public void controllerSetsStone(int xCoord, int yCoord, String description) {
        turnOffJumpModeIfOn();
        boardArray[xCoord][yCoord] = getTurn();
        SingleMove setMove;
        if (getTurn() == StoneColor.BLACK) {
            setMove = new SingleMove(getTurn(), xCoord, yCoord, true);
        } else if (getTurn() == StoneColor.WHITE) {
            setMove = new SingleMove(getTurn(), xCoord, yCoord, true);
        } else {
            setMove = new SingleMove(getTurn(), xCoord, yCoord, true);
        }
        increaseTurn();
        this.checkAllStonesIfTheyHaveLiberties(setMove, description);
    }

    public String getWinner(){
        setGameHasEnded(true);
        if (whitePoints > blackPoints) {
            return "WEIß gewinnt!";
        } else if (blackPoints > whitePoints) {
            return "SCHWARZ gewinnt!";
        } else {
            return "Unentschieden!";
        }
    }

    public String getSurrenderer(){
        setGameHasEnded(true);
        if (getTurn() == StoneColor.BLACK){
            return "WEIß gewinnt";
        } else {
            return "SCHWARZ gewinnt";
        }
    }

    public String getTurnColor(){
        if (getTurn() == StoneColor.BLACK) {
            return "SCHWARZ ist am Zug";
        } else {
            return "WEIß ist am Zug";
        }
    }

    public String getPassColor(){
        if (getTurn() == StoneColor.BLACK) {
            return "SCHWARZ passt";
        } else {
            return "WEIß passt";
        }
    }

    public void saveGame() {
        moveList.exportMoves("list.json");
    }

    public void loadGame(File loadedFile) throws IOException {
        //clearBoardArray();
        moveList.importMoves(loadedFile);
        for (SingleMove singleMove : moveList.getAllSingleMoves()) {
            if (singleMove.isSetStone()) {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = singleMove.getColor();
            } else {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = StoneColor.NEUTRAL;
            }
        }
        blackPoints = moveList.getLastMove().getBlackPoints();
        whitePoints = moveList.getLastMove().getWhitePoints();
    }

    private void checkAllStonesIfTheyHaveLiberties(SingleMove setMove, String description) {
        boolean isOnlySetMove = true;
        for (int x = 0; x < boardArray.length; x++) {
            for (int y = 0; y < boardArray[x].length; y++) {
                StoneColor color = boardArray[x][y];
                if (color != StoneColor.NEUTRAL) {
                    islandPoints = new ArrayList<>();
                    callBFS(deepCopy(this.boardArray), x, y, color);
                    //now i got island Points
                    int totalLiberties = 0;
                    for (Point p : islandPoints
                    ) {
                        //check Freiheiten
                        totalLiberties += getLiberties(p.x, p.y);
                    }
                    if (totalLiberties == 0) {
                        //remove group - got catched!
                        isOnlySetMove = false;
                        SingleMove[] move = new SingleMove[islandPoints.size() + 1];
                        move[0] = setMove;
                        int counter = 1;
                        for (Point p : islandPoints
                        ) {
                            //check Freiheiten
                            //int id = p.x * size + p.y; Unnötig?
                            this.removeStone(p.x, p.y);
                            move[counter] = new SingleMove(getTurn(), p.x, p.y, false);
                            counter++;
                            System.out.println(this);
                            if (getTurn() == StoneColor.WHITE) {
                                blackPoints++;
                            } else {
                                whitePoints++;
                            }
                        }
                        moveList.addMoveWithDescription(move, description, size, (int)blackPoints, (int)whitePoints);
                        //controller.gridReload();
                    }
                }
            }
        }
        if (isOnlySetMove) {
            moveList.addMoveWithDescription(new SingleMove[]{setMove}, description, size, (int)blackPoints, (int)whitePoints);
        }
    }

    private void callBFS(StoneColor[][] grid, int x, int y, StoneColor color) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[x].length || (grid[x][y] != color || grid[x][y] == StoneColor.NEUTRAL)) {
            return;
        }
        islandPoints.add(new Point(x, y));
        grid[x][y] = StoneColor.NEUTRAL; // Test mit neutral?

        callBFS(grid, x + 1, y, color);
        callBFS(grid, x - 1, y, color);
        callBFS(grid, x, y + 1, color);
        callBFS(grid, x, y - 1, color);
    }

    private int getLiberties(int x, int y) {
        int liberties = 0;
        // Check the four neighbors of the stone
        if (x > 0 && boardArray[x - 1][y] == StoneColor.NEUTRAL) {
            liberties++;
        }
        if (x < boardArray.length - 1 && boardArray[x + 1][y] == StoneColor.NEUTRAL) {
            liberties++;
        }
        if (y > 0 && boardArray[x][y - 1] == StoneColor.NEUTRAL) {
            liberties++;
        }
        if (y < boardArray[x].length - 1 && boardArray[x][y + 1] == StoneColor.NEUTRAL) {
            liberties++;
        }
        return liberties;
    }

    public static StoneColor[][] deepCopy(StoneColor[][] original) {
        if (original == null) {
            return null;
        }
        StoneColor[][] copy = new StoneColor[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    public StoneColor[][] getNeutralBoardArray() {
        StoneColor[][] newArray = new StoneColor[this.size][this.size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newArray[i][j] = StoneColor.NEUTRAL;
            }
        }
        return newArray;
    }

    public String toString() {
        StringBuilder out = new StringBuilder("[\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                out.append(boardArray[i][j]).append(" ");
            }
            out.append("\n");
        }
        return out + "]";
    }

    public void removeCatchedStones() {
        for (int i = 0; i < size * size; i++) {
            removeCatchedStone(i / size, i % size);
        }
        System.out.println(this);
    }

    public void removeCatchedStone(int xCoord, int yCoord) {
        StoneColor color = boardArray[xCoord][yCoord];
        if (yCoord > 0 && yCoord < size - 1 && xCoord > 0 && xCoord < size - 1) {
            if (color == StoneColor.WHITE && isCatchedWhiteInnerStone(xCoord, yCoord)) {
                removeStone(xCoord, yCoord);
                System.out.println(xCoord + " " + yCoord);
            }
            if (color == StoneColor.BLACK && isCatchedBlackInnerStone(xCoord, yCoord)) {
                removeStone(xCoord, yCoord);
                System.out.println(xCoord + " " + yCoord);
            }
        }
    }


    public boolean isCatchedWhiteInnerStone(int x, int y) {
        return (boardArray[x - 1][y] == StoneColor.BLACK && boardArray[x + 1][y] == StoneColor.BLACK && boardArray[x][y - 1] == StoneColor.BLACK && boardArray[x][y + 1] == StoneColor.BLACK);
    }

    public boolean isCatchedBlackInnerStone(int x, int y) {
        return (boardArray[x - 1][y] == StoneColor.WHITE && boardArray[x + 1][y] == StoneColor.WHITE && boardArray[x][y - 1] == StoneColor.WHITE && boardArray[x][y + 1] == StoneColor.WHITE);
    }

    public void removeStone(int xCoord, int yCoord) {
        boardArray[xCoord][yCoord] = StoneColor.NEUTRAL;
    }

    public Color getColorById(int id) {
        if (boardArray[id / size][id % size] == StoneColor.WHITE) return Color.WHITE;
        if (boardArray[id / size][id % size] == StoneColor.BLACK) return Color.BLACK;
        return Color.TRANSPARENT;
    }

    public boolean getUsedById(int id) {
        return (boardArray[id / size][id % size] != StoneColor.NEUTRAL);
    }

    public double getWhitePoints() {
        return whitePoints;
    }

    public double getBlackPoints() {
        return blackPoints;
    }

    public boolean turnOffJumpModeIfOn(){
        if(jumpModeOn) {
            turnOfJumpMode();
            return true;
        }
        return false;
    }

    private void turnOfJumpMode(){
        moveList.deleteMovesAfterIndex(jumpCounter);
        jumpModeOn = false;
    }

    public void enterJumpMode(){
        jumpModeOn = true;
        jumpCounter = moveList.getIndexOfLastMove();
    }

    public void jumpForward(){
        if(jumpModeOn){
            if(jumpCounter < moveList.getIndexOfLastMove()) doForwardJump();
        }
    }

    public void jumpBackward(){
        if(jumpModeOn){
            if(jumpCounter >= 0) doBackwardJump();
        }
    }

    private void doForwardJump(){
        jumpCounter++;
        Move forwardMove = moveList.getMoveByIndex(jumpCounter);
        for (SingleMove singleMove : forwardMove.getSingleMoves()) {
            if (singleMove.isSetStone()) {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = singleMove.getColor();
            } else {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = StoneColor.NEUTRAL;
            }
        }
        blackPoints = forwardMove.getBlackPoints();
        whitePoints = forwardMove.getWhitePoints();
    }

    private void doBackwardJump(){
        Move backwardMove = moveList.getMoveByIndex(jumpCounter);
        for (SingleMove singleMove : backwardMove.getSingleMoves()) {
            if (!singleMove.isSetStone()) {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = singleMove.getColor();
            } else {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = StoneColor.NEUTRAL;
            }
        }
        if(jumpCounter > 0) {
            jumpCounter--;
            Move newCurrentMove = moveList.getMoveByIndex(jumpCounter);
            blackPoints = newCurrentMove.getBlackPoints();
            whitePoints = newCurrentMove.getWhitePoints();
        }else{
            jumpCounter--;
            blackPoints = 0;
            whitePoints = 0;
        }
    }

    public String getDescriptionFromJump(){
        if (jumpCounter < 0){
            return "Beginn";
        }
        return moveList.getMoveByIndex(jumpCounter).getDescription();
    }
}
