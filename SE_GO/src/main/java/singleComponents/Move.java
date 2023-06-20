package singleComponents;

public class Move {
    static int idCounter;
     private int id;
     private String description;
     private int boardSize;
     private SingleMove[] singleMoves;


     public Move(){}

    public Move (SingleMove[] singleMoves, int boardSize){
        this.id = idCounter;
        idCounter++;
        description = "";
        this.singleMoves = singleMoves;
        this.boardSize = boardSize;
    }

    public Move (SingleMove[] singleMoves, String description, int boardSize){
        this.id = idCounter;
        idCounter++;
        this.description = description;
        this.singleMoves = singleMoves;
        this.boardSize = boardSize;

    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public SingleMove[] getSingleMoves(){
        return singleMoves;
    }

    public int getBoardSize(){ return boardSize; }

    public static void setIdCounter(int idCounter) {
        Move.idCounter = idCounter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSingleMoves(SingleMove[] singleMoves) {
        this.singleMoves = singleMoves;
    }

    public String toString(){
        String out = ""+id+" ";
        for(SingleMove singleMove : singleMoves) out += singleMove.getColor();
        return out;
    }
}
