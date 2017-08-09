package c195;

import static c195.Appointments.convertToSystem;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import static java.util.Locale.FRENCH;
import static java.util.Locale.ITALIAN;
import static java.util.Locale.US;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class LogInController implements Initializable {

    @FXML
    private Label logInPrompt;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private RadioButton english;

    @FXML
    private RadioButton italian;

    @FXML
    private RadioButton french;

    private static String userName;

    public static String getUserName() {
        return userName;
    }

    @FXML
    public void Login(ActionEvent event) throws Exception {

        userName = userNameField.getText();
        String password = passwordTextField.getText();

        try {
            PreparedStatement getLogin = DBconnection.getConnection().prepareStatement("SELECT password FROM user WHERE userName=\"" + userName + "\"");

            ResultSet rs = getLogin.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            if (rs.next()) {

                String dbPassword = rs.getString("password");

                if (dbPassword.trim().length() > 0 && dbPassword.equals(password)) {

                    logUser(Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())), true);

                    Alarm meeting = new Alarm();
                    meeting.setDaemon(true);
                    meeting.start();

                    Stage primaryStage = new Stage();
                    C195 currentWindow = new C195();
                    Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));

                    Scene scene = new Scene(root);

                    primaryStage.setScene(scene);
                    currentWindow.setNewStage(primaryStage);

                } else {
                    System.out.println("Log in Failed");

                }

            } else if (english.isSelected()) {
                System.out.println("No Such User");
                logInPrompt.setText(ResourceBundle.getBundle("c195/MessagesBundle", US).getString("logInFailed"));

            } else if (italian.isSelected()) {
                logInPrompt.setText(ResourceBundle.getBundle("c195/MessagesBundle_it_IT", ITALIAN).getString("logInFailed"));

            } else if (french.isSelected()) {
                logInPrompt.setText(ResourceBundle.getBundle("c195/MessagesBundle_fr_FR", FRENCH).getString("logInFailed"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void langSelect() {
        if (english.isSelected()) {
            logInPrompt.setText(ResourceBundle.getBundle("c195/MessagesBundle", US).getString("logInPrompt"));
        } else if (italian.isSelected()) {
            logInPrompt.setText(ResourceBundle.getBundle("c195/MessagesBundle_it_IT", ITALIAN).getString("logInPrompt"));
        } else if (french.isSelected()) {
            logInPrompt.setText(ResourceBundle.getBundle("c195/MessagesBundle_fr_FR", FRENCH).getString("logInPrompt"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public static void logUser(Timestamp t, boolean logIn) {
        String entry = new String();

        if (logIn) {
            entry = userName + " Logged in at " + t.toString() + "\n";
        } else {
            entry = userName + " Logged out at " + t.toString() + "\n";
        }
        byte data[] = entry.getBytes();
        Path p = Paths.get("./logfile.txt");

        //Try with Resources
        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }

    }

    public class Alarm extends Thread implements Runnable {

        @Override
        public void run() {

            String appointmentId = new String();
            String customerId = new String();
            String title = new String();
            String description = new String();
            String location = new String();
            String contact = new String();
            String url = new String();
            Timestamp start;
            Timestamp end;
            while (true) {

                Timestamp now = Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault()));
                
                ZonedDateTime nowUTC = now.toLocalDateTime().atZone(ZoneId.systemDefault());  //Start time in UTC for database
                nowUTC = nowUTC.withZoneSameInstant(ZoneId.of("UTC"));
                
                now = Timestamp.valueOf(nowUTC.toLocalDateTime());

                now.setTime(now.getTime() - 1 * 60 * 1000L);

                long timeDelay = 16 * 60 * 1000L;
                timeDelay = now.getTime() + timeDelay;

                Timestamp later = Timestamp.valueOf(LocalDateTime.now());
                later.setTime(timeDelay);

                try {
                    PreparedStatement stmt = DBconnection.getConnection().prepareStatement("SELECT * FROM appointment WHERE contact=? AND start BETWEEN ? AND ?");

                    stmt.setString(1, LogInController.getUserName());
                    stmt.setTimestamp(2, now);
                    stmt.setTimestamp(3, later);

                    ResultSet rs = stmt.executeQuery();
                    ResultSetMetaData rsmd = rs.getMetaData();

                    while (rs.next()) {

                        appointmentId = rs.getString("appointment.appointmentId");
                        customerId = rs.getString("appointment.customerId");
                        title = rs.getString("appointment.title");
                        description = rs.getString("appointment.description");
                        location = rs.getString("appointment.location");
                        contact = rs.getString("appointment.contact");
                        url = rs.getString("appointment.url");
                        //Convert from UTC time in the database to local system time.
                        start = Appointments.convertToSystem(rs.getTimestamp("appointment.start"));
                        end = Appointments.convertToSystem(rs.getTimestamp("appointment.end"));

                    }

                } catch (SQLException e) {
                    System.out.println("Your SQL is not correct.");
                } catch (Exception re) {
                    System.out.println("The SQL looks right, something else went wrong");
                }
                if (!appointmentId.isEmpty()) {
                    Platform.runLater(() -> {

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("This is your reminder alert");
                        alert.setHeaderText("You have an appointment scheduled within the next 15 minutes");
                        alert.setContentText("Your appointment will begin shortly");

                        alert.showAndWait();

                    });
                }
                try {
                    sleep(420000);
                } catch (InterruptedException ie) {

                }
            }
        }

    }
}
