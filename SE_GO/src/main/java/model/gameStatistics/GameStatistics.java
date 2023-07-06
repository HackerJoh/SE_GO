package model.gameStatistics;

import singleComponents.EndgameColors;

public record GameStatistics(int blackPoints, int whitePoints, int blackMoves, int whiteMoves, int capturedBlackStones, int capturedWhiteStones, EndgameColors[][] endBoard) {
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("GameStats\n----------------------------------\n");
        out.append("BlackPoints: ").append(blackPoints).append("\n");
        out.append("WhitePoints: ").append(whitePoints).append("\n");
        out.append("BlackMoves: ").append(blackMoves).append("\n");
        out.append("WhiteMoves: ").append(whiteMoves).append("\n");
        out.append("CapturedBlackStones: ").append(capturedBlackStones).append("\n");
        out.append("CapturedWhiteStones: ").append(capturedWhiteStones).append("\n\n");

        for(EndgameColors[] subarray : endBoard){
            for(EndgameColors color : subarray){
                out.append(color).append(" ");
            }
            out.append("\n");
        }

        return out.toString();
    }
}