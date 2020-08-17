package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class DBAppointment {
    private static final Connection conn = DBConnection.openConnection();
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    //add appointment to db
    public static void insertAppointment(Appointment appointment) {
        try {
            LocalDateTime localStart = appointment.getStart().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime localEnd = appointment.getEnd().atZone(ZoneId.systemDefault()).toLocalDateTime();

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
            stmt.setTimestamp(9, Timestamp.valueOf(localStart));
            stmt.setTimestamp(10, Timestamp.valueOf(localEnd));
            stmt.setString(11, appointment.getCreatedBy());
            stmt.setString(12, User.getLoggedUser());
            stmt.executeUpdate();

            appointments.add(appointment);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static ObservableList<Appointment> getAllAppointments(){
        appointments.clear();

        try {
            String getAppointments = "SELECT * FROM appointment";
            PreparedStatement stmt = conn.prepareStatement(getAppointments);
            ResultSet rs = stmt.executeQuery(getAppointments);

            while (rs.next()){
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String type = rs.getString("type");
                String url = rs.getString("url");
                LocalDate startDate = rs.getDate("start").toLocalDate();
                LocalTime startTime = rs.getTime("start").toLocalTime();
                LocalDateTime start = startDate.atTime(startTime);
                LocalDate endDate = rs.getDate("end").toLocalDate();
                LocalTime endTime = rs.getTime("end").toLocalTime();
                LocalDateTime end = endDate.atTime(endTime);

                Appointment appointment = new Appointment();
                appointment.setAppointmentId(appointmentId);
                appointment.setCustomerId(customerId);
                appointment.setUserId(userId);
                appointment.setTitle(title);
                appointment.setDescription(description);
                appointment.setLocation(location);
                appointment.setContact(contact);
                appointment.setType(type);
                appointment.setUrl(url);
                appointment.setStart(start);
                appointment.setEnd(end);

                    appointments.add(appointment);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointments;
    }

    public static void updateAppointment(Appointment appointment){
        try {
            LocalDateTime localStart = appointment.getStart().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime localEnd = appointment.getEnd().atZone(ZoneId.systemDefault()).toLocalDateTime();

            String query = "UPDATE appointment " +
                    "SET customerId = ?, userId = ?, title = ?, description = ?, location = ?, contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdate = NOW(), lastUpdateBy = ? " +
                    "WHERE appointmentId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, appointment.getCustomerId());
            stmt.setInt(2, appointment.getUserId());
            stmt.setString(3, appointment.getTitle());
            stmt.setString(4, appointment.getDescription());
            stmt.setString(5, appointment.getLocation());
            stmt.setString(6, appointment.getContact());
            stmt.setString(7, appointment.getType());
            stmt.setString(8, appointment.getUrl());
            stmt.setTimestamp(9, Timestamp.valueOf(localStart));
            stmt.setTimestamp(10, Timestamp.valueOf(localEnd));
            stmt.setString(11, User.getLoggedUser());
            stmt.setInt(12, appointment.getAppointmentId());
            stmt.executeUpdate();

            appointments.remove(appointment);
            appointments.add(appointment);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void deleteAppointment(Appointment appointment){
        try {
            String query = "DELETE FROM appointment " +
                    "WHERE appointmentId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, appointment.getAppointmentId());

            stmt.executeUpdate();

            appointments.remove(appointment);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static Boolean ifAppointmentExists(LocalDateTime apptStart){
        Appointment appointment = new Appointment();

        try {
            String query = "SELECT * FROM appointment" +
                    " WHERE ? BETWEEN start AND end;";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);

            stmt.setTimestamp(1, Timestamp.valueOf(apptStart));

            while (rs.next()){
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String type = rs.getString("type");
                String url = rs.getString("url");
                LocalDate startDate = rs.getDate("start").toLocalDate();
                LocalTime startTime = rs.getTime("start").toLocalTime();
                LocalDateTime start = startDate.atTime(startTime);
                LocalDate endDate = rs.getDate("end").toLocalDate();
                LocalTime endTime = rs.getTime("end").toLocalTime();
                LocalDateTime end = endDate.atTime(endTime);

                appointment.setAppointmentId(appointmentId);
                appointment.setCustomerId(customerId);
                appointment.setUserId(userId);
                appointment.setTitle(title);
                appointment.setDescription(description);
                appointment.setLocation(location);
                appointment.setContact(contact);
                appointment.setType(type);
                appointment.setUrl(url);
                appointment.setStart(start);
                appointment.setEnd(end);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(appointment != null){
            return true;
        }
        return false;
    }

}
