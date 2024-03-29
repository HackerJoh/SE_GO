package model.modelComponents;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class saves all Moves of the Gameplay
 */
public class MoveList {
    private List<Move> moves;

    public MoveList() {
        this.moves = new LinkedList<>();
    }

    public void addMove(SingleMove[] singleMoves, double blackPoints, double whitePoints) {
        moves.add(new Move(singleMoves, blackPoints, whitePoints));
    }

    public void addMoveWithDescription(SingleMove[] singleMoves, String description, double blackPoints, double whitePoints) {
        moves.add(new Move(singleMoves, description, blackPoints, whitePoints));
    }

    public void exportMoves(File saveFile, int size) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SaveGame saveGame = new SaveGame(size, moves);
        FileWriter fileWriter = new FileWriter(saveFile);
        String json = mapper.writeValueAsString(saveGame);
        fileWriter.write(json);
        fileWriter.close();
    }

    public static int getSizeFromFile(File loadedFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SaveGame importSaveGame = mapper.readValue(loadedFile, new TypeReference<>() {
        });
        return importSaveGame.boardsize();
    }

    public void importMoves(File loadedFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SaveGame importSavegame = mapper.readValue(loadedFile, new TypeReference<>() {
        });
        moves = importSavegame.moves();
    }

    public SingleMove[] getAllSingleMoves() {
        List<SingleMove> allSingleMovesList = new LinkedList<>();
        for (Move move : moves) {
            Collections.addAll(allSingleMovesList, move.getSingleMoves());
        }
        SingleMove[] allSingleMoves = new SingleMove[allSingleMovesList.size()];
        allSingleMovesList.toArray(allSingleMoves);
        return allSingleMoves;
    }

    public Move getLastMove() {
        return moves.get(moves.size() - 1);
    }

    public void deleteMovesAfterIndex(int index) {
        if (moves.size() > index + 1) {
            moves.subList(index + 1, moves.size()).clear();
        }
    }

    public int getIndexOfLastMove() {
        return moves.size() - 1;
    }

    public Move getMoveByIndex(int index) {
        return moves.get(index);
    }
}
