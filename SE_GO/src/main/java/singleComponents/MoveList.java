package singleComponents;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class MoveList {
    private List<Move> moves;

    public MoveList() {
        this.moves = new LinkedList<>();
    }

    public void addMove(SingleMove[] singleMoves, int boardsize) {
        moves.add(new Move(singleMoves, boardsize));
    }

    public void addMoveWithDescription(SingleMove[] singleMoves, String description, int boardsize) {
        moves.add(new Move(singleMoves, description, boardsize));
    }


    public void exportMoves(String path) {
        ObjectMapper mapper = new ObjectMapper();

        try (FileWriter fileWriter = new FileWriter(path)) {
            String json = mapper.writeValueAsString(moves);
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getSizeFromFile(File loadedFile) throws IOException { //TODO: einheitliche Fehlerbehandlung
        ObjectMapper mapper = new ObjectMapper();
        List<Move> importMoves = mapper.readValue(loadedFile, new TypeReference<>() {
        });
        return importMoves.get(0).getBoardSize();
    }

    public void importMoves(File loadedFile) throws IOException { //TODO: getsizeFromFile und importMoves zusammenlegen -- mit Moritz absprechen
        ObjectMapper mapper = new ObjectMapper();
        moves = mapper.readValue(loadedFile, new TypeReference<>() {
        });
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
}
