package com.example.se_go;

import javafx.scene.paint.Color;

public class GoModel {
    private int[][] boardArray;
    private int size;

    GoModel(int size){
        boardArray = new int[size][size];
        this.size = size;
    }

    public void setStone(int id, int color){
        boardArray[id/size][id%size] = color;
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
        for(int i = 0; i< size*size; i++) removeCatchedStone(i);
        System.out.println(this);
    }

    public void removeCatchedStone(int id){
        int color = boardArray[id/size][id%size];
        if(id%size > 0 && id%size < size-1 && id/size > 0 && id/size < size -1){
            if(color == 1) removeWhiteInnerStone(id/size, id%size);
        }
    }

    public void removeWhiteInnerStone(int x, int y){
        if (boardArray[y-1][x] == -1 && boardArray[y+1][x] == -1 && boardArray[y][x-1] == -1 && boardArray[y][x+1] == -1) {
            boardArray[y][x] = 0;
        }
    }

    public Color getColorById(int id){
        if(boardArray[id/size][id%size] > 0) return Color.WHITE;
        if(boardArray[id/size][id%size] < 0) return Color.BLACK;
        return Color.TRANSPARENT;
    }
}
