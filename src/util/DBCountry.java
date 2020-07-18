package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCountry {
    private static final Connection conn = DBConnection.openConnection();
    public static ObservableList<Country> countries = FXCollections.observableArrayList(); //list of countries

    //insert country into database
    public static void insertCountry(Country newCountry) {
        try {
            String insertString = "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                    "VALUES (?, NOW(),?,NOW(),?)";
            PreparedStatement stmt = conn.prepareStatement(insertString);

            stmt.setString(1, newCountry.getCountry());
            stmt.setString(2, newCountry.getCreatedBy());
            stmt.setString(3, newCountry.getLastUpdateBy());
            stmt.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    //return all countries
    public static void getCountries(){
        try{
            String allCountries = "SELECT * FROM country"; //select statement
            PreparedStatement stmt = conn.prepareStatement(allCountries);
            ResultSet rs = stmt.executeQuery(allCountries);

            while(rs.next()){
                String country = rs.getString("country");//get name of country
                Country newCountry = new Country(country);//create new country object to add to list
                countries.add(newCountry);//add country object to list
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static int getCountryId(String country){
        int countryId = 0;
        try {
            String query = "SELECT * FROM country WHERE country = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                countryId = rs.getInt("countryId");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return countryId;
    }

    public static Country getCountry(int id){
        Country selectedCountry = null;
        try {
            String query = "SELECT * FROM country WHERE countryId = ?"; //select statement
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                String country = rs.getString("country");
                selectedCountry = new Country(country);
                selectedCountry.setCountryId(id);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return selectedCountry;
    }

    public static void updateCountry(Country country) {
        try {
            String query = "UPDATE country " +
                    "SET country = ?, lastUpdate = NOW(), lastUpdateBy = ?" +
                    "WHERE countryId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, country.getCountry());
            stmt.setString(2, User.getLoggedUser());
            stmt.setInt(3, country.getCountryId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
