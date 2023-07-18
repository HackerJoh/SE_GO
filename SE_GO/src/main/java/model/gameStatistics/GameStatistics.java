package model.gameStatistics;

import singleComponents.EndgameColors;
import singleComponents.StoneColor;

public record GameStatistics(double blackPoints, double whitePoints, int blackMoves, int whiteMoves,
                             int capturedBlackStones, int capturedWhiteStones, StoneColor winner,
                             EndgameColors[][] endBoard) {
    @Override
    public String toString() {
        String out = "GameStats\n----------------------------------\n" + "BlackPoints: " + blackPoints + "\n" +
                "WhitePoints: " + whitePoints + "\n" +
                "BlackMoves: " + blackMoves + "\n" +
                "WhiteMoves: " + whiteMoves + "\n" +
                "CapturedBlackStones: " + capturedBlackStones + "\n" +
                "CapturedWhiteStones: " + capturedWhiteStones + "\n" +
                "Winner: " + winner + "\n\n";

        return out;
    }
}
