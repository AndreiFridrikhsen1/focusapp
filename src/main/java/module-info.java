module projects.focusapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens projects.focusapp to javafx.fxml;
    exports projects.focusapp;
}