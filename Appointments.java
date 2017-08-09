package c195;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.scene.control.Alert;

public class Appointments {

    public static Timestamp convertToSystem(Timestamp time) {

        ZonedDateTime timeInUTC = time.toLocalDateTime().atZone(ZoneId.of("UTC")); //Coming from UTC time

        timeInUTC = timeInUTC.withZoneSameInstant(ZoneId.systemDefault()); //Convert to system default

        LocalDateTime newTime = timeInUTC.toLocalDateTime();

        return Timestamp.valueOf(newTime);

    }

    public static boolean timeChecker(Timestamp start, Timestamp end) {

        LocalTime open = LocalTime.of(9, 0);
        LocalTime close = LocalTime.of(17, 0);
        Timestamp dbStart;
        Timestamp dbEnd;

        if (start.after(end)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Start/End Times");
            alert.setHeaderText("The Start Time Must Be Before the End Time");
            alert.setContentText("Please Enter a New Start and End Time");

            alert.showAndWait();
            return false;

        }

        try {

            LocalDateTime ldt = start.toLocalDateTime();
            LocalDate ld = ldt.toLocalDate();

            PreparedStatement ps = DBconnection.getConnection().prepareStatement("Select start, end FROM appointment"
                    + " WHERE start LIKE \"" + ld.toString() + "%\";");

            System.out.println(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dbStart = rs.getTimestamp("start");
                dbEnd = rs.getTimestamp("end");
                //Convert from UTC time in the database to local system time.
                dbStart = convertToSystem(dbStart);
                dbEnd = convertToSystem(dbEnd);

                if ((start.after(dbStart) && end.before(dbEnd)) //12-1 and 12:45 - 1:45
                        || (end.after(dbStart) && end.before(dbEnd)) //12-1 and 11:45 - 12:45
                        || (start.before(dbStart) && end.after(dbEnd))//12-1 and 11:45 - 1:45
                        || start.equals(dbStart)
                        || end.equals(dbEnd)) { //Perfect Overlap

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Appointment Time");
                    alert.setHeaderText("Appointment is Overlapping With Existing Appointment");
                    alert.setContentText("Please Choose a New Meeting Time");

                    alert.showAndWait();
                    return false;
                }


            }

        } catch (SQLException sqe) {
            System.out.println("Bad SQL in time check ps");
        }

        LocalDateTime ldt = start.toLocalDateTime();
        LocalDate ld = ldt.toLocalDate();

        Timestamp openTS = Timestamp.valueOf(LocalDateTime.of(ld, open));
        Timestamp closeTS = Timestamp.valueOf(LocalDateTime.of(ld, close));
        if ((start.before(openTS)) || (end.after(closeTS) || (end.before(openTS) || (start.after(closeTS))))) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Appointment Time");
            alert.setHeaderText("Appointment is Outside of Business Hours");
            alert.setContentText("Please Choose a Time Between 9:00 and 17:00");

            alert.showAndWait();
            return false;
        }

        return true;
    }
}
