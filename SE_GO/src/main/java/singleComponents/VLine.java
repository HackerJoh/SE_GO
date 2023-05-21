package singleComponents;

import javafx.scene.shape.Line;

/**
 * This Class represents a vertical Line for the Go-View, every Java-FX-Group with a Stone in it has one.
 */
public class VLine extends Line {
    private final int id;

    /**
     *
     * @param id: Is the id of the Line, the Line gets the ID when de Lines for de Go-Board are created
     * @param startY: Are for the Superclass
     * @param endY: Also for the Superclass
     */
    public VLine(int id, double startY, double endY){
        super(0, startY, 0, endY);
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
