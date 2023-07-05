package model.gameStatistics;

import controller.guiComponents.Stone;
import model.GoModel;
import model.modelComponents.MoveList;
import model.modelComponents.Point;
import singleComponents.StoneColor;

import java.util.ArrayList;
import java.util.List;

public class GameEvalutation {
    private final StoneColor[][] board;
    private final MoveList moveList;
    private int blackPoints;
    private int whitePoints;

    public GameEvalutation(StoneColor[][] board, MoveList moveList){
        this.board = board;
        this.moveList = moveList;
        this.blackPoints = moveList.getLastMove().getBlackPoints();
        this.whitePoints = moveList.getLastMove().getWhitePoints();
    }

    public GameStatistics evaluateEndGameStatistics(){
        lifeOfTwoStones();
        checkAllNeutralAreas();
        return new GameStatistics(blackPoints, whitePoints);
    }

    private void lifeOfTwoStones(){
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if(board[x][y] != StoneColor.NEUTRAL){
                    checkSingleStoneGroupTwoStones(x,y);
                }
            }
        }
    }

    private void checkSingleStoneGroupTwoStones(int x, int y){
        List<Point> islandPoints = new ArrayList<>();
        GoModel.callBFS(GoModel.deepCopy(this.board), x, y, this.board[x][y], islandPoints);
        if(islandPoints.size() < 2){
            islandPoints.forEach(p -> this.board[p.x][p.y] = StoneColor.NEUTRAL);
        }
    }


    private void checkAllNeutralAreas(){
        StoneColor[][] dummyArray = GoModel.deepCopy(this.board);
        for (int x = 0; x < dummyArray.length; x++) {
            for (int y = 0; y < dummyArray[x].length; y++) {
                if(dummyArray[x][y] == StoneColor.NEUTRAL){
                    checkSingleNeutralArea(dummyArray, x, y);
                }
            }
        }
    }

    private void checkSingleNeutralArea(StoneColor[][] dummyArray, int x, int y){
        List<Point> neutralIsland = new ArrayList<>();
        callNeutralBFS(dummyArray, x, y, neutralIsland);

        boolean hasBlackStoneNeighbour = false;
        boolean hasWhiteStoneNeighbour = false;

        for(Point p : neutralIsland){
            if(hasColoredStoneNeighbour(dummyArray, p, StoneColor.BLACK)){
                hasBlackStoneNeighbour = true;
            }
            if(hasColoredStoneNeighbour(dummyArray, p, StoneColor.WHITE)){
                hasWhiteStoneNeighbour = true;
            }
        }

        if(hasBlackStoneNeighbour && !hasWhiteStoneNeighbour) blackPoints += neutralIsland.size();
        if(hasWhiteStoneNeighbour && !hasBlackStoneNeighbour) whitePoints += neutralIsland.size();

        neutralIsland.forEach(p -> dummyArray[p.x][p.y] = StoneColor.UNDEFINED);
    }

    private boolean hasColoredStoneNeighbour(StoneColor[][] dummyArray, Point p, StoneColor color){
        return  (isValidArrayCoord(p.x - 1, p.y, dummyArray) && dummyArray[p.x -1][p.y] == color) ||
                (isValidArrayCoord(p.x + 1, p.y, dummyArray) && dummyArray[p.x +1][p.y] == color) ||
                (isValidArrayCoord(p.x, p.y - 1, dummyArray) && dummyArray[p.x][p.y -1] == color) ||
                (isValidArrayCoord(p.x, p.y + 1, dummyArray) && dummyArray[p.x][p.y +1] == color);
    }

    private boolean isValidArrayCoord(int x, int y, StoneColor[][] array){
        return x >= 0 && x < array.length && y >= 0 && y < array[x].length;
    }

    public static void callNeutralBFS(StoneColor[][] grid, int x, int y, List<Point> neutralIsland) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[x].length || grid[x][y] != StoneColor.NEUTRAL){
            return;
        }
        neutralIsland.add(new Point(x, y));
        grid[x][y] = StoneColor.UNDEFINED;

        callNeutralBFS(grid, x + 1, y, neutralIsland);
        callNeutralBFS(grid, x - 1, y, neutralIsland);
        callNeutralBFS(grid, x, y + 1, neutralIsland);
        callNeutralBFS(grid, x, y - 1, neutralIsland);
    }

}
