package c195;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CalendarWeekController implements Initializable {

    @FXML
    private TableView calendarWeekTable;

    @FXML
    private DatePicker weekPicker;

    @FXML
    private TableColumn appointmentIdCol;

    @FXML
    private TableColumn customerIdCol;

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

    private static CalendarWeek selectedCalendarWeek;

    private LocalDate selectedDate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        weekPicker.setShowWeekNumbers(true);

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        weekPicker.setOnAction(event -> {
            selectedDate = weekPicker.getValue();
            calendarWeekTable.getItems().setAll(parseCalendarWeekList());
        });

    }

    private List<CalendarWeek> parseCalendarWeekList() {

        String appointmentId = new String();
        String customerId = new String();
        String title = new String();
        String description = new String();
        String location = new String();
        String contact = new String();
        String url = new String();
        String start = new String();
        String end = new String();

        ArrayList<CalendarWeek> calendarWeekList = new ArrayList<>();

        try {
            PreparedStatement stmt = DBconnection.getConnection().prepareStatement("SELECT * FROM appointment"
                    + " WHERE start BETWEEN ? AND ?;");

            stmt.setString(1, selectedDate.toString());
            stmt.setString(2, selectedDate.plusDays(7).toString());

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
                start = rs.getString("appointment.start");
                end = rs.getString("appointment.end");

                calendarWeekList.add(new CalendarWeek(appointmentId, customerId, title, description, location, contact, url, start, end));

            }

        } catch (SQLException e) {
            System.out.println("Your SQL is not correct.");
        } catch (Exception re) {
            System.out.println("The SQL looks right, something else went wrong");
        }

        return calendarWeekList;

    }

    public static CalendarWeek getSelectedCalendarWeek() {
        return selectedCalendarWeek;

    }

}
