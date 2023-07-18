package model;

import controller.BoardController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import singleComponents.Settings;
import singleComponents.StoneColor;

import static org.junit.jupiter.api.Assertions.*;

public class GoModelTest {
    private GoModel model;
    BoardController controller = null;

    @BeforeAll
    static void beforeAll() {
        Platform.startup(() -> {
        });
    }

    @BeforeEach
    void setUp() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Board.fxml"));
        loader.load();
        controller = loader.getController();
        Settings s = new Settings();
        s.setBoardSize(9);
        s.setKomi(0);
        s.setHandicap(0);
        controller.initData(s);
        model = controller.model;
    }

    //set noOfMoves
    @Test
    public void testSetNoMoves() {
        // Arrange
        int expectedNoMoves = 5;

        // Act
        model.setNoMoves(expectedNoMoves);
        int actualNoMoves = model.getNoMoves();

        // Assert
        assertEquals(expectedNoMoves, actualNoMoves);
    }

    @Test
    public void testSetNoMovesMultipleTimes() {
        // Arrange
        int noMoves1 = 3;
        int noMoves2 = 7;
        int noMoves3 = 11;

        // Act
        model.setNoMoves(noMoves1);
        model.setNoMoves(noMoves2);
        model.setNoMoves(noMoves3);

        // Assert
        assertEquals(noMoves3, model.getNoMoves());
    }

    //GetNoOfMoves
    @Test
    public void testGetNoMovesInitiallyZero() {
        // Arrange and Act
        int actualNoMoves = model.getNoMoves();

        // Assert
        assertEquals(0, actualNoMoves);
    }

    @Test
    public void testCheckAllStonesIfTheyHaveLibertiesAndTestPointsCounting() {
        this.whiteCaptures1Black();

        double actualWhitePoints = model.getWhitePoints();
        double actualBlackPoints = model.getBlackPoints();

        assertEquals(Color.TRANSPARENT, model.getColorById(3 * 9 + 3));
        // Assert
        assertEquals(1.0, actualWhitePoints);
        assertEquals(0.0, actualBlackPoints);
    }

    public void whiteCaptures1Black() {
        // Arrange
        // Set some stones on the board for testing
        model.controllerSetsStone(3, 3, "Move 1"); // Assuming the controllerSetsStone works as intended
        model.controllerSetsStone(3, 2, "Move 2");
        model.controllerSetsStone(6, 6, "Move 3");
        model.controllerSetsStone(4, 3, "Move 4");
        model.controllerSetsStone(7, 8, "Move 5");
        model.controllerSetsStone(3, 4, "Move 6");
        model.controllerSetsStone(5, 5, "Move 7");
        //capture 1 stone in the middle
        model.controllerSetsStone(2, 3, "Move 8");
    }

    @Test
    public void testCheckAllStonesIfTheyHaveLibertiesAndTestPointsCountingBothHavePoints() {
        this.whiteCaptures1Black();

        //set ueseless to switch colors
        model.controllerSetsStone(6, 3, "Move 8");

        model.controllerSetsStone(0, 0, "Move 9");
        model.controllerSetsStone(1, 0, "Move 10");
        model.controllerSetsStone(6, 8, "Move 11");
        model.controllerSetsStone(0, 1, "Move 12");

        double actualWhitePoints = model.getWhitePoints();
        double actualBlackPoints = model.getBlackPoints();

        assertEquals(Color.TRANSPARENT, model.getColorById(3 * 9 + 3));
        // Assert
        assertEquals(1.0, actualWhitePoints);
        assertEquals(1.0, actualBlackPoints);
    }

    @Test
    public void testGetTurn() {
        assertEquals(StoneColor.BLACK, model.getTurn());
        model.controllerSetsStone(0, 1, "Test");
        assertEquals(StoneColor.WHITE, model.getTurn());
    }


    @Test
    public void testGameEnd() {
        model.getSurrenderer();
        assertTrue(model.isGameHasEnded());
    }

    @Test
    public void testHandicap9x9() {
        model.setHandicap9x9(1);
        assertEquals(Color.BLACK, model.getColorById(20));
    }

    @Test
    public void testHandicap13x13() {
        model = new GoModel(13);
        model.setHandicap13x13(1);
        assertEquals(Color.BLACK, model.getColorById(42));
    }

    @Test
    public void testHandicap19x19() {
        model = new GoModel(19);
        model.setHandicap19x19(1);
        assertEquals(Color.BLACK, model.getColorById(60));
    }

    @Test
    public void testEvaluateGame() {
        this.whiteCaptures1Black();
        assertEquals(2.00, model.evaluateGame().whitePoints());
    }

    @Test
    public void testInspectionMode() {
        model.enterInspectionMode();
        assertTrue(model.isInspectionModeOn());
        model.turnOffInspectionModeIfOn();
        assertFalse(model.isInspectionModeOn());
    }

    @Test
    public void testGetPassColor() {
        String test = model.getPassColor();
        assertEquals("SCHWARZ passt", test);
    }

    @Test
    public void testIncreaseTurn() {
        model.increaseTurn();
        assertEquals(1, model.getNoMoves());
    }

}
