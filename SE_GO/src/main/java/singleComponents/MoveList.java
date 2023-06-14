package singleComponents;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class MoveList {
    final private List<Move> moves;

    public MoveList(){
        this.moves = new LinkedList<>();
    }

    public void addMove(SingleMove[] singleMoves){
        moves.add(new Move(singleMoves));
    }

    public void addMoveWithDescription(SingleMove[] singleMoves, String description){
        moves.add(new Move(singleMoves, description));
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
}
