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

    public void addMove(StoneColor color, int xCord, int yCord){
        moves.add(new Move(color, xCord, yCord));
    }

    public void addMoveWithDescription(StoneColor color, String description, int xCord, int yCord){
        moves.add(new Move(color, description, xCord, yCord));
    }

    public void deleteLastMove(){
        moves.remove(moves.remove(moves.size()-1));
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
