package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBAppointment {
    private static final Connection conn = DBConnection.openConnection();
    public static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    //add appointment to db
    public static void insertAppointment(Appointment appointment) {
        try {
            String insertString = "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)";
            PreparedStatement stmt = conn.prepareStatement(insertString);

            stmt.setInt(1, appointment.getCustomerId());
            stmt.setInt(2, appointment.getUserId());
            stmt.setString(3, appointment.getTitle());
            stmt.setString(4, appointment.getDescription());
            stmt.setString(5, appointment.getLocation());
            stmt.setString(6, appointment.getContact());
            stmt.setString(7, appointment.getType());
            stmt.setString(8, appointment.getUrl());


            stmt.executeUpdate();

            appointments.add(appointment);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
