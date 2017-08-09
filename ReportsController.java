package c195;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ReportsController {

    @FXML
    private Button typeBtn;

    @FXML
    private Button consultantBtn;

    public void typeByMonthListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        Parent root = FXMLLoader.load(getClass().getResource("TypeByMonthReport.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);

    }

    public void consultantScheduleListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        Parent root = FXMLLoader.load(getClass().getResource("ConsultantScheduleReport.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);

    }

    public void employeeRosterListener(ActionEvent event) throws Exception {

        Stage primaryStage = new Stage();
        C195 currentWin = new C195();

        Parent root = FXMLLoader.load(getClass().getResource("EmployeeRosterReport.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        currentWin.setNewStage(primaryStage);

    }

}
