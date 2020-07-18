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

public class UpdateCustomerController implements Initializable {

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
            String postalCode = ZipcodeField.getText();
            String phone = PhoneField.getText();
            String country = CountryField.getText();

            int selectedCustomerId = MainController.getSelectedCustomer().getCustomerId();
            Customer selectedCustomer = DBCustomer.getCustomer(selectedCustomerId);
            Address selectedAddress = DBAddress.getAddress(selectedCustomer.getAddressId());
            City selectedCity = DBCity.getCity(selectedAddress.getCityId());
            Country selectedCountry = DBCountry.getCountry(selectedCity.getCountryId());

            if(!Validation.isFilledOut(name) &&
                !Validation.isFilledOut(address) &&
                !Validation.isFilledOut(address2) &&
                !Validation.isFilledOut(city) &&
                !Validation.isFilledOut(phone) &&
                !Validation.isFilledOut(postalCode) &&
                !Validation.isFilledOut(country))
            {

                    Alert missingItems = new Alert(Alert.AlertType.ERROR);
                    missingItems.setContentText("At least one text field needs filled out.");
                    missingItems.show();
            } else {
                if (Validation.isFilledOut(name)) {
                    selectedCustomer.setCustomerName(name);
                }

                if (Validation.isFilledOut(address)) {
                    selectedAddress.setAddress(address);
                }

                if (Validation.isFilledOut(address2)) {
                    selectedAddress.setAddress2(address2);
                }

                if (Validation.isFilledOut(postalCode)) {
                    selectedAddress.setPostalCode(postalCode);
                }

                if(Validation.isFilledOut(phone)){
                    selectedAddress.setPhone(phone);
                }

                if(Validation.isFilledOut(city)){
                    if(selectedCity.getCity() == city){
                        int newCity = DBCity.getCityId(city);
                        selectedAddress.setCityId(newCity);
                    }
                    selectedCity.setCity(city);
                }

                if(Validation.isFilledOut(country)){
                    selectedCountry.setCountry(country);
                }

                //update statements
                DBCustomer.updateCustomer(selectedCustomer);
                DBAddress.updateAddress(selectedAddress);
                DBCity.updateCity(selectedCity);
                DBCountry.updateCountry(selectedCountry);

                //close window
                Stage currentWindow = (Stage) SubmitButton.getScene().getWindow();
                currentWindow.close();
            }
        });
    }
}
