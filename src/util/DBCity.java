package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.City;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCity {
    private static final Connection conn = DBConnection.openConnection();
    public static ObservableList<City> cities = FXCollections.observableArrayList(); //list of cities


    //insert country into database
    public static void insertCity(City newCity) {
        try {
            String insertString = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES (?, ?, NOW(),?,NOW(),?)";
            PreparedStatement stmt = conn.prepareStatement(insertString);

            stmt.setString(1, newCity.getCity());
            stmt.setInt(2, newCity.getCountryId());
            stmt.setString(3, newCity.getCreatedBy());
            stmt.setString(4, newCity.getLastUpdatedBy());
            stmt.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    //get list of cities
    public static void getCities(){
        try{
            String allCities = "SELECT * FROM city";
            PreparedStatement stmt = conn.prepareStatement(allCities);
            ResultSet rs = stmt.executeQuery(allCities);

            while(rs.next()){
                String city = rs.getString("city");

                City newCity = new City(city);
                cities.add(newCity);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static int getCityId(String city){
        int cityId = 0;
        try {
            String query = "SELECT * FROM city WHERE city = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                cityId = rs.getInt("cityId");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return cityId;
    }

    public static City getCity(int id){
        City selectedCity = null;
        try {
            String query = "SELECT * FROM city WHERE cityId = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                String city = rs.getString("city");
                int countryId = rs.getInt("countryId");
                selectedCity = new City(city);
                selectedCity.setCityId(id);
                selectedCity.setCountryId(countryId);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return selectedCity;
    }

    public static void updateCity(City city) {
        try {
            String query = "UPDATE city " +
                    "SET city = ?, lastUpdate = NOW(), lastUpdateBy = ?" +
                    "WHERE cityId = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, city.getCity());
            stmt.setString(2, User.getLoggedUser());
            stmt.setInt(3, city.getCityId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
