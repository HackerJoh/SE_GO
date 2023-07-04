package model.gameStatistics;

import model.modelComponents.MoveList;
import singleComponents.StoneColor;

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
        return null;
    }


}
