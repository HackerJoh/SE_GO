package singleComponents;

public class Move {
    static int idCounter;
    final private int id;
    final private String description;
    final private SingleMove[] singleMoves;

    public Move (SingleMove[] singleMoves){
        this.id = idCounter;
        idCounter++;
        description = "";
        this.singleMoves = singleMoves;
    }

    public Move (SingleMove[] singleMoves, String description){
        this.id = idCounter;
        idCounter++;
        this.description = description;
        this.singleMoves = singleMoves;
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
}
