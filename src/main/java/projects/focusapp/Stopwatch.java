package projects.focusapp;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stopwatch extends Thread {

    private Label seconds;
    private Label hours;
    private Label minutes;
    private int secondsInt = 0;
    private int minutesInt = 0;
    private int hoursInt = 0;
    public boolean running;
    private String task;
    private String time;
    private Date date;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public Stopwatch(String task, String time){
        this.task = task;
        this.time = time;
    }

    private FocusController controller;
    //param constructor
    public Stopwatch(Label seconds, Label minutes, Label hours, String task ){
        this.hours = hours;
        this.seconds=seconds;
        this.minutes=minutes;
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Stopwatch(){}
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
                        time = hours.getText() + ":" + minutes.getText() + ":" + seconds.getText();
                        DateFormat format = new SimpleDateFormat("hh:mm:ss");



                    }


                });







            }catch (Exception e){
                System.out.println(e);
            }
        }

    }

}
