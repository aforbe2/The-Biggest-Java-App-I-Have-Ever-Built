package c195;

import static c195.LogInController.logUser;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class C195 extends Application {

    private static Connection dbConn;
    private static Stage mainWindow;
    private Stage secondWindow;

    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));

        Scene scene = new Scene(root);

        mainWindow = primaryStage;
        mainWindow.setScene(scene);
        mainWindow.show();
    }

    public void setNewStage(Stage newStage) {

        secondWindow = newStage;
        secondWindow.show();
       
        if (mainWindow.isShowing()) {
            mainWindow.close();
        }

    }

    public static Connection getConnection() {
        return dbConn;
    }

    public static void main(String[] args) {

        DBconnection.connect();
        dbConn = DBconnection.getConnection();

        launch(args);
        logUser(Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())), false);
        DBconnection.closeConnection();
    }

}
