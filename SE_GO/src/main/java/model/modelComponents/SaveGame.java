package model.modelComponents;
import java.util.List;

/**
 * Record represents a Game which can be saved in a JSON
 */
public record SaveGame(int boardsize,
                       List<Move> moves)
{}
