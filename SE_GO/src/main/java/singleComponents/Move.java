package singleComponents;

public class Move {
    static int idCounter;
    final private int id;
    final private StoneColor color;
    final private String description;

    public Move (StoneColor color){
        this.id = idCounter;
        this.color = color;
        idCounter++;
        description = "";
    }

    public Move (StoneColor color, String description){
        this.id = idCounter;
        this.color = color;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public StoneColor getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }
}
