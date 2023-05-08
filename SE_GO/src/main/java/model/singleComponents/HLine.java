package model.singleComponents;

import javafx.scene.shape.Line;

/**
 * This Class represents a horizontal Line for the Go-View, every Java-FX-Group with a Stone in it has one.
 */
public class HLine extends Line {
    //The id is primarily intended for easier assignment in the GoView
    private final int id;

    /**
     *
     * @param id: Is the id of the Line, the Line gets the ID when de Lines for de Go-Board are created
     * @param startX: Are for the Superclass
     * @param endX: Also for the Superclass
     */
    public HLine(int id, double startX, double endX){
        super(startX, 0, endX, 0);
        this.id = id;
    }

    /**
     * Getter for the ID of the Horizontal Line, it's primilary used for the Eventlisteners of the GUI-Size
     * @return : The ID of the Line
     */
    public int getGoLineId() {
        return id;
    }
}
