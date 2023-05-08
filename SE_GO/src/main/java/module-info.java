module com.example.se_go {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens controller;

    exports model;
    exports controller;
    exports GoApplication;
    exports model.singleComponents;

}