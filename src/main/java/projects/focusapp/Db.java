    package projects.focusapp;
    import javafx.beans.Observable;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.scene.control.Tab;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.cell.PropertyValueFactory;

    import java.sql.*;
    import java.text.SimpleDateFormat;
    import java.time.LocalTime;
    import java.util.ArrayList;
    import java.util.List;

    public class Db {
        private Boolean taskExists;
        private String overallTime;
        private Connection connection;
        private PreparedStatement statement;

        private String taskName;
        private String time;
        private String time2;
        private TableView<Stopwatch> tableView;
        TableColumn<Stopwatch, String> focusTime;
        TableColumn<Stopwatch, String> taskN;
        List<String> tasks = new ArrayList<>();


        public Db() {


        }

        // submit
        public void submit(String taskName, String time) {
            this.time = time;
            System.out.println(taskName);
            System.out.println(time);

            try {


                connection = DriverManager.getConnection(SensitiveInfo.connectionString);
                // if task exist add time to it without creating a new row
                getTime(taskName);
                if (taskExists) {
                    statement = connection.prepareStatement("UPDATE focus_session set focus_time = ? where task = ?");
                    statement.setString(1, overallTime);
                    statement.setString(2, taskName);
                    statement.execute();
                    System.out.println("task exists");

                } else {
                    statement = connection.prepareStatement("INSERT into focus_session (task, focus_time) values (?,?)");
                    statement.setString(1, taskName);
                    statement.setString(2, time);
                    statement.execute();
                    System.out.println("task not exists");

                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            // get time of existing task

        }

        public void getTime(String taskName) {
            try {
                connection = DriverManager.getConnection(SensitiveInfo.connectionString);
                statement = connection.prepareStatement("SELECT task, focus_time FROM focus_session WHERE task = ?");
                statement.setString(1, taskName);

                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.next()) {
                    taskExists = false;

                } else {
                    taskExists = true;
                    time2 = resultSet.getString("focus_time");
                    System.out.println("Time 2: " + time2);

                    // add times if the task exists
                    try {

                        LocalTime t1 = LocalTime.parse(time);
                        LocalTime t2 = LocalTime.parse(time2);
                        LocalTime sum = t1.plusHours(t2.getHour()).plusMinutes(t2.getMinute()).plusSeconds(t2.getSecond());
                        overallTime = sum.toString();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("Overall time: " + overallTime);

                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        }

        public ObservableList addToList() {
            ObservableList<Stopwatch> data = FXCollections.observableArrayList();
            try {
                connection = DriverManager.getConnection(SensitiveInfo.connectionString);
                statement = connection.prepareStatement("Select * from focus_session");
                ResultSet resultSet = statement.executeQuery();


                while (resultSet.next()) {
                    System.out.println(resultSet.getString("task"));
                    // add tasks to the list to display them in the dropdown
                    System.out.println(resultSet.getString("focus_time"));
                    data.add(new Stopwatch(resultSet.getString("task"), resultSet.getString("focus_time")));
                }

                System.out.println(data);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return data;
        }

        public List<String> getTasks() {
            try {
                connection = DriverManager.getConnection(SensitiveInfo.connectionString);
                statement = connection.prepareStatement("Select * from focus_session");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    tasks.add(resultSet.getString("task"));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return tasks;
        }


    }
