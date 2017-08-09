package c195;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ConsultantScheduleReportController implements Initializable {

    @FXML
    private TableView scheduleTable;

    @FXML
    private TableColumn appointmentIdCol;

    @FXML
    private TableColumn titleCol;

    @FXML
    private TableColumn descriptionCol;

    @FXML
    private TableColumn locationCol;

    @FXML
    private TableColumn startCol;

    @FXML
    private TableColumn endCol;

    private static Schedule selectedSchedule;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        scheduleTable.getItems().setAll(parseScheduleList());
    }

    private List<Schedule> parseScheduleList() {

        String appointmentId = new String();
        String title = new String();
        String description = new String();
        String location = new String();
        Timestamp start;
        Timestamp end;

        ArrayList<Schedule> scheduleList = new ArrayList<>();

        try {
            PreparedStatement stmt = DBconnection.getConnection().prepareStatement("SELECT * FROM appointment WHERE contact=?");

            stmt.setString(1, LogInController.getUserName());

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {

                appointmentId = rs.getString("appointment.appointmentId");
                title = rs.getString("appointment.title");
                description = rs.getString("appointment.description");
                location = rs.getString("appointment.location");
                start = Appointments.convertToSystem(rs.getTimestamp("appointment.start"));
                end = Appointments.convertToSystem(rs.getTimestamp("appointment.end"));

                scheduleList.add(new Schedule(appointmentId, title, description, location, start.toString(), end.toString()));

            }

        } catch (SQLException e) {
            System.out.println("Your SQL is not correct.");
        } catch (Exception re) {
            System.out.println("The SQL looks right, something else went wrong");
        }

        return scheduleList;
    }

    public static Schedule getSelectedSchedule() {
        return selectedSchedule;

    }

}
