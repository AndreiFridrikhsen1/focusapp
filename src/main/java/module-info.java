module projects.focusapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires com.google.gson;


    opens projects.focusapp to javafx.fxml, com.google.gson;
    exports projects.focusapp;
}