package controller;


import de.saxsys.javafx.test.JfxRunner;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.assertions.api.Assertions.assertThat;

public class MenuControllerTest {
    private MenuController controller;

    @BeforeAll
    static void beforeAll(){
        Platform.startup(() -> {});
    }
    @BeforeEach
    void setUp() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Menu.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
    }

    @Test
    public void testComboBoxSize(){
        ComboBox<String> comboBox = controller.cbx_boardSize;
        List<String> items = comboBox.getItems();
        assertEquals(3,items.size());

        List<String> actualList = Arrays.asList("19x19", "13x13", "9x9");
        assertEquals(items,actualList);
    }

    @Test
    public void testKomi(){
        Spinner<Double> komi = controller.sp_komi;
        assertEquals(0,komi.getValue());
        komi.increment(99);
        assertEquals(10,komi.getValue());
        komi.decrement(600);
        assertEquals(0,komi.getValue());
    }

    @Test
    public void testHandicap(){
        Spinner<Integer> hc = controller.sp_handicap;
        assertEquals(0,hc.getValue());
        hc.increment(99);
        assertEquals(9,hc.getValue());
        hc.decrement(600);
        assertEquals(0,hc.getValue());
    }


}
