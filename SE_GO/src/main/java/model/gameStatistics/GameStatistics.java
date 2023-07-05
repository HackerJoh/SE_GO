package model.gameStatistics;

public record GameStatistics(int blackPoints, int whitePoints) {
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("GameStats\n----------------------------------\n");
        out.append("BlackPoints: ").append(blackPoints).append("\n");
        out.append("WhitePoints: ").append(whitePoints).append("\n");
        return out.toString();
    }
}
