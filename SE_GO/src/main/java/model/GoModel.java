package model;

import javafx.scene.paint.Color;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoModel {
    private final int[][] boardArray;

    private List<Point> islandPoints = new ArrayList<Point>();
    private final int size;
    private long zug;

    public GoModel(int size) {
        boardArray = new int[size][size];

        this.size = size;
        this.zug = 0;
    }

    public int getSize() {
        return size;
    }

    public long getZug(){return zug;}

    public void increaseZug(){
        this.zug++;
    }

    public void setStone(int id, int color) {
        boardArray[id / size][id % size] = color;
        int x = id / size;
        int y= id % size;
        //check after each move if somebody captured something / cought stones
        this.checkAllStonesIfTheyHaveLiberties();
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
        //System.out.println(this);
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

    private void checkAllStonesIfTheyHaveLiberties() {
        for(int x = 0; x<boardArray.length; x++){
            for(int y=0; y<boardArray[x].length; y++){
                int color = boardArray[x][y];

                islandPoints=new ArrayList<Point>();

                callBFS(deepCopy(this.boardArray),x,y,color);
                //now i got island Points
                int totalLiberties=0;
                for (Point p:islandPoints
                ) {
                    //check Freiheiten
                    totalLiberties+=getLiberties(p.x,p.y);
                }
                if(totalLiberties==0){
                    //remove group - got catched!
                    for (Point p:islandPoints
                    ) {
                        //check Freiheiten
                        int id = p.x * size + p.y;
                        this.removeStone(id);
                    }
                }
            }
        }
    }
    private void callBFS(int [][] grid, int x ,int y, int color){
        if(x<0 || x >=grid.length || y<0 || y>=grid[x].length || (grid[x][y]==-color ||grid[x][y] == 0)){
            return;
        }
        islandPoints.add(new Point(x,y));
        grid[x][y]= -color;
        callBFS(grid,x+1,y,color);
        callBFS(grid,x-1,y,color);
        callBFS(grid,x,y+1,color);
        callBFS(grid,x,y-1,color);

    }
    private int getLiberties(int x, int y) {
        int liberties = 0;

        // Check the four neighbors of the stone
        if (x > 0 && boardArray[x-1][y] == 0) {
            liberties++;
        }
        if (x < boardArray.length - 1 && boardArray[x+1][y] == 0) {
            liberties++;
        }
        if (y > 0 && boardArray[x][y-1] == 0) {
            liberties++;
        }
        if (y < boardArray[x].length - 1 && boardArray[x][y+1] == 0) {
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
        System.out.println("Removed stone at: "+id / size+" ; "+id % size);
        boardArray[id / size][id % size] = 0;
    }


    public Color getColorById(int id) {
        if (boardArray[id / size][id % size] > 0) return Color.WHITE;
        if (boardArray[id / size][id % size] < 0) return Color.BLACK;
        return Color.TRANSPARENT;
    }
}
