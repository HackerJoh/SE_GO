package singleComponents;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class MoveList {
    private List<Move> moves;

    public MoveList(){
        this.moves = new LinkedList<>();
    }

    public void addMove(SingleMove[] singleMoves, int boardsize){
        moves.add(new Move(singleMoves, boardsize));
    }

    public void addMoveWithDescription(SingleMove[] singleMoves, String description, int boardsize){
        moves.add(new Move(singleMoves, description, boardsize));
    }

    public void deleteLastMove(){
        moves.remove(moves.size()-1);
    }

    public void exportMoves(String path){
        ObjectMapper mapper = new ObjectMapper();

        try(FileWriter fileWriter = new FileWriter(path)){
            String json = mapper.writeValueAsString(moves);
            fileWriter.write(json);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void importMoves(String path){
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Move> importMoves = mapper.readValue(Paths.get(path).toFile(), new TypeReference<>() {});
            if(importMoves.get(0).getBoardSize() == moves.get(0).getBoardSize()){
                moves = importMoves;
            }else{
                System.out.println("Falsche Spielgröße");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public SingleMove[] getAllSingleMoves(){
        List<SingleMove> allSingleMovesList = new LinkedList<>();
        for(Move move : moves){
            Collections.addAll(allSingleMovesList, move.getSingleMoves());
        }
        SingleMove[] allSingleMoves = new SingleMove[allSingleMovesList.size()];
        allSingleMovesList.toArray(allSingleMoves);
        return allSingleMoves;
    }
}
