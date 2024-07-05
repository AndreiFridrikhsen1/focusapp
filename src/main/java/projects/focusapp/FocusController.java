package projects.focusapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FocusController {
    public Stopwatch stpw;
    @FXML
    private Label hours;

    @FXML
    private Label minutes;

    @FXML
    public Label seconds;


    @FXML
    void start(ActionEvent event) {
        Stopwatch stopwatch1 = new Stopwatch(seconds,minutes,hours);
        // assign to an obj so it's accessible in stop method
        stpw = stopwatch1;
        stpw.running = true;
        // start stop watch in a separate thread
        stpw.start();
        //hours.setText(String.valueOf(stopwatch1.hours));
       // minutes.setText(String.valueOf(stopwatch1.minutes));
       // seconds.setText(String.valueOf(stopwatch1.seconds));
    }

    @FXML
    void stop(ActionEvent event) {
        stpw.running = false;
    }
}
