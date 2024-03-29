module com.example.se_go {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;

    opens controller;
    opens singleComponents;

    exports model;
    exports controller;
    exports GoApplication;
    exports singleComponents;
    exports model.modelComponents;
    opens model.modelComponents;
    exports controller.guiComponents;
    opens controller.guiComponents;


}