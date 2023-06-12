package controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardControllerTest {
    private BoardController controller;

    @BeforeAll
    static void beforeAll(){
        Platform.startup(() -> {});
    }
    @BeforeEach
    void setUp() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Board_2.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
    }

    @Test
    public void checkSize(){
        assertEquals(controller.model.getSize(),1);
    }


}
