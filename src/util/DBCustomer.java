package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCustomer {
    //database connection
    private static final Connection conn = DBConnection.openConnection();
    public static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static int customerId;

    //add new customer info
    public static void insertCustomer(Customer newCustomer) {
        try {
            String insertString = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES (?, ?, 1,NOW(),?,NOW(),?)";
            PreparedStatement stmt = conn.prepareStatement(insertString);

            stmt.setString(1, newCustomer.getCustomerName());
            stmt.setInt(2, newCustomer.getAddressId());
            stmt.setString(3, newCustomer.getCreatedBy());
            stmt.setString(4, newCustomer.getLastUpdateBy());
            stmt.executeUpdate();

            customers.add(newCustomer);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void getCustomers() {
        try {
            String query = "SELECT * FROM customer"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String customer = rs.getString("customerName");
                int id = rs.getInt("customerId");
                int addressId = rs.getInt("addressId");
                int active = rs.getInt("active");;

                Customer newCustomer = new Customer(customer);
                newCustomer.setCustomerId(id);
                newCustomer.setAddressId(addressId);
                newCustomer.setActive(active);

                customers.add(newCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //get customer by id
    public static Customer getCustomer(int customerId){
        Customer selectedCustomer = null;
        try {
            String query = "SELECT * FROM customer WHERE customerId = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                String customer = rs.getString("customerName");
                int addressId = rs.getInt("addressId");
                selectedCustomer = new Customer(customer);
                selectedCustomer.setCustomerId(customerId);
                selectedCustomer.setAddressId(addressId);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return selectedCustomer;
    }

    public static int getCustomerId(String customer){
        int customerId = 0;
        try {
            String query = "SELECT * FROM customer WHERE customerName = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, customer);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                customerId = rs.getInt("customerId");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return customerId;
    }

    public static void updateCustomer(Customer customer) {
        try {
            String query = "UPDATE customer " +
                    "SET customerName = ?, lastUpdate = NOW(), lastUpdateBy = ?" +
                    "WHERE customerId = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, User.getLoggedUser());
            stmt.setInt(3, customer.getCustomerId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCustomer(Customer customer){
        try {
            String deleteCustomer = "DELETE FROM customer WHERE customerId = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(deleteCustomer);

            stmt.setInt(1, customer.getCustomerId());
            stmt.executeUpdate();

            String deleteAddress = "DELETE FROM address WHERE addressId = ?"; //select statement
            PreparedStatement statement = conn.prepareStatement(deleteAddress);

            statement.setInt(1, customer.getAddressId());
            statement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
