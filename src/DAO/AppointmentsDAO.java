package DAO;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Appointment;
import model.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static javafx.collections.FXCollections.observableArrayList;

public abstract class AppointmentsDAO {
    private static ObservableList<Appointment> allApptsList = observableArrayList();
    private static ObservableList<Appointment> currMonthList = observableArrayList();
    private static ObservableList<Appointment> currWeekList = observableArrayList();
    private static ObservableList<Appointment> loginApptList = observableArrayList();

    public static ObservableList<Appointment> getAllApptData() {
        try {
            // SQL statement to get all customers from customer table
            String sql = "SELECT * FROM appointments";

            // Get a connection to DB and send over the SQL
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            // Get results of query
            ResultSet rs = ps.executeQuery();

            // Clear apptList
            allApptsList.clear();

            // Set bind variables to create appt object, add appt to list
            while(rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                int custId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String description = rs.getString("Description");
                Timestamp startTimestamp = rs.getTimestamp("Start");
                // LocalDateTime startDateTime = startTimestamp.toLocalDateTime();
                Timestamp endTimestamp = rs.getTimestamp("End");
                //LocalDateTime endDateTime = endTimestamp.toLocalDateTime();
                String type = rs.getString("Type");
                Appointment appt = new Appointment (apptId, custId, userId, contactId, title, description, location, type, startTimestamp, endTimestamp);
                allApptsList.add(appt);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Return apptList from db
        return allApptsList;
    }

    public static ObservableList<Appointment> getCurrMonthApptData() {
        try {
            // SQL statement to get all customers from customer table
            String sql = "SELECT * FROM appointments WHERE MONTH(Start) = MONTH(NOW())";

            // Get a connection to DB and send over the SQL
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            // Get results of query
            ResultSet rs = ps.executeQuery();

            // Clear apptList
            currMonthList.clear();

            // Set bind variables to create appt object, add appt to list
            while(rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                int custId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String description = rs.getString("Description");
                Timestamp startTimestamp = rs.getTimestamp("Start");
                // LocalDateTime startDateTime = startTimestamp.toLocalDateTime();
                Timestamp endTimestamp = rs.getTimestamp("End");
                //LocalDateTime endDateTime = endTimestamp.toLocalDateTime();
                String type = rs.getString("Type");
                Appointment appt = new Appointment (apptId, custId, userId, contactId, title, description, location, type, startTimestamp, endTimestamp);
                currMonthList.add(appt);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Return apptList from db
        return currMonthList;
    }

    public static ObservableList<Appointment> getCurrWeekApptData() {
        try {
            // SQL statement to get all customers from customer table
            String sql = "SELECT * FROM appointments WHERE WEEK(start) = WEEK(NOW())";

            // Get a connection to DB and send over the SQL
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            // ps.setTimestamp(1, timestampNow);

            // Get results of query
            ResultSet rs = ps.executeQuery();

            // Clear apptList
            currWeekList.clear();

            // Set bind variables to create appt object, add appt to list
            while(rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                int custId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String description = rs.getString("Description");
                Timestamp startTimestamp = rs.getTimestamp("Start");
                // LocalDateTime startDateTime = startTimestamp.toLocalDateTime();
                Timestamp endTimestamp = rs.getTimestamp("End");
                //LocalDateTime endDateTime = endTimestamp.toLocalDateTime();
                String type = rs.getString("Type");
                Appointment appt = new Appointment (apptId, custId, userId, contactId, title, description, location, type, startTimestamp, endTimestamp);
                currWeekList.add(appt);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Return apptList from db
        return currWeekList;
    }

    public static int addAppt(int custId, int userId, int contactId, String title, String description, String location, String type, Timestamp startTimestamp, Timestamp endTimestamp) {
        int apptId = 0;
        int overlappingAppts = overlapCheck(custId, startTimestamp, endTimestamp);

        if (overlappingAppts == 0) {
            try {
                // SQL statement to insert customer in customers table
                String sql = "INSERT INTO appointments (Customer_ID, User_ID, Contact_ID, Title, Description, Location, Type, Start, End) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                // Get connection to DB and send over the SQL
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                // Call prepared statement setter method to assign bind variables value
                ps.setInt(1, custId);
                ps.setInt(2, userId);
                ps.setInt(3, contactId);
                ps.setString(4, title);
                ps.setString(5, description);
                ps.setString(6, location);
                ps.setString(7, type);
                ps.setTimestamp(8, startTimestamp);
                ps.setTimestamp(9, endTimestamp);

                // Execute the insert, get returned customer id
                ps.execute();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                apptId = rs.getInt(1);

            } catch (SQLException throwables) {
                // Catch if errors with SQL
                throwables.printStackTrace();
            }
        }
        else {
            Alert alert;

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Error");
            alert.setContentText("Appointment could not be added because of overlapping appointment(s).");
            alert.showAndWait();
            return apptId;
        }

        // return rowsAffected;
        return apptId;
    }

    public static int updateAppt(int apptId, int custId, int userId, int contactId, String title, String description, String location, String type, Timestamp startTimestamp, Timestamp endTimestamp ) {
        Alert alert;
        int rowsAffected = 0;
        int overlappingAppts = modifyOverlapCheck(custId, apptId, startTimestamp, endTimestamp);

        if (overlappingAppts == 0) {
            alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to edit appointment #" + apptId + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // SQL statement to insert customer in customers table
                    String sql = "Update appointments SET Customer_ID = ?, User_ID =?, Contact_ID =?, Title =?, Description =?, Location =?, Type =?, Start =?, End =? WHERE Appointment_ID = ?";

                    // Get connection to DB and send over the SQL
                    PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

                    // Call prepared statement setter method to assign bind variables value
                    ps.setInt(1, custId);
                    ps.setInt(2, userId);
                    ps.setInt(3, contactId);
                    ps.setString(4, title);
                    ps.setString(5, description);
                    ps.setString(6, location);
                    ps.setString(7, type);
                    ps.setTimestamp(8, startTimestamp);
                    ps.setTimestamp(9, endTimestamp);
                    ps.setInt(10, apptId);

                    // Execute the update, assign number of rows affected and return
                    rowsAffected = ps.executeUpdate();
                    return rowsAffected;
                } catch (SQLException throwables) {
                    // Catch if errors with SQL
                    throwables.printStackTrace();
                }
            }
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setContentText("Appointment could not be updated because of overlapping appointment(s).");
            alert.showAndWait();
            return rowsAffected;
        }

        // return number of rows affected
        return rowsAffected;
    }

    public static int deleteAppt(Appointment apptToDelete) {
        Alert alert;
        int rowsAffected = 0;
        int apptId = apptToDelete.getId();
        String apptType = apptToDelete.getType();

        // Confirm user wants to delete customer & delete
        alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete appointment #" + apptId + " of type: " + apptType + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // SQL statement to run
                String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";

                // Get a connection to DB and send over the SQL
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

                // Call prepared statement setter method to assign bind variables value
                ps.setInt(1, apptId);

                // Var of updated rows to return
                rowsAffected = ps.executeUpdate();
                return rowsAffected;

            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowsAffected;
    }

    public static int overlapCheck(int newCustId, Timestamp start, Timestamp end) {
        int overlappingRows = 0;

        try {
            // SQL statement to check for customer id with conflicting appointment times
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ? AND (((? >= Start) AND (? < End)) OR " +
                    "((? > START) AND (? <= End)) OR ((? <= Start) AND (? >= End)))";

            // Get connection to DB and send over the SQL
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            // Call prepared statement setter method to assign bind variables value
            ps.setInt(1, newCustId);
            ps.setTimestamp(2, start);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
            ps.setTimestamp(5, end);
            ps.setTimestamp(6, start);
            ps.setTimestamp(7, end);

            // Get results of query
            ResultSet rs = ps.executeQuery();

            // Get and return number of rows that overlap
            while(rs.next()) {
                overlappingRows++;
            }
            return overlappingRows;
        }
        catch (SQLException throwables) {
            // Catch if errors with SQL
            throwables.printStackTrace();
        }

        // return number of rows overlapping
        return overlappingRows;
    }

    public static int modifyOverlapCheck(int existingCustId, int existingApptId, Timestamp start, Timestamp end) {
        int overlappingRows = 0;

        try {
            // SQL statement to check if overlapping appointments
            String sql = "SELECT * FROM appointments WHERE Appointment_ID != ? AND (Customer_ID = ? AND (((? >= Start) AND (? < End)) " +
                    "OR ((? > START) AND (? <= End)) OR ((? <= Start) AND (? >= End))))";

            // Get connection to DB and send over the SQL
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            // Call prepared statement setter method to assign bind variables value
            ps.setInt(1, existingApptId);
            ps.setInt(2, existingCustId);
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, start);
            ps.setTimestamp(5, end);
            ps.setTimestamp(6, end);
            ps.setTimestamp(7, start);
            ps.setTimestamp(8, end);

            // Get results of query
            ResultSet rs = ps.executeQuery();

            // Get and return number of rows that overlap
            while(rs.next()) {
                overlappingRows++;
            }
            return overlappingRows;
        }
        catch (SQLException throwables) {
            // Catch if errors with SQL
            throwables.printStackTrace();
        }

        // return number of rows overlapping
        return overlappingRows;
    }

    public static void apptLoginCheck() {
        Alert alert;
        LocalDateTime ldtNow = LocalDateTime.now();
        LocalDateTime ldtPlusMins = ldtNow.plusMinutes(15);
        Timestamp timestampNow = Timestamp.valueOf(ldtNow);
        Timestamp timestampPlusMins = Timestamp.valueOf(ldtPlusMins);
        User currentUser = UserDAO.getCurrentUser();
        int currentUserId = currentUser.getId();
        String apptsToPrint = "";

        try {
            // SQL statement to insert customer in customers table
            String sql = "SELECT * FROM Appointments WHERE User_ID = ? AND ((Start >= ?) AND (Start <= ?))";


            // Get connection to DB and send over the SQL
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            // Call prepared statement setter method to assign bind variables value
            ps.setInt(1, currentUserId);
            ps.setTimestamp(2, timestampNow);
            ps.setTimestamp(3, timestampPlusMins);

            // Get results of query
            ResultSet rs = ps.executeQuery();

            // Clear apptList
            // loginApptList.clear();

            // Set bind variables to create appt object, add appt to overlapping appt list
            while(rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                apptsToPrint = (apptsToPrint.concat(apptId + "                ") );
                int custId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String description = rs.getString("Description");
                Timestamp startTimestamp = rs.getTimestamp("Start");
                LocalDate startDate = (startTimestamp.toLocalDateTime()).toLocalDate();
                apptsToPrint = (apptsToPrint + startDate + "      ");
                LocalTime startTime = (startTimestamp.toLocalDateTime()).toLocalTime();
                apptsToPrint = (apptsToPrint + startTime + " - ");
                Timestamp endTimestamp = rs.getTimestamp("End");
                LocalTime endDateTime = (endTimestamp.toLocalDateTime()).toLocalTime();
                apptsToPrint = (apptsToPrint + endDateTime + "\n");
                String type = rs.getString("Type");
                Appointment appt = new Appointment (apptId, custId, userId, contactId, title, description, location, type, startTimestamp, endTimestamp);
                loginApptList.add(appt);
            }

            // Check if there are appointments in login appointment list and alert user
            if (loginApptList.isEmpty()) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Reminder");
                alert.setContentText("You do not have any scheduled appointments in the next 15 minutes.");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Reminder");
                alert.setContentText("You have the following appointment(s) in the next 15 minutes:\n" +
                        "Appt Id:       Date:                Time:\n" + apptsToPrint);
                alert.showAndWait();
            }
        }
        catch (SQLException throwables) {
            // Catch if errors with SQL
            throwables.printStackTrace();
        }
    }

}
