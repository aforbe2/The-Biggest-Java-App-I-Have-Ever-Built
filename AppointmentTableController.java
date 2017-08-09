package c195;

import static c195.Appointments.convertToSystem;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AppointmentTableController implements Initializable {

    
    @FXML
    private Button editApptBtn;

    @FXML
    private TableView appointmentTable;

    @FXML
    private TableColumn appointmentIdCol;

    @FXML
    private TableColumn titleCol;

    @FXML
    private TableColumn descriptionCol;

    @FXML
    private TableColumn locationCol;

    @FXML
    private TableColumn contactCol;

    @FXML
    private TableColumn urlCol;

    @FXML
    private TableColumn startCol;

    @FXML
    private TableColumn endCol;
    
    private static AppointmentInstance selectedAppointmentInstance;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        appointmentTable.getItems().setAll(parseAppointmentInstanceList());

    }

    private List<AppointmentInstance> parseAppointmentInstanceList() {

        String appointmentId = new String();
        String title = new String();
        String description = new String();
        String location = new String();
        String contact = new String();
        String url = new String();
        Timestamp start;
        Timestamp end;

        ArrayList<AppointmentInstance> appointmentInstanceList = new ArrayList<>();

        try {
            PreparedStatement stmt = DBconnection.getConnection().prepareStatement("SELECT appointment.appointmentId, appointment.title, appointment.description, appointment.location, appointment.contact, appointment.url, appointment.start, appointment.end\n"
                    + "FROM appointment\n");

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {

                appointmentId = rs.getString("appointment.appointmentId");
                title = rs.getString("appointment.title");
                description = rs.getString("appointment.description");
                location = rs.getString("appointment.location");
                contact = rs.getString("appointment.contact");
                url = rs.getString("appointment.url");
                start = rs.getTimestamp("appointment.start");
                end = rs.getTimestamp("appointment.end");
                //Convert from UTC time in the database to local system time.
                start = convertToSystem(start);
                end = convertToSystem(end);

                appointmentInstanceList.add(new AppointmentInstance(appointmentId, title, description, location, contact, url, start.toString(), end.toString()));

            }

        } catch (SQLException e) {
            System.out.println("Your SQL is not correct.");
        } catch (Exception re) {
            System.out.println("The SQL looks right, something else went wrong");
        }

        return appointmentInstanceList;
    }
    
        public void editApptButtonListener(ActionEvent event) throws Exception {

        selectedAppointmentInstance = (AppointmentInstance) appointmentTable.getSelectionModel().getSelectedItem();
        EditAppointmentFormController.setIsNew(false);

        if (selectedAppointmentInstance == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Select an Appointment");
            alert.setHeaderText("No Appointment Selected to Edit");
            alert.setContentText("Please Select an Appointment to be Edited");

            alert.showAndWait();

        } else {
            Stage primaryStage = new Stage();
            C195 currentWin = new C195();

            Parent root = FXMLLoader.load(getClass().getResource("EditAppointmentForm.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            currentWin.setNewStage(primaryStage);
            
            appointmentTable.getScene().getWindow().hide();
        }

    }
    
        public static AppointmentInstance getSelectedAppointmentInstance() {
        return selectedAppointmentInstance;

    }
}
