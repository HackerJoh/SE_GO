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

    void addMove(StoneColor color){
        moves.add(new Move(color));
    }

    void addMoveWithDescription(StoneColor color, String description){
        moves.add(new Move(color, description));
    }

    void deleteLastMove(){
        moves.remove(moves.remove(moves.size()-1));
    }

    void exportMoves(String path){
        ObjectMapper mapper = new ObjectMapper();

        try(FileWriter fileWriter = new FileWriter("jsonArray.json")){
            String json = mapper.writeValueAsString(moves);
            fileWriter.write(json);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
