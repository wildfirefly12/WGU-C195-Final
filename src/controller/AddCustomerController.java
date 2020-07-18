package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Address;
import model.City;
import model.Country;
import model.Customer;
import util.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    @FXML
    private Button SubmitButton;

    @FXML
    private Button CancelButton;

    @FXML
    private TextField CustomerNameField;

    @FXML
    private TextField AddressField;

    @FXML
    private TextField AptField;

    @FXML
    private TextField CityField;

    @FXML
    private TextField ZipcodeField;

    @FXML
    private TextField PhoneField;

    @FXML
    private TextField CountryField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CancelButton.setOnAction(e -> {
            Stage stage = (Stage) CancelButton.getScene().getWindow();
            stage.close();
        });

        SubmitButton.setOnAction(e -> {
            String name = CustomerNameField.getText();
            String address = AddressField.getText();
            String address2 = AptField.getText();
            String city = CityField.getText();
            String zipcode = ZipcodeField.getText();
            String phone = PhoneField.getText();
            String country = CountryField.getText();


            if(!Validation.isFilledOut(name) || !Validation.isFilledOut(address) || !Validation.isFilledOut(city) || !Validation.isFilledOut(city) || !Validation.isFilledOut(zipcode) || !Validation.isFilledOut(country)){
                Alert missingItems = new Alert(Alert.AlertType.ERROR);
                missingItems.setContentText("Please fill out required fields.");
                missingItems.show();
            } else if(!Validation.isInt(phone) || !Validation.isPhone(phone)) {
                Alert invalidPhone = new Alert(Alert.AlertType.ERROR);
                invalidPhone.setContentText("Please enter a valid phone number.");
                invalidPhone.show();
            } else if (!Validation.isInt(zipcode) || !Validation.isZipCode(zipcode)){
                Alert invalidZipCode = new Alert(Alert.AlertType.ERROR);
                invalidZipCode.setContentText("Please enter a valid zip code.");
                invalidZipCode.show();
            } else {
                //create objects to be passed to insert statements
                Country selectedCountry = new Country(country);
                City selectedCity = new City(city);
                Address selectedAddress = new Address(address, address2, zipcode, phone);
                Customer selectedCustomer = new Customer(name);

                //check if country already exists, if not add country
                if(!Validation.countryExists(country)){
                    DBCountry.insertCountry(selectedCountry);
                    selectedCountry.setCountryId(DBCountry.getCountryId(country));
                } else {
                    selectedCountry.setCountryId(DBCountry.getCountryId(country));
                }

                //check if city already exists, if not add city
                if(!Validation.cityExists(city)){
                    selectedCity.setCountryId(selectedCountry.getCountryId());
                    DBCity.insertCity(selectedCity);
                    selectedCity.setCityId(DBCity.getCityId(city));
                } else {
                    selectedCity.setCityId(DBCity.getCityId(city));
                }

                //add address
                selectedAddress.setCityId(selectedCity.getCityId());
                DBAddress.insertAddress(selectedAddress);
                selectedAddress.setAddressId(DBAddress.getAddressId(address));

                //add customer
                if(!Validation.customerExists(name)){
                    selectedCustomer.setAddressId(selectedAddress.getAddressId());
                    DBCustomer.insertCustomer(selectedCustomer);
                }

                //close window
                Stage currentWindow = (Stage) SubmitButton.getScene().getWindow();
                currentWindow.close();
            }
        });
    }



}


