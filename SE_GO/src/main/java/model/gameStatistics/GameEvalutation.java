package model.gameStatistics;

import model.GoModel;
import model.modelComponents.MoveList;
import model.modelComponents.Point;
import singleComponents.EndgameColors;
import singleComponents.StoneColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for Evaluating the EndGame Statistics
 */
public class GameEvalutation {
    private final StoneColor[][] board;
    private final MoveList moveList;
    private double blackPoints;
    private double whitePoints;
    private final EndgameColors[][] endBoard;

    public GameEvalutation(StoneColor[][] board, MoveList moveList){
        this.board = board;
        this.moveList = moveList;
        this.blackPoints = moveList.getLastMove().getBlackPoints();
        this.whitePoints = moveList.getLastMove().getWhitePoints();
        this.endBoard = new EndgameColors[board.length][board.length];
    }

    public GameStatistics evaluateEndGameStatistics(){
        lifeOfTwoStones();
        syncEndBoard();
        checkAllNeutralAreas();
        int whiteMoves = countMovesByColor(StoneColor.WHITE);
        int blackMoves = countMovesByColor(StoneColor.BLACK);
        int capturedWhiteStones = countCapturedStoneByColor(StoneColor.WHITE);
        int capturedBlackStones = countCapturedStoneByColor(StoneColor.BLACK);
        StoneColor winner;
        if (whitePoints > blackPoints) {
            winner = StoneColor.WHITE;
        } else if (blackPoints > whitePoints) {
            winner = StoneColor.BLACK;
        } else {
            winner = StoneColor.NEUTRAL;
        }
        return new GameStatistics(blackPoints, whitePoints, blackMoves, whiteMoves, capturedBlackStones, capturedWhiteStones, winner, endBoard);
    }

    private void syncEndBoard(){
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if(board[x][y] == StoneColor.BLACK) endBoard[x][y] = EndgameColors.BLACK;
                if(board[x][y] == StoneColor.WHITE) endBoard[x][y] = EndgameColors.WHITE;
                if(board[x][y] == StoneColor.NEUTRAL) endBoard[x][y] = EndgameColors.NEUTRAL;
            }
        }
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
        callBFS(GoModel.deepCopy(this.board), x, y, this.board[x][y], islandPoints);
        if(islandPoints.size() < 2){
            islandPoints.forEach(p -> this.board[p.x][p.y] = StoneColor.NEUTRAL);
        }
    }

    public void callBFS(StoneColor[][] grid, int x, int y, StoneColor color, List<Point> islandPoints) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[x].length || (grid[x][y] != color || grid[x][y] == StoneColor.NEUTRAL)) {
            return;
        }
        islandPoints.add(new Point(x, y));
        grid[x][y] = StoneColor.NEUTRAL; // Test mit neutral?

        callBFS(grid, x + 1, y, color, islandPoints);
        callBFS(grid, x - 1, y, color, islandPoints);
        callBFS(grid, x, y + 1, color, islandPoints);
        callBFS(grid, x, y - 1, color, islandPoints);
        callBFS(grid, x + 1, y + 1, color, islandPoints);
        callBFS(grid, x + 1, y -1, color, islandPoints);
        callBFS(grid, x - 1, y + 1, color, islandPoints);
        callBFS(grid, x - 1, y - 1, color, islandPoints);
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

        if(hasBlackStoneNeighbour && !hasWhiteStoneNeighbour){
            blackPoints += neutralIsland.size();
            neutralIsland.forEach(p -> endBoard[p.x][p.y] = EndgameColors.BLACKAREA);
        }
        if(hasWhiteStoneNeighbour && !hasBlackStoneNeighbour){
            whitePoints += neutralIsland.size();
            neutralIsland.forEach(p -> endBoard[p.x][p.y] = EndgameColors.WHITEAREA);
        }
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

    private static void callNeutralBFS(StoneColor[][] grid, int x, int y, List<Point> neutralIsland) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[x].length || (grid[x][y] != StoneColor.NEUTRAL || grid[x][y] == StoneColor.UNDEFINED)){
            return;
        }
        neutralIsland.add(new Point(x, y));
        grid[x][y] = StoneColor.UNDEFINED;

        callNeutralBFS(grid, x + 1, y, neutralIsland);
        callNeutralBFS(grid, x - 1, y, neutralIsland);
        callNeutralBFS(grid, x, y + 1, neutralIsland);
        callNeutralBFS(grid, x, y - 1, neutralIsland);
    }

    private int countMovesByColor(StoneColor color){
        return (int)Arrays.stream(moveList.getAllSingleMoves()).filter(m -> m.isSetStone() && m.getColor() == color).count();
    }

    private int countCapturedStoneByColor(StoneColor color){
        return (int)Arrays.stream(moveList.getAllSingleMoves()).filter(m -> !m.isSetStone() && m.getColor() == color).count();
    }

    @Override
    public String toString(){
        StringBuilder out = new StringBuilder("GameStats\n----------------------------------\n");
        out.append("BlackPoints: ").append(blackPoints).append("\n");
        out.append("WhitePoints: ").append(whitePoints).append("\n");
        return out.toString();
    }



}
