package c195;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MainMenuController implements Initializable {

    @FXML
    private Button appointmentsBtn;
    
    @FXML
    private Button appointmentTableBtn;

    @FXML
    private Button reportsBtn;

    @FXML
    private Button calendarBtn;

    @FXML
    private Button newBtn;

    @FXML
    private Button editBtn;

    @FXML
    private TableView customerTable;

    @FXML
    private TableColumn customerIdCol;

    @FXML
    private TableColumn customerNameCol;

    @FXML
    private TableColumn phoneNumberCol;

    @FXML
    private TableColumn streetAddressCol;

    @FXML
    private TableColumn cityCol;

    @FXML
    private TableColumn addressIdCol;

    @FXML
    private TableColumn zipCodeCol;

    private static Customer selectedCustomer;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        streetAddressCol.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        addressIdCol.setCellValueFactory(new PropertyValueFactory<>("addressId"));
        zipCodeCol.setCellValueFactory(new PropertyValueFactory<>("zipCode"));

        customerTable.getItems().setAll(parseCustomerList());

    }

    private List<Customer> parseCustomerList() {

        String customerId = new String();
        String customerName = new String();
        String phoneNumber = new String();
        String streetAddress = new String();
        String city = new String();
        String addressId = new String();
        String zipCode = new String();

        ArrayList<Customer> customerList = new ArrayList<>();

        try {
            PreparedStatement stmt = DBconnection.getConnection().prepareStatement("SELECT customer.customerId, customer.customerName, address.phone, address.address, city.city, address.addressId, address.postalCode\n"
                    + "FROM customer\n"
                    + "JOIN address ON customer.addressId = address.addressId\n"
                    + "JOIN city ON address.cityId = city.cityId;"
            );
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {

                customerId = rs.getString("customer.customerId");
                customerName = rs.getString("customer.customerName");
                phoneNumber = rs.getString("address.phone");
                streetAddress = rs.getString("address.address");
                city = rs.getString("city.city");
                addressId = rs.getString("address.addressId");
                zipCode = rs.getString("address.postalCode");

                customerList.add(new Customer(customerId, customerName, phoneNumber, streetAddress, city, addressId, zipCode));

            }

        } catch (SQLException e) {
            System.out.println("Your SQL is not correct.");
        } catch (Exception re) {
            System.out.println("The SQL looks right, something else went wrong");
        }
        return customerList;
    }

    public void apptButtonListener(ActionEvent event) throws Exception {

        selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        NewEditFormController.setIsNew(false);

        if (selectedCustomer == null) {

            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Select Customer!");
            alert.setHeaderText("No Customer Selected for appointment");
            alert.setContentText("Please select a customer to make an appointment with");

            alert.showAndWait();

        } else {
            Stage primaryStage = new Stage();
            C195 currentWin = new C195();

            Parent root = FXMLLoader.load(getClass().getResource("Appointments.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            currentWin.setNewStage(primaryStage);
        }

    }

    public void reportButtonListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        Parent root = FXMLLoader.load(getClass().getResource("Reports.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);

    }

    public void CalendarButtonListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        Parent root = FXMLLoader.load(getClass().getResource("Calendar.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);

    }

    public void newButtonListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        NewEditFormController.setIsNew(true);

        Parent root = FXMLLoader.load(getClass().getResource("newEditForm.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);
        customerTable.getScene().getWindow().hide();

    }

    public void editButtonListener(ActionEvent event) throws Exception {

        selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        NewEditFormController.setIsNew(false);

        if (selectedCustomer == null) {

            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Select Customer!");
            alert.setHeaderText("No Customer Selected to edit");
            alert.setContentText("Please select a customer to be edited");

            alert.showAndWait();

        } else {

            Stage primaryStage = new Stage();
            C195 currentWin = new C195();

            Parent root = FXMLLoader.load(getClass().getResource("newEditForm.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            currentWin.setNewStage(primaryStage);
            customerTable.getScene().getWindow().hide();
        }
    }
    
        public void appointmentTableListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        Parent root = FXMLLoader.load(getClass().getResource("AppointmentTable.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);

    }
        
        public void CalendarWeekButtonListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        Parent root = FXMLLoader.load(getClass().getResource("CalendarWeek.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);

    }

    public static Customer getSelectedCustomer() {
        return selectedCustomer;

    }

}
