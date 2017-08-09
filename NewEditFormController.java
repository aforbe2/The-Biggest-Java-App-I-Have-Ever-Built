package c195;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewEditFormController implements Initializable {

    @FXML
    private TextField custIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField address2Field;

    @FXML
    private TextField cityField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField zipCodeField;

    @FXML
    private Button closeBtn;

    private static boolean isNew;

    private final int active = 1;

    private static String customerId;

    public static String getCustomerId() {

        return customerId;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (!isNew) {
            Customer c = MainMenuController.getSelectedCustomer();
            custIdField.setText(c.getCustomerId());

            nameField.setText(c.getCustomerName());

            phoneField.setText(c.getPhoneNumber());

            addressField.setText(c.getStreetAddress());

            cityField.setText(c.getCity());

            countryField.setText(c.getCountry());

            zipCodeField.setText(c.getZipCode());

        }
    }

    public static void setIsNew(boolean setting) {
        isNew = setting;
    }

    public void closeBtnListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);

        custIdField.getScene().getWindow().hide();

    }

    public void handleNewSubmitButton(ActionEvent event) {

        customerId = custIdField.getText();
        String name = nameField.getText();
        String phoneNumber = phoneField.getText();
        String address = addressField.getText();
        String address2 = address2Field.getText();
        String city = cityField.getText();
        String country = countryField.getText();
        String zipCode = zipCodeField.getText();

        String countryId = new String();
        String cityId = new String();
        String addressId = new String();

        if (name.trim().isEmpty()
                || phoneNumber.trim().isEmpty()
                || address.trim().isEmpty()
                //Did not check address2 since many addresses do not have a second component
                || city.trim().isEmpty()
                || country.trim().isEmpty()
                || zipCode.trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Missing");
            alert.setHeaderText("One or more required fields are empty");
            alert.setContentText("Please fill out all fields before submitting");

            alert.showAndWait();

        } else {

            //Country
            try {
                Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT countryId FROM country WHERE country=\"" + country + "\";");

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    countryId = rs.getString("countryId");

                }
                if (countryId.isEmpty()) {

                    try {

                        PreparedStatement insertCountry = DBconnection.getConnection().prepareStatement("INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy)"
                                + "VALUES(\"" + country + "\", UTC_TIMESTAMP(),\"" + LogInController.getUserName() + "\", UTC_TIMESTAMP(), \"" + LogInController.getUserName() + "\");");

                        insertCountry.executeUpdate();

                        PreparedStatement getId = DBconnection.getConnection().prepareStatement("Select countryId FROM country WHERE country=?");
                        getId.setString(1, country);

                        rs = getId.executeQuery();

                        while (rs.next()) {
                            countryId = rs.getString("countryId");
                        }

                    } catch (SQLException sq) {
                        System.out.println("Bad SQL in Country ps");
                    }

                }

            } catch (SQLException sqe) {
                System.out.println("Bad SQL in country");
            }

            //City
            try {
                Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT cityId FROM city WHERE city=\"" + city + "\";");
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    cityId = rs.getString("cityId");

                }

                if (cityId.isEmpty()) {

                    try {

                        PreparedStatement insertCity = DBconnection.getConnection().prepareStatement("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)"
                                + "VALUES(\"" + city + "\", " + countryId + ", UTC_TIMESTAMP(), \"" + LogInController.getUserName() + "\", UTC_TIMESTAMP(), \"" + LogInController.getUserName() + "\");");

                        insertCity.executeUpdate();

                        PreparedStatement getId = DBconnection.getConnection().prepareStatement("Select cityId FROM city WHERE city=?");
                        getId.setString(1, city);

                        rs = getId.executeQuery();

                        while (rs.next()) {
                            cityId = rs.getString("cityId");
                        }

                    } catch (SQLException sq) {
                        System.out.println("Bad SQL in city ps");
                    }

                }
            } catch (SQLException cityException) {
                System.out.println("Bad SQL in city");
            }
            //Address
            if (!isNew) {

                try {

                    PreparedStatement getCustId = DBconnection.getConnection().prepareStatement("Select addressId FROM customer WHERE customerId=?");
                    getCustId.setString(1, customerId);
                    ResultSet rs = getCustId.executeQuery();

                    if (rs.next()) {
                        addressId = rs.getString("addressId");
                    }

                    PreparedStatement addressUpdate = DBconnection.getConnection().prepareStatement("UPDATE address "
                            + "SET address=\"" + address + "\" , address2=\"" + address2 + "\", cityId=\"" + cityId + "\", postalCode=\"" + zipCode + "\", phone=\"" + phoneNumber + "\", lastUpdate=UTC_TIMESTAMP(), lastUpdateBy=\""
                            + LogInController.getUserName() + "\" WHERE addressId=?");

                    addressUpdate.setString(1, addressId);

                    addressUpdate.executeUpdate();

                    PreparedStatement getId = DBconnection.getConnection().prepareStatement("Select addressId FROM address WHERE address=?");
                    getId.setString(1, address);

                    rs = getId.executeQuery();

                    while (rs.next()) {
                        addressId = rs.getString("addressId");
                    }

                } catch (SQLException sq) {
                    System.out.println("Bad SQL at line 228");
                }
            } else {
                try {
                    Connection conn = DBconnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement("SELECT addressId FROM address WHERE address=\"" + address + "\";");
                    System.out.println(ps.toString());
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        addressId = rs.getString("addressId");

                    }

                    if (addressId.isEmpty()) {
                        try {

                            PreparedStatement addressInsert = DBconnection.getConnection().prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)"
                                    + "VALUES(\"" + address + "\", \"" + address2 + "\", " + cityId + ", \"" + zipCode + "\", \"" + phoneNumber + "\", UTC_TIMESTAMP(), \"" + LogInController.getUserName() + "\", UTC_TIMESTAMP(),  \"" + LogInController.getUserName() + "\");");
                            System.out.println(addressInsert.toString());
                            addressInsert.executeUpdate();

                            PreparedStatement getId = DBconnection.getConnection().prepareStatement("Select addressId FROM address WHERE address=?");
                            getId.setString(1, address);

                            rs = getId.executeQuery();

                            while (rs.next()) {
                                addressId = rs.getString("addressId");
                            }

                        } catch (SQLException sq) {
                            System.out.println("Bad SQL in address PS");
                        }

                    }

                } catch (SQLException addressIdException) {
                    System.out.println("Bad SQL in address ps");
                }

            }
        }

        //Customer
        if (!isNew) {

            try {

                PreparedStatement customerUpdate = DBconnection.getConnection().prepareStatement("UPDATE customer "
                        + "SET customerName=\"" + name + "\" , lastUpdate=UTC_TIMESTAMP(), lastUpdateBy=\""
                        + LogInController.getUserName() + "\" WHERE customerId=" + customerId + ";");

                customerUpdate.executeUpdate();

            } catch (SQLException sq) {

            }

        } else {

            try {
                Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT customerId FROM customer WHERE customerName=\"" + name + "\";");
                System.out.println(ps.toString());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    customerId = rs.getString("customerId");
                }
                try {

                    PreparedStatement customerInsert = DBconnection.getConnection().prepareStatement("INSERT "
                            + "INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                            + "VALUES(\"" + name + "\", " + addressId + ", " + active + ", UTC_TIMESTAMP(), \"" + LogInController.getUserName() + "\", UTC_TIMESTAMP(), \"" + LogInController.getUserName() + "\");");

                    customerInsert.executeUpdate();

                } catch (SQLException sq) {
                    System.out.println("Bad SQL in customer insert ps");
                }

            } catch (SQLException customerIdException) {
                System.out.println("Bad SQL in customer ps");
            }
        }
        try {
            Stage primaryStage = new Stage();
            C195 currentWin = new C195();

            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            currentWin.setNewStage(primaryStage);
        } catch (Exception e) {
            System.out.println("Couldn't load main window");

        }
        custIdField.getScene().getWindow().hide();
    }
}
