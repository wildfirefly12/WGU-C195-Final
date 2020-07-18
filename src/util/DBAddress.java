package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAddress {
    //database connection
    private static final Connection conn = DBConnection.openConnection();
    public static ObservableList<Address> addresses = FXCollections.observableArrayList();
    public static int addressId;

    //add new customer info
    public static void insertAddress(Address newAddress) {
        try {
            String insertString = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES (?, ?, ?, ?, ?, NOW(),?,NOW(),?)";
            PreparedStatement stmt = conn.prepareStatement(insertString);

            stmt.setString(1, newAddress.getAddress());
            stmt.setString(2, newAddress.getAddress2());
            stmt.setInt(3, newAddress.getCityId());
            stmt.setString(4, newAddress.getPostalCode());
            stmt.setString(5, newAddress.getPhone());
            stmt.setString(6, newAddress.getCreatedBy());
            stmt.setString(7, newAddress.getLastUpdateBy());
            stmt.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void getAddresses(){
        try{
            String query = "SELECT * FROM address"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                String address = rs.getString("address");
                String address2 = rs.getString("address2");
                String postalCode = rs.getString("postalCode");
                String phone = rs.getString("phone");
                Address newAddress = new Address(address, address2, postalCode, phone);
                addresses.add(newAddress);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static int getAddressId(String address){
        int addressId = 0;
        try {
            String query = "SELECT * FROM address WHERE address = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, address);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                addressId = rs.getInt("addressId");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return addressId;
    }

    //get address by id
    public static Address getAddress(int addressId){
        Address selectedAddress = null;
        try {
            String query = "SELECT * FROM address WHERE addressId = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, addressId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                String address = rs.getString("address");
                String address2 = rs.getString("address2");
                String postalCode = rs.getString("postalCode");
                String phone = rs.getString("phone");
                int cityId = rs.getInt("cityId");
                selectedAddress = new Address(address, address2, postalCode, phone);
                selectedAddress.setAddressId(addressId);
                selectedAddress.setCityId(cityId);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return selectedAddress;
    }

    //update first address field
    public static void updateAddress(Address address){
        try {
            String query = "UPDATE address " +
                    "SET address = ?, address2 = ?, postalCode = ?, phone = ?, lastUpdate = NOW(), lastUpdateBy = ? " +
                    "WHERE addressId = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, address.getAddress());
            stmt.setString(2, address.getAddress2());
            stmt.setString(3, address.getPostalCode());
            stmt.setString(4, address.getPhone());
            stmt.setString(5, address.getLastUpdateBy());
            stmt.setInt(6, address.getAddressId());
            stmt.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
