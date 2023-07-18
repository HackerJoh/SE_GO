package singleComponents;

import java.io.File;

/**
 * Class to store and transmit the given settings from the menu to the board.
 */
public class Settings {
    private int boardSize;
    private int handicap;
    private double komi;
    private File loadedFile;

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getHandicap() {
        return handicap;
    }

    public File getLoadedFile() {
        return loadedFile;
    }

    public void setLoadedFile(File loadedFile) {
        this.loadedFile = loadedFile;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    public double getKomi() {
        return komi;
    }

    public void setKomi(double komi) {
        this.komi = komi;
    }
}
