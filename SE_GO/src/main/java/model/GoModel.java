package model;

import controller.BoardController;
import javafx.scene.paint.Color;
import singleComponents.MoveList;
import singleComponents.Point;
import singleComponents.StoneColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoModel {
    private final StoneColor[][] boardArray;
    private final int size;
    private final MoveList moveList;
    private int noMoves;
    private final BoardController controller;
    private boolean gameHasEnded = false;
    private boolean haveSurrendered = false;
    private double whitePoints;
    private double blackPoints;

    private List<Point> islandPoints = new ArrayList<>();

    public GoModel(int size, BoardController controller) {
        this.size = size;
        this.boardArray = fillBoardArrayNeutral();
        this.noMoves = 0;
        this.controller = controller;
        this.blackPoints = controller.getKomi();
        this.whitePoints = 0;
        this.moveList = new MoveList();
    }

    public StoneColor getTurn() {
        if (noMoves % 2 == 1) {
            return StoneColor.BLACK;
        } else {
            return StoneColor.WHITE;
        }
    }

    public void setStatusText() {
        if (!gameHasEnded) {
            if (getTurn() == StoneColor.BLACK) {
                controller.setStatusText("SCHWARZ ist am Zug");
            } else {
                controller.setStatusText("WEIß ist am Zug");
            }
        }
    }

    public void setStatusTextPassed() {
        if (!gameHasEnded) {
            noMoves++;
            if (getTurn() == StoneColor.BLACK) {
                controller.setStatusText("SCHWARZ passt");
            } else {
                controller.setStatusText("WEIß passt");
            }
        }
    }

    public void endGame() {
        if (haveSurrendered) {
            if (getTurn() == StoneColor.WHITE) {
                controller.setStatusText("WEIß gewinnt!");
            } else {
                controller.setStatusText("SCHWARZ gewinnt!");
            }
        } else if (whitePoints > blackPoints) {
            controller.setStatusText("WEIß gewinnt!");
        } else if (blackPoints > whitePoints) {
            controller.setStatusText("SCHWARZ gewinnt!");
        } else {
            controller.setStatusText("Unentschieden!");
        }
        controller.disableBtns();
        gameHasEnded = true;
    }

    public int getSize() {
        return size;
    }

    public long getNoMoves() {
        return noMoves;
    }

    public boolean isGameHasEnded() {
        return gameHasEnded;
    }

    public void setHaveSurrendered(boolean haveSurrendered) {
        this.haveSurrendered = haveSurrendered;
    }

    public void increaseTurn() {
        this.noMoves++;
    }

    public void setStone(int id, StoneColor color) {
        int xCord = id/size;
        int yCord = id%size;
        boardArray[xCord][yCord] = color;
        controller.setZug(0);
        setStatusText();
        if(color == StoneColor.BLACK) {
            moveList.addMove(StoneColor.BLACK, xCord, yCord);
        }else if(color == StoneColor.WHITE){
            moveList.addMove(StoneColor.WHITE, xCord, yCord);
        }else {
            moveList.addMove(StoneColor.NEUTRAL,xCord, yCord);
        }
        //check after each move if somebody captured something / cought stones
        this.checkAllStonesIfTheyHaveLiberties();
    }

    public void saveGame(){
        moveList.exportMoves("list.json");
    }

    private void checkAllStonesIfTheyHaveLiberties() {
        for (int x = 0; x < boardArray.length; x++) {
            for (int y = 0; y < boardArray[x].length; y++) {
                StoneColor color = boardArray[x][y];
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
                    for (Point p : islandPoints
                    ) {
                        //check Freiheiten
                        //int id = p.x * size + p.y; Unnötig?
                        this.removeStone(p.x, p.y);
                        System.out.println(this);
                        System.out.println("gefangen!");
                        if (getTurn() == StoneColor.WHITE) {
                            blackPoints++;
                        } else {
                            whitePoints++;
                        }
                    }
                    controller.gridReload();
                }
            }
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

    public StoneColor[][] fillBoardArrayNeutral() {
        StoneColor[][] newArray = new StoneColor[this.size][this.size];
        for(int i = 0; i < size ; i++){
            for (int j = 0; j < size; j++) {
                newArray[i][j] = StoneColor.NEUTRAL;
            }
        }
        return newArray;
    }


    public String toString() {
        String out = "[\n";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                out += boardArray[i][j] + " ";
            }
            out += "\n";
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
                System.out.println(xCoord +" "+ yCoord);
            }
            if (color == StoneColor.BLACK && isCatchedBlackInnerStone(xCoord, yCoord)) {
                removeStone(xCoord, yCoord);
                System.out.println(xCoord +" "+ yCoord);
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

    public boolean getUsedById(int id){
        return (boardArray[id / size][id % size] != StoneColor.NEUTRAL);
    }

    public double getWhitePoints() {
        return whitePoints;
    }

    public double getBlackPoints() {
        return blackPoints;
    }
}
