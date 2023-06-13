package model;

import controller.BoardController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kordamp.bootstrapfx.BootstrapFX;
import singleComponents.Settings;
import singleComponents.StoneColor;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Board_2.fxml"));
        loader.load();
        BoardController controller = loader.getController();
        Settings s = new Settings();
        s.setBoardSize(9);
        s.setKomi(0);
        s.setHandicap(0);
        controller.initData(s);
        model = controller.model;
    }

    @Test
    public void testSetStone() {

        model.setStone(0, StoneColor.WHITE);
        assertEquals(Color.WHITE,model.getColorById(0));
    }

}
