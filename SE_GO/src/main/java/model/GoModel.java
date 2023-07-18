package model;

import javafx.scene.paint.Color;
import model.gameStatistics.GameEvalutation;
import model.gameStatistics.GameStatistics;
import model.modelComponents.*;
import singleComponents.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GoModel {
    private final StoneColor[][] boardArray;
    private final int size;
    private final MoveList moveList;
    private int noMoves;
    private boolean gameHasEnded = false;
    private double whitePoints;
    private double blackPoints;
    private boolean inspectionModeOn = false;
    private int jumpCounter;

    public GoModel(int size) {
        this.size = size;
        this.boardArray = getNeutralBoardArray();
        this.noMoves = 0;
        this.whitePoints = 0;
        this.blackPoints = 0;
        this.moveList = new MoveList();
    }

    public void setNoMoves(int noMoves) {
        this.noMoves = noMoves;
    }

    public int getNoMoves() {
        return noMoves;
    }

    public double getWhitePoints() {
        return whitePoints;
    }

    public double getBlackPoints() {
        return blackPoints;
    }

    public boolean isGameHasEnded() {
        return gameHasEnded;
    }

    public void setGameHasEnded(boolean gameHasEnded) {
        this.gameHasEnded = gameHasEnded;
    }

    public boolean isInspectionModeOn() {
        return inspectionModeOn;
    }

    /**
     * Evaluate with arithmetic operation which player is in turn.
     * @return StoneColor-object
     */
    public StoneColor getTurn() {
        if (noMoves % 2 == 1) {
            return StoneColor.WHITE;
        } else {
            return StoneColor.BLACK;
        }
    }

    /**
     * Add a move to the counter of moves made.
     */
    public void increaseTurn() {
        this.noMoves++;
    }

    /**
     * Controller tells model to set a stone when he recognizes a click on the board
     * @param xCoord: x-coordinate where stone will be placed in the grid.
     * @param yCoord: y-coordinate where stone will be placed in the grid.
     * @param description: Move description which is displayed in status bar.
     */
    public void controllerSetsStone(int xCoord, int yCoord, String description) {
        turnOffInspectionModeIfOn();
        if(isSuicideMove(xCoord, yCoord, getTurn())) return;
        boardArray[xCoord][yCoord] = getTurn();
        SingleMove setMove;
        if (getTurn() == StoneColor.BLACK) {
            setMove = new SingleMove(getTurn(), xCoord, yCoord, true);
        } else if (getTurn() == StoneColor.WHITE) {
            setMove = new SingleMove(getTurn(), xCoord, yCoord, true);
        } else {
            setMove = new SingleMove(getTurn(), xCoord, yCoord, true);
        }
        increaseTurn();
        this.checkAllStonesIfTheyHaveLiberties(setMove, description);
    }

    /**
     * Tell the controller which player has surrendered the game.
     * @return: String with the text for the status bar.
     */
    public String getSurrenderer() {
        setGameHasEnded(true);
        if (getTurn() == StoneColor.BLACK) {
            return "WEIß gewinnt";
        } else {
            return "SCHWARZ gewinnt";
        }
    }

    /**
     * Tell the controller which color is in turn.
     * @return: StoneColor-object
     */
    public String getTurnColor() {
        if (getTurn() == StoneColor.BLACK) {
            return "SCHWARZ ist am Zug";
        } else {
            return "WEIß ist am Zug";
        }
    }

    /**
     * Tell the controller which color is in turn and has passed his move.
     * @return: StoneColor-object
     */
    public String getPassColor() {
        if (getTurn() == StoneColor.BLACK) {
            return "SCHWARZ passt";
        } else {
            return "WEIß passt";
        }
    }

    /**
     * Forward the boardSize and the file in which the file has to be saved to MoveList class.
     * @param saveFile: File returned by the FileChooser of the save-Button.
     */
    public void saveGame(File saveFile) {
        moveList.exportMoves(saveFile, size);
    }

    /**
     * Method which reads the moves and points from the loaded game-file.
     * @param loadedFile: File which is loaded by the FileChooser from the load-Button.
     * @throws IOException: Given exception is thrown toward Main class.
     */
    public void loadGame(File loadedFile) throws IOException {
        //clearBoardArray();
        moveList.importMoves(loadedFile);
        for (SingleMove singleMove : moveList.getAllSingleMoves()) {
            if (singleMove.isSetStone()) {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = singleMove.getColor();
            } else {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = StoneColor.NEUTRAL;
            }
        }
        blackPoints = moveList.getLastMove().getBlackPoints();
        whitePoints = moveList.getLastMove().getWhitePoints();
    }

    /**
     * check if every stone or stone group have at least one liberty, if not remove it.
     * @param setMove: Current move which was made from user.
     * @param description: Description of the current move.
     */
    private void checkAllStonesIfTheyHaveLiberties(SingleMove setMove, String description) {
        boolean isOnlySetMove = true;

        // Iterate through the whole board
        for (int x = 0; x < boardArray.length; x++) {
            for (int y = 0; y < boardArray[x].length; y++) {
                StoneColor color = boardArray[x][y];

                // Check only for white or black stones, no neutrals
                if (color != StoneColor.NEUTRAL) {
                    List<Point> islandPoints = new ArrayList<>();
                    callBFS(deepCopy(this.boardArray), x, y, color, islandPoints);
                    int totalLiberties = 0;
                    for (Point p : islandPoints
                    ) {
                        totalLiberties += getLiberties(p.x, p.y);
                    }

                    // Check if stone or group has no further liberty
                    if (totalLiberties == 0) {
                        isOnlySetMove = false;
                        SingleMove[] move = new SingleMove[islandPoints.size() + 1];
                        move[0] = setMove;
                        int counter = 1;

                        // Remove every stone in the group
                        for (Point p : islandPoints
                        ) {
                            this.removeStone(p.x, p.y);
                            move[counter] = new SingleMove(getTurn(), p.x, p.y, false);
                            counter++;
                            System.out.println(this);
                            if (color == StoneColor.WHITE) {
                                blackPoints++;
                            } else {
                                whitePoints++;
                            }
                        }
                        // Add the current move to the moveList with description and points. Move can also be a hit stone or group.
                        moveList.addMoveWithDescription(move, description, blackPoints, whitePoints);
                    }
                }
            }
        }
        // If current move is only a set stone operation then add a SingleMove to the moveList.
        if (isOnlySetMove) {
            moveList.addMoveWithDescription(new SingleMove[]{setMove}, description, blackPoints, whitePoints);
        }
    }

    /**
     *
     * @param grid
     * @param x
     * @param y
     * @param color
     * @param islandPoints
     */
    public static void callBFS(StoneColor[][] grid, int x, int y, StoneColor color, List<Point> islandPoints) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[x].length || (grid[x][y] != color || grid[x][y] == StoneColor.NEUTRAL)) {
            return;
        }
        islandPoints.add(new Point(x, y));
        grid[x][y] = StoneColor.NEUTRAL; // Test mit neutral?

        callBFS(grid, x + 1, y, color, islandPoints);
        callBFS(grid, x - 1, y, color, islandPoints);
        callBFS(grid, x, y + 1, color, islandPoints);
        callBFS(grid, x, y - 1, color, islandPoints);
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private int getLiberties(int x, int y) {
        int liberties = 0;
        // Check the four neighbors of the stone
        if (x > 0 && boardArray[x - 1][y] == StoneColor.NEUTRAL) {
            liberties++;
        }
        if (x < boardArray.length - 1 && boardArray[x + 1][y] == StoneColor.NEUTRAL) {
            liberties++;
        }
        if (y > 0 && boardArray[x][y - 1] == StoneColor.NEUTRAL) {
            liberties++;
        }
        if (y < boardArray[x].length - 1 && boardArray[x][y + 1] == StoneColor.NEUTRAL) {
            liberties++;
        }
        return liberties;
    }

    /**
     *
     * @param original
     * @return
     */
    public static StoneColor[][] deepCopy(StoneColor[][] original) {
        if (original == null) {
            return null;
        }
        StoneColor[][] copy = new StoneColor[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    /**
     * Create a new board with no stones placed on it.
     * @return: StoneColor-array which contains the empty board.
     */
    public StoneColor[][] getNeutralBoardArray() {
        StoneColor[][] newArray = new StoneColor[this.size][this.size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newArray[i][j] = StoneColor.NEUTRAL;
            }
        }
        return newArray;
    }

    public String toString() {
        StringBuilder out = new StringBuilder("[\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                out.append(boardArray[i][j]).append(" ");
            }
            out.append("\n");
        }
        return out + "]";
    }

    /*public void removeCatchedStones() {
        for (int i = 0; i < size * size; i++) {
            removeCatchedStone(i / size, i % size);
        }
        System.out.println(this);
    }

    public void removeCatchedStone(int xCoord, int yCoord) {
        StoneColor color = boardArray[xCoord][yCoord];
        if (yCoord > 0 && yCoord < size - 1 && xCoord > 0 && xCoord < size - 1) {
            if (color == StoneColor.WHITE && isCatchedWhiteInnerStone(xCoord, yCoord)) {
                removeStone(xCoord, yCoord);
                System.out.println(xCoord + " " + yCoord);
            }
            if (color == StoneColor.BLACK && isCatchedBlackInnerStone(xCoord, yCoord)) {
                removeStone(xCoord, yCoord);
                System.out.println(xCoord + " " + yCoord);
            }
        }
    }


    public boolean isCatchedWhiteInnerStone(int x, int y) {
        return (boardArray[x - 1][y] == StoneColor.BLACK && boardArray[x + 1][y] == StoneColor.BLACK && boardArray[x][y - 1] == StoneColor.BLACK && boardArray[x][y + 1] == StoneColor.BLACK);
    }

    public boolean isCatchedBlackInnerStone(int x, int y) {
        return (boardArray[x - 1][y] == StoneColor.WHITE && boardArray[x + 1][y] == StoneColor.WHITE && boardArray[x][y - 1] == StoneColor.WHITE && boardArray[x][y + 1] == StoneColor.WHITE);
    }*/

    /**
     * Set the stone on the given coordinates to neutral ("remove it" from the board)
     * @param xCoord: x-coordinate from stone which must be removed
     * @param yCoord: y-coordinate from stone which must be removed
     */
    public void removeStone(int xCoord, int yCoord) {
        boardArray[xCoord][yCoord] = StoneColor.NEUTRAL;
    }

    /**
     * From stone id we can get the color of the stone by arithmetic operation.
     * @param id: Stone identifier.
     * @return: Color of the given stone.
     */
    public Color getColorById(int id) {
        if (boardArray[id / size][id % size] == StoneColor.WHITE) return Color.WHITE;
        if (boardArray[id / size][id % size] == StoneColor.BLACK) return Color.BLACK;
        return Color.TRANSPARENT;
    }

    /**
     * Check by stone id if a stone is set on that point of the grid.
     * @param id: Stone identifier.
     * @return: Boolean value if stone is set or neutral.
     */
    public boolean getUsedById(int id) {
        return (boardArray[id / size][id % size] != StoneColor.NEUTRAL);
    }

    /**
     * Turn off inspection mode when activated.
     * @return: Boolean if inspection was on or off.
     */
    public boolean turnOffInspectionModeIfOn() {
        if (inspectionModeOn) {
            turnOffInspectionMode();
            return true;
        }
        return false;
    }

    /**
     * Turn off inspection mode.
     */
    private void turnOffInspectionMode() {
        moveList.deleteMovesAfterIndex(jumpCounter);
        inspectionModeOn = false;
    }

    /**
     * Turn on inspection mode.
     */
    public void enterInspectionMode() {
        inspectionModeOn = true;
        jumpCounter = moveList.getIndexOfLastMove();
    }

    /**
     * When inspectionMode is on, jump to next step in moveList.
     */
    public void jumpForward() {
        if (inspectionModeOn) {
            if (jumpCounter < moveList.getIndexOfLastMove()) doForwardJump();
        }
    }

    /**
     * When inspectionMode is on, jump to previous step in moveList.
     */
    public void jumpBackward() {
        if (inspectionModeOn) {
            if (jumpCounter >= 0) doBackwardJump();
        }
    }

    /**
     * Get the next step from moveList and synchronize the points to it.
     */
    private void doForwardJump() {
        jumpCounter++;
        Move forwardMove = moveList.getMoveByIndex(jumpCounter);
        for (SingleMove singleMove : forwardMove.getSingleMoves()) {
            if (singleMove.isSetStone()) {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = singleMove.getColor();
            } else {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = StoneColor.NEUTRAL;
            }
        }
        blackPoints = forwardMove.getBlackPoints();
        whitePoints = forwardMove.getWhitePoints();
    }

    /**
     * Get the previous step from moveList and synchronize the points to it.
     */
    private void doBackwardJump() {
        Move backwardMove = moveList.getMoveByIndex(jumpCounter);
        for (SingleMove singleMove : backwardMove.getSingleMoves()) {
            if (!singleMove.isSetStone()) {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = singleMove.getColor();
            } else {
                boardArray[singleMove.getxCoord()][singleMove.getyCoord()] = StoneColor.NEUTRAL;
            }
        }
        if (jumpCounter > 0) {
            jumpCounter--;
            Move newCurrentMove = moveList.getMoveByIndex(jumpCounter);
            blackPoints = newCurrentMove.getBlackPoints();
            whitePoints = newCurrentMove.getWhitePoints();
        } else {
            jumpCounter--;
            blackPoints = 0;
            whitePoints = 0;
        }
    }

    /**
     * Get the description from the actual step which the user jumped to.
     * @return: String of the current description from the move.
     */
    public String getDescriptionFromJump() {
        if (jumpCounter < 0) {
            return "Beginn";
        }
        return moveList.getMoveByIndex(jumpCounter).getDescription();
    }

    /**
     * If description gets changed in inspection mode set it to the new String when jumping.
     * @param description: New description which is set by user.
     */
    public void setDescriptionWhenJumping(String description) {
        if (jumpCounter > 0) {
            moveList.getMoveByIndex(jumpCounter).setDescription(description);
        }
    }

    /**
     * Trigger the GameEvaluation class and generate a GameStatistics object where game parameters are stored.
     * @return: GameStatistics object for controller to display winner and points.
     */
    public GameStatistics evaluateGame() {
        setGameHasEnded(true);
        GameEvalutation evalutation = new GameEvalutation(deepCopy(boardArray), moveList);
        GameStatistics endgame = evalutation.evaluateEndGameStatistics();
        System.out.println(endgame);

        return endgame;
    }

    /**
     * Set the given amount of handicap stones on the board and add it as first move to moveList.
     * @param handicap: Amount of handicap stones to set.
     * @param komi: Amount of komi to add as point for white side.
     */
    public void setHandicap(int handicap, double komi){
        if(handicap > 9 || handicap < 0) throw new IllegalArgumentException("Invalid Input!");

        switch (size) {
            case 9 -> setHandicap9x9(handicap);
            case 13 -> setHandicap13x13(handicap);
            case 19 -> setHandicap19x19(handicap);
        }

        List<SingleMove> singleMoves = new LinkedList<>();
        for (int x = 0; x < boardArray.length; x++) {
            for(int y = 0; y < boardArray[x].length; y++){
                if(boardArray[x][y] == StoneColor.BLACK){
                    singleMoves.add(new SingleMove(StoneColor.BLACK, x, y, true));
                }
            }
        }
        moveList.addMove(singleMoves.toArray(SingleMove[]::new), 0, komi);
        whitePoints = komi;
    }

    /**
     * Set fixed handicap positions for 9x9 board size.
     * @param handicap: Amount of handicap stones.
     */
    public void setHandicap9x9(int handicap){
        if(size != 9) throw new IllegalArgumentException();

        if(handicap ==9) boardArray[4][4] = StoneColor.BLACK;
        if(handicap >=8) boardArray[2][4] = StoneColor.BLACK;
        if(handicap >=7) boardArray[4][6] = StoneColor.BLACK;
        if(handicap >=6) boardArray[6][4] = StoneColor.BLACK;
        if(handicap >=5) boardArray[4][2] = StoneColor.BLACK;
        if(handicap >=4) boardArray[2][6] = StoneColor.BLACK;
        if(handicap >=3) boardArray[6][6] = StoneColor.BLACK;
        if(handicap >=2) boardArray[6][2] = StoneColor.BLACK;
        if(handicap >=1) boardArray[2][2] = StoneColor.BLACK;


    }

    /**
     * Set fixed handicap positions for 13x13 board size.
     * @param handicap: Amount of handicap stones.
     */
    public void setHandicap13x13(int handicap){
        if(size != 13) throw new IllegalArgumentException();

        if(handicap ==9) boardArray[6][6] = StoneColor.BLACK;
        if(handicap >=8) boardArray[3][6] = StoneColor.BLACK;
        if(handicap >=7) boardArray[6][9] = StoneColor.BLACK;
        if(handicap >=6) boardArray[9][6] = StoneColor.BLACK;
        if(handicap >=5) boardArray[6][3] = StoneColor.BLACK;
        if(handicap >=4) boardArray[3][9] = StoneColor.BLACK;
        if(handicap >=3) boardArray[9][9] = StoneColor.BLACK;
        if(handicap >=2) boardArray[9][3] = StoneColor.BLACK;
        if(handicap >=1) boardArray[3][3] = StoneColor.BLACK;

    }

    /**
     * Set fixed handicap positions for 19x19 board size.
     * @param handicap: Amount of handicap stones.
     */
    public void setHandicap19x19(int handicap){
        if(size != 19) throw new IllegalArgumentException();

        if(handicap ==9) boardArray[9][9] = StoneColor.BLACK;
        if(handicap >=8) boardArray[3][9] = StoneColor.BLACK;
        if(handicap >=7) boardArray[9][15] = StoneColor.BLACK;
        if(handicap >=6) boardArray[15][9] = StoneColor.BLACK;
        if(handicap >=5) boardArray[9][3] = StoneColor.BLACK;
        if(handicap >=4) boardArray[3][15] = StoneColor.BLACK;
        if(handicap >=3) boardArray[15][15] = StoneColor.BLACK;
        if(handicap >=2) boardArray[15][3] = StoneColor.BLACK;
        if(handicap >=1) boardArray[3][3] = StoneColor.BLACK;
    }

    private boolean isSuicideMove(int x, int y, StoneColor moveColor){
        List<Point> moveGroup = new ArrayList<>();
        StoneColor[][] dummyArray = deepCopy(boardArray);
        dummyArray[x][y] = moveColor;
        callBFS(dummyArray, x, y, moveColor, moveGroup);
        int totalLiberties = 0;
        for(Point p : moveGroup){
            totalLiberties = getLiberties(p.x, p.y);
        }
        return totalLiberties <= 0;
    }
}
