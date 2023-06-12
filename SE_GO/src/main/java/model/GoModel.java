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
    private final int[][] boardArray; //TODO: Stone-Array
    private final int size;
    private final MoveList moveList;
    private int noMoves;
    private BoardController controller;
    private boolean gameHasEnded = false;
    private boolean haveSurrendered = false;
    private double whitePoints;
    private double blackPoints;

    private List<Point> islandPoints = new ArrayList<Point>();

    public GoModel(int size, BoardController controller) {
        boardArray = new int[size][size];
        this.size = size;
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

    public void setStone(int id, int color) {
        boardArray[id / size][id % size] = color;
        int x = id / size;
        int y = id % size;  //TODO: ID in x und y splitten
        controller.setZug(0);
        setStatusText();
        if(color < 0) {
            moveList.addMove(StoneColor.BLACK, x, y);
        }else if(color > 0){
            moveList.addMove(StoneColor.WHITE, x, y);
        }else {
            moveList.addMove(StoneColor.NEUTRAL,x, y);
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
                int color = boardArray[x][y];
                islandPoints = new ArrayList<Point>();
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
                        int id = p.x * size + p.y;
                        this.removeStone(id);
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

    private void callBFS(int[][] grid, int x, int y, int color) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[x].length || (grid[x][y] == -color || grid[x][y] == 0)) {
            return;
        }
        islandPoints.add(new Point(x, y));
        grid[x][y] = -color;
        callBFS(grid, x + 1, y, color);
        callBFS(grid, x - 1, y, color);
        callBFS(grid, x, y + 1, color);
        callBFS(grid, x, y - 1, color);
    }

    private int getLiberties(int x, int y) {
        int liberties = 0;
        // Check the four neighbors of the stone
        if (x > 0 && boardArray[x - 1][y] == 0) {
            liberties++;
        }
        if (x < boardArray.length - 1 && boardArray[x + 1][y] == 0) {
            liberties++;
        }
        if (y > 0 && boardArray[x][y - 1] == 0) {
            liberties++;
        }
        if (y < boardArray[x].length - 1 && boardArray[x][y + 1] == 0) {
            liberties++;
        }
        return liberties;
    }

    public static int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
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
            removeCatchedStone(i);
        }
        System.out.println(this);
    }

    public void removeCatchedStone(int id) {
        int color = boardArray[id / size][id % size];
        if (id % size > 0 && id % size < size - 1 && id / size > 0 && id / size < size - 1) {
            if (color == 1 && isCatchedWhiteInnerStone(id / size, id % size)) {
                removeStone(id);
                System.out.println(id);
            }
            if (color == -1 && isCatchedBlackInnerStone(id / size, id % size)) {
                removeStone(id);
                System.out.println(id);
            }
        }
    }

    /**
     * @param y
     * @param x
     * @return
     */
    public boolean isCatchedWhiteInnerStone(int y, int x) {
        return (boardArray[y - 1][x] == -1 && boardArray[y + 1][x] == -1 && boardArray[y][x - 1] == -1 && boardArray[y][x + 1] == -1);
    }

    public boolean isCatchedBlackInnerStone(int y, int x) {
        return (boardArray[y - 1][x] == 1 && boardArray[y + 1][x] == 1 && boardArray[y][x - 1] == 1 && boardArray[y][x + 1] == 1);
    }

    public void removeStone(int id) {
        boardArray[id / size][id % size] = 0;
    }


    public Color getColorById(int id) {
        if (boardArray[id / size][id % size] > 0) return Color.WHITE;
        if (boardArray[id / size][id % size] < 0) return Color.BLACK;
        return Color.TRANSPARENT;
    }

    public boolean getUsedById(int id){
        return (boardArray[id / size][id % size] != 0);
    }

    public double getWhitePoints() {
        return whitePoints;
    }

    public double getBlackPoints() {
        return blackPoints;
    }
}
