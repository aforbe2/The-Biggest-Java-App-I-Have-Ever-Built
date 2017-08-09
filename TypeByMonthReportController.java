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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class TypeByMonthReportController implements Initializable {

    @FXML
    private TableView amountTable;

    @FXML
    private ChoiceBox monthField;

    @FXML
    private TableColumn titleCol;

    @FXML
    private TableColumn amountCol;

    private static Type selectedType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        monthField.getItems().setAll(Month.values());

        monthField.getSelectionModel().select(4);

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        amountTable.getItems().setAll(parseAmountList());

        monthField.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number newValue) {
                amountTable.getItems().setAll(parseAmountList());
            }
        });

    }

    private List<Type> parseAmountList() {
        int month = monthField.getSelectionModel().getSelectedIndex() + 1;

        String monthStr = "2017-0" + month + "%";

        String title = new String();
        String amount = new String();

        ArrayList<Type> amountList = new ArrayList<>();

        try {
            PreparedStatement stmt = DBconnection.getConnection().prepareStatement("SELECT title, count(title) FROM appointment WHERE start LIKE ? GROUP BY title;");
            stmt.setString(1, monthStr);

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {

                title = rs.getString("appointment.title");
                amount = rs.getString("count(title)");

                amountList.add(new Type(title, amount));

            }

        } catch (SQLException e) {
            System.out.println("Your SQL is not correct.");
        } catch (Exception re) {
            System.out.println("The SQL looks right, something else went wrong");
        }

        return amountList;
    }

    public static Type getSelectedType() {
        return selectedType;

    }

}
