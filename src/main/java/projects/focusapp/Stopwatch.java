package projects.focusapp;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Stopwatch extends Thread {

    public Label seconds;
    public Label hours;
    public Label minutes;
    public int secondsInt = 0;
    public int minutesInt = 0;
    public int hoursInt = 0;
    public boolean running;
    private FocusController controller;
    //param constructor
    public Stopwatch(Label seconds, Label minutes, Label hours){
        this.hours = hours;
        this.seconds=seconds;
        this.minutes=minutes;
    }
    public void run(){
        //cast labels to int
         secondsInt = Integer.parseInt(seconds.getText().substring(1));
         minutesInt = Integer.parseInt(minutes.getText().substring(1));
         hoursInt = Integer.parseInt(hours.getText().substring(1));
        while (running) {

            try {
                Thread.sleep(1000);

                secondsInt ++;
                if(secondsInt>59){
                    secondsInt = 0;
                    minutesInt ++;

                }
                if(minutesInt>59){
                    minutesInt = 0;
                    hoursInt ++;
                }


                //setText on the main thread
                Platform.runLater(new Runnable() {
                    public void run() {
                        // format time
                        if(secondsInt<10) {
                            seconds.setText("0" + String.valueOf(secondsInt));
                        }else{
                            seconds.setText(String.valueOf(secondsInt));
                        }
                        if(hoursInt<10) {
                            hours.setText("0" + String.valueOf(hoursInt));
                        }else{
                            hours.setText(String.valueOf(hoursInt));
                        }
                        if(minutesInt<10){
                            minutes.setText("0" + String.valueOf(minutesInt));
                        }else{
                            minutes.setText(String.valueOf(minutesInt));
                        }



                    }
                });







            }catch (Exception e){
                System.out.println(e);
            }
        }

    }

}
