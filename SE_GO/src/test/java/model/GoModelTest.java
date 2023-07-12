package model;

import controller.BoardController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import singleComponents.Settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoModelTest {
    private GoModel model;
    BoardController controller = null;
    @BeforeAll
    static void beforeAll(){
        Platform.startup(() -> {});
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

    /*@Test
    public void testSetStone() {
        model.setStone(0, StoneColor.WHITE);
        assertEquals(Color.WHITE,model.getColorById(0));
    }

    @Test
    public void testGetTurn() {
        model.setStone(0, StoneColor.WHITE);
        assertEquals(StoneColor.BLACK,model.getTurn());
    }


    @Test
    public void testGetSize() {
        assertEquals(model.getSize(),9);
    }

    @Test
    public void testNoOfMoves() {
        model.setStone(0, StoneColor.WHITE);
        model.setStone(1, StoneColor.BLACK);
        model.setStone(2, StoneColor.WHITE);
        assertEquals(3,model.getNoMoves());
    }

    @Test
    public void testIncreaseTurn() {
        model.increaseTurn();
        assertEquals(1,model.getNoMoves());
    }

    @Test
    public void testSaveGame() {
        model.saveGame();
        File file = new File("list.json");
        assertTrue(file.exists());
    }

    @Test
    public void testLibertyCheck() {
        model.setStone(0, StoneColor.WHITE);
        model.setStone(1, StoneColor.BLACK);
        model.setStone(3, StoneColor.BLACK);

        assertEquals(1,model.getBlackPoints());
    }

    @Test
    public void testRemoveCatchedStone() {
        model.setStone(0, StoneColor.WHITE);
        model.removeStone(0,0);
        assertEquals(Color.TRANSPARENT,model.getColorById(0));
    }*/

}
