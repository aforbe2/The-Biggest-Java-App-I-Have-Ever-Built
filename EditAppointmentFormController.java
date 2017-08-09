package c195;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditAppointmentFormController implements Initializable {

    @FXML
    private DatePicker editDatePicker;

    @FXML
    private TextField apptIdField;

    @FXML
    private ChoiceBox titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField urlField;

    @FXML
    private TextField startField;

    @FXML
    private TextField endField;

    @FXML
    private Button closeBtn;

    @FXML
    private Button submitBtn;

    private static boolean isNew;

    public static void setIsNew(boolean setting) {
        isNew = setting;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        titleField.setItems(FXCollections.observableArrayList("Stand up", "New Customer", "Test", "Product"));

        titleField.getSelectionModel().select(2);

        if (!isNew) {
            AppointmentInstance a = AppointmentTableController.getSelectedAppointmentInstance();
            apptIdField.setText(a.getAppointmentId());

            descriptionField.setText(a.getDescription());

            locationField.setText(a.getLocation());

            contactField.setText(a.getContact());

            urlField.setText(a.getUrl());

        }
    }

    public void closeBtnListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        Parent root = FXMLLoader.load(getClass().getResource("AppointmentTable.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);

        titleField.getScene().getWindow().hide();

    }

    public void handleNewSubmitButton(ActionEvent event) {

        String appointmentId = apptIdField.getText();
        String title = (String) titleField.getSelectionModel().getSelectedItem();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String contact = contactField.getText();
        String url = urlField.getText();
        String start = startField.getText();
        String end = endField.getText();
        LocalDate date = editDatePicker.getValue();

        if (start.isEmpty() || end.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Times Entered");
            alert.setHeaderText("Please Enter Times");
            alert.setContentText("Appointment must have a time");

            alert.showAndWait();
            return;
        }

        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);

        LocalDateTime startDandT = startTime.atDate(date);  //complete start day and time
        LocalDateTime endDandT = endTime.atDate(date); //complete end day and time

        if (!Appointments.timeChecker(Timestamp.valueOf(startDandT), Timestamp.valueOf(endDandT))) {
            return;
        }

        ZonedDateTime start_UTC_time = startDandT.atZone(ZoneId.systemDefault());  //Start time in UTC for database
        start_UTC_time = start_UTC_time.withZoneSameInstant(ZoneId.of("UTC"));

        ZonedDateTime end_UTC_time = endDandT.atZone(ZoneId.systemDefault());  //End time in UTC for database
        end_UTC_time = end_UTC_time.withZoneSameInstant(ZoneId.of("UTC"));

        Timestamp startTS = Timestamp.valueOf(start_UTC_time.toLocalDateTime());
        Timestamp endTS = Timestamp.valueOf(end_UTC_time.toLocalDateTime());

        if (title.trim().isEmpty()
                || description.trim().isEmpty()
                || location.trim().isEmpty()
                || contact.trim().isEmpty()
                || url.trim().isEmpty()
                || start.trim().isEmpty()
                || end.trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Missing");
            alert.setHeaderText("One or more required fields are empty");
            alert.setContentText("Please fill out all fields before submitting");

            alert.showAndWait();

        } else if (!isNew) {

            try {

                PreparedStatement customerUpdate = DBconnection.getConnection().prepareStatement("UPDATE appointment "
                        + "SET title=\"" + title + "\" , description=\"" + description + "\", location=\""
                        + location + "\", contact=\"" + contact + "\", url=\"" + url + "\", start=\"" + startTS + "\", end=\"" + endTS + "\" WHERE appointmentId=" + appointmentId + ";");

                customerUpdate.executeUpdate();

            } catch (SQLException sq) {
                
            }

        }
        try {
            Stage primaryStage = new Stage();
            C195 currentWin = new C195();

            Parent root = FXMLLoader.load(getClass().getResource("AppointmentTable.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            currentWin.setNewStage(primaryStage);

            titleField.getScene().getWindow().hide();
        } catch (Exception e) {
            System.out.println("Unable to load appointment table");

        }
    }
}
