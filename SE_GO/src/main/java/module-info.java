module com.example.se_go {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens controller;

    exports model;
    exports controller;

}