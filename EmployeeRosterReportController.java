package c195;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EmployeeRosterReportController implements Initializable {

    @FXML
    private TableView employeeTable;

    @FXML
    private TableColumn userIdCol;

    @FXML
    private TableColumn userNameCol;

    @FXML
    private TableColumn passwordCol;

    @FXML
    private TableColumn activeCol;

    private static Employee selectedEmployee;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));

        employeeTable.getItems().setAll(parseEmployeeList());
    }

    private List<Employee> parseEmployeeList() {

        String userId = new String();
        String userName = new String();
        String password = new String();
        String active = new String();

        ArrayList<Employee> employeeList = new ArrayList<>();

        try {
            PreparedStatement stmt = DBconnection.getConnection().prepareStatement("SELECT user.userId, user.userName, user.password, user.active\n"
                    + "FROM user\n");

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {

                userId = rs.getString("user.userId");
                userName = rs.getString("user.userName");
                password = rs.getString("user.password");
                active = rs.getString("user.active");

                employeeList.add(new Employee(userId, userName, password, active));

            }

        } catch (SQLException e) {
            System.out.println("Your SQL is not correct.");
        } catch (Exception re) {
            System.out.println("The SQL looks right, something else went wrong");
        }

        return employeeList;
    }

    public static Employee getSelectedEmployee() {
        return selectedEmployee;

    }

}
