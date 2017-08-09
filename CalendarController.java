package c195;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CalendarController implements Initializable {

    @FXML
    private ChoiceBox monthBox;

    @FXML
    private TableView calendarMonthTable;

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

    private static CalendarMonth selectedCalendarMonth;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        monthBox.getItems().setAll(Month.values());

        monthBox.getSelectionModel().select(4);

        int month = monthBox.getSelectionModel().getSelectedIndex();

        String Monthstr = "2017-0" + month;

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        calendarMonthTable.getItems().setAll(parseCalendarMonthList());

        monthBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number newValue) {
                calendarMonthTable.getItems().setAll(parseCalendarMonthList());
            }
        });

    }

    private List<CalendarMonth> parseCalendarMonthList() {
        int month = monthBox.getSelectionModel().getSelectedIndex() + 1;

        String monthStr = "2017-0" + month + "%";

        String appointmentId = new String();
        String customerId = new String();
        String title = new String();
        String description = new String();
        String location = new String();
        String contact = new String();
        String url = new String();
        String start = new String();
        String end = new String();

        ArrayList<CalendarMonth> calendarMonthList = new ArrayList<>();

        try {
            PreparedStatement stmt = DBconnection.getConnection().prepareStatement("SELECT * FROM appointment WHERE start LIKE ?;");
            stmt.setString(1, monthStr);

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

                calendarMonthList.add(new CalendarMonth(appointmentId, customerId, title, description, location, contact, url, start, end));

            }

        } catch (SQLException e) {
            System.out.println("Your SQL is not correct.");
        } catch (Exception re) {
            System.out.println("The SQL looks right, something else went wrong");
        }

        return calendarMonthList;

    }

    public static CalendarMonth getSelectedCalendarMonth() {
        return selectedCalendarMonth;

    }

}
