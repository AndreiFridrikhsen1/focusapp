module projects.focusapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens projects.focusapp to javafx.fxml;
    exports projects.focusapp;
}