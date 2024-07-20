package projects.focusapp;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FocusController {
    public Stopwatch stpw;

    @FXML
    private Label hours;

    @FXML
    private Label minutes;


    @FXML
    public Label seconds;
    @FXML
    private Button focus;

    @FXML
    private Button showTasks;

    @FXML
    private TextField input;

    @FXML
    private Label error;
    private int numberOfAddedItems;
    @FXML
    private ComboBox<String> dropdown;
    List<String> comboboxList = new ArrayList<>();
    private String task;
    private int index;
    @FXML
    private TableView<Stopwatch> table;
    @FXML
    private TableColumn<Stopwatch, String> taskName;
    @FXML
    private TableColumn<Stopwatch, String> focusTime;
    private boolean scene1;
    @FXML
    private TextField searchField;
    private String taskToSearch;
    ObservableList<Stopwatch> data;
    // search task
    @FXML
    void search(ActionEvent event) {
        taskToSearch = searchField.getText();
        if (!taskToSearch.isEmpty()) {
            // search observable list
            ObservableList<Stopwatch> filteredList = FXCollections.observableArrayList();
            boolean taskFound = false;

            // if stopwatch obj in observable list where taskname = to tasktosearch, add this object to a filtered list
            for (Stopwatch stopwatch : data) {
                if (stopwatch.getTask().toLowerCase().equals(taskToSearch.toLowerCase())) {
                    filteredList.add(stopwatch);
                    taskFound = true;
                }
            }

            if (taskFound) {
                table.setItems(filteredList);
                error.setText("");
            } else {
                error.setText("Task not found");
                System.out.println("Task to search: " + taskToSearch);
            }

        } else {
            error.setText("Enter the task name");
        }
    }
    @FXML
    void showAll(ActionEvent event) {
        table.setItems(data);
    }

    @FXML
    void changeScene1(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(FocusApplication.class.getResource("scene2-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage)showTasks.getScene().getWindow();
            stage.setTitle("FocusApp");
            stage.setScene(scene);
            stage.show();
            scene1 = false;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void changeScene2(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(FocusApplication.class.getResource("app-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage)focus.getScene().getWindow();
            stage.setTitle("FocusApp");
            stage.setScene(scene);
            stage.show();
            scene1 = true;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void ok(ActionEvent event) {
        if(!input.getText().isEmpty()){
            task = input.getText().toLowerCase().trim();
            try {
                validate();
                dropdown.getItems().add(input.getText().toLowerCase().trim());
                comboboxList = dropdown.getItems();
                // display last added item to the combobox
                dropdown.getSelectionModel().selectLast();
                error.setText("");
                input.setText("");


            }catch(AlreadyExistsException e){
                error.setText(e.getMessage());
            }
        }else{
            error.setText("Field shouldn't be empty");

        }

    }
    //check if input is in dropdown list
    public void validate() throws AlreadyExistsException{
        if(dropdown.getItems().contains(task)){
            throw new AlreadyExistsException("This task already exists");
        }
    }
    @FXML
    void onSelect(ActionEvent event) {
            index = dropdown.getSelectionModel().getSelectedIndex();
            task = comboboxList.get(index);


    }


    @FXML
    void start(ActionEvent event) {
        seconds.setText("00");
        minutes.setText("00");
        hours.setText("00");
        Stopwatch stopwatch1 = new Stopwatch(seconds,minutes,hours, task);
        if(dropdown.getSelectionModel().isEmpty()){
            error.setText("Please, add a new task");
        }else{
            // assign to an obj so it's accessible in stop method
            stpw = stopwatch1;
            stpw.running = true;

            // start stop watch in a separate thread
            stpw.start();
        }

        //hours.setText(String.valueOf(stopwatch1.hours));
       // minutes.setText(String.valueOf(stopwatch1.minutes));
       // seconds.setText(String.valueOf(stopwatch1.seconds));
    }
    // submit task with the time focused to the db on stop
    @FXML
    void stop(ActionEvent event) {
        try {
            stpw.running = false;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        // insert into db
        if(task!=null&&stpw.getTime()!=null) {
            Db db = new Db();
            db.submit(task, stpw.getTime());
            task = "";
            stpw.setTime("");
            //error.setText("added");
            // refresh scene
            try {
                Stage stage = (Stage) input.getScene().getWindow();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("app-view.fxml"));

                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        }else{
            System.out.println("null");
        }


    }
    public void initialize (){

       error.setText("");
        Db db = new Db();
       List<String> tasks = db.getTasks();
       try {
           for (String taskName : tasks) {
               dropdown.getItems().add(taskName);
               dropdown.getSelectionModel().selectFirst();



           }
           comboboxList = dropdown.getItems();
           task = dropdown.getSelectionModel().getSelectedItem();

       }catch (NullPointerException e){
           System.out.println(e.getMessage());
       }
       // if scene 2 display table
        if(!scene1) {
        try {

            data = db.addToList();
            taskName.setCellValueFactory(new PropertyValueFactory<>("task"));
            focusTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            table.setItems(data);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        }


    }
}
