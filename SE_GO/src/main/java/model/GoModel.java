package model;

import javafx.scene.paint.Color;

public class GoModel {
    private final int[][] boardArray;
    private final int size;

    public GoModel(int size) {
        boardArray = new int[size][size];
        this.size = size;
    }

    public void setStone(int id, int color) {
        boardArray[id / size][id % size] = color;
    }

    public String toString(){
        String out = "[\n";
        for(int i = 0; i< size; i++){
            for(int j = 0; j< size; j++){
                out += boardArray[i][j] + " ";
            }
            out += "\n";
        }
        return out + "]";
    }

    public void removeCatchedStones(){
        for(int i = 0; i< size*size; i++){
            removeCatchedStone(i);
        }
        System.out.println(this);
    }

    public void removeCatchedStone(int id){
        int color = boardArray[id/size][id%size];
        if(id%size > 0 && id%size < size-1 && id/size > 0 && id/size < size -1){
            if(color == 1 && isCatchedWhiteInnerStone(id/size, id%size)){
                removeStone(id);
                System.out.println(id);
            }
            if(color == -1 && isCatchedBlackInnerStone(id/size, id%size)){
                removeStone(id);
                System.out.println(id);
            }
        }
    }

    /**
     *
     * @param y
     * @param x
     * @return
     */
    public boolean isCatchedWhiteInnerStone(int y, int x){
        return (boardArray[y-1][x] == -1 && boardArray[y+1][x] == -1 && boardArray[y][x-1] == -1 && boardArray[y][x+1] == -1);
    }

    public boolean isCatchedBlackInnerStone(int y, int x){
        return (boardArray[y-1][x] == 1 && boardArray[y+1][x] == 1 && boardArray[y][x-1] == 1 && boardArray[y][x+1] == 1);
    }

    public void removeStone(int id){
        boardArray[id/size][id%size] = 0;
    }


    public Color getColorById(int id){
        if(boardArray[id/size][id%size] > 0) return Color.WHITE;
        if(boardArray[id/size][id%size] < 0) return Color.BLACK;
        return Color.TRANSPARENT;
    }
}
