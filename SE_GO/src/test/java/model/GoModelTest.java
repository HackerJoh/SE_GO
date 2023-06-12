package model;

import controller.BoardController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoModelTest {
    private GoModel model;
    @BeforeAll
    static void beforeAll(){
        Platform.startup(() -> {});
    }
    @BeforeEach
    void setUp() throws Exception {
        model=new GoModel(9, new BoardController());
    }

    @Test
    public void testSetStone() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.load(getClass().getResource("/view/Board_2.fxml"));
        BoardController controller = (BoardController) fxmlLoader.getController();

        controller.model.setStone(0, 1);
        assertEquals(Color.WHITE,controller.model.getColorById(0));
    }

}
