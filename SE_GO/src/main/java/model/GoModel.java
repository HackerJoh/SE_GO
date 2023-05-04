package model;

import javafx.scene.paint.Color;

public class GoModel {
    private final int[][] boardArray;
    private final int size;
    private long moves;

    public GoModel(int size) {
        boardArray = new int[size][size];
        this.size = size;
        this.moves = 0;
    }

    /**
     * Getter for the size of the Go-Board
     * @return : The size of the Go-Board (6 --> 6x6)
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the Counter for the 
     * @return an long with the current move
     */
    public long getMoveCounter(){return moves;}

    /**
     * Increases
     */
    public void increaseMoves(){
        this.moves++;
    }

    /**
     *
     * @param id is the ID of the Stone, outside the Model class it's one number, here it's converted for the
     *           two-dimensional Array
     * @param color is the Color of the Stone reprented as an Integer -1 is Black, 1 is White and 0 is Transparent
     */
    public void setStone(int id, int color) {
        boardArray[id / size][id % size] = color;
    }

    /**
     * Creates an Illustration from the current Go-Board as an String
     * @return An String which illustrates a two-dimensional Array
     */
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


    public boolean isCatchedWhiteInnerStone(int y, int x) {
        return (boardArray[y - 1][x] == -1 && boardArray[y + 1][x] == -1 && boardArray[y][x - 1] == -1 && boardArray[y][x + 1] == -1);
    }

    public boolean isCatchedBlackInnerStone(int y, int x) {
        return (boardArray[y - 1][x] == 1 && boardArray[y + 1][x] == 1 && boardArray[y][x - 1] == 1 && boardArray[y][x + 1] == 1);
    }


    /**
     * Sets the Color of the Stone to Transparent --> 0
     * @param id it's a one Integer ID, which is becomes the Coordinates for the two-dimensional array
     */
    public void removeStone(int id) {
        boardArray[id / size][id % size] = 0;
    }


    /**
     * Getter for the Color of the Stone in the Model Array
     * @param id it's a one Integer ID, which is becomes the Coordinates for the two-dimensional array
     * @return a Color object, which represents the color of the position in the Array
     */
    public Color getColorById(int id) {
        if (boardArray[id / size][id % size] > 0) return Color.WHITE;
        if (boardArray[id / size][id % size] < 0) return Color.BLACK;
        return Color.TRANSPARENT;
    }
}
