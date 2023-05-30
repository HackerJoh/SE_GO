package singleComponents;

public class Move {
    static int idCounter;
    final private int id;
    final private StoneColor color;
    final private String description;
    final private int xCord;
    final private int yCord;

    public Move (StoneColor color, int xCord, int yCord){
        this.id = idCounter;
        this.color = color;
        idCounter++;
        description = "";
        this.xCord = xCord;
        this.yCord = yCord;

    }

    public Move (StoneColor color, String description, int xCord, int yCord){
        this.id = idCounter;
        this.color = color;
        this.description = description;
        this.xCord = xCord;
        this.yCord = yCord;
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
