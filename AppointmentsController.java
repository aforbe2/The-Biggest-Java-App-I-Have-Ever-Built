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
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AppointmentsController implements Initializable {

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
    private DatePicker apptDatePicker;

    private static int apptIdNumbers = 20;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleField.setItems(FXCollections.observableArrayList("Stand up", "New Customer", "Test", "Product"));

        titleField.getSelectionModel().select(2);
    }

    public void handleApptBtn(ActionEvent event) {

        Customer c = MainMenuController.getSelectedCustomer();

        String title = (String) titleField.getSelectionModel().getSelectedItem();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String contact = contactField.getText();
        String url = urlField.getText();
        String start = startField.getText();
        String end = endField.getText();
        LocalDate date = apptDatePicker.getValue();

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

        try {

            PreparedStatement appointmentInsert = DBconnection.getConnection().prepareStatement("INSERT INTO appointment "
                    + "(customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                    + "VALUES( " + c.getCustomerId() + ", \"" + title + "\", \"" + description + "\", \""
                    + location + "\", \"" + contact + "\", \"" + url + "\", \"" + startTS + "\", \"" + endTS + "\", UTC_TIMESTAMP(), \"" + LogInController.getUserName()
                    + "\", UTC_TIMESTAMP(), \"" + LogInController.getUserName() + "\");");

            appointmentInsert.executeUpdate();

        } catch (SQLException customerIdException) {
            System.out.println("Bad SQL in Appointment ps");
        }

        titleField.getScene().getWindow().hide();

    }

}
