package util;

import model.Address;
import model.City;
import model.Country;
import model.Customer;

import static util.DBAddress.addresses;
import static util.DBCity.cities;
import static util.DBCountry.countries;
import static util.DBCustomer.customers;

public class Validation {

    //check if input is an integer
    public static Boolean isInt(String string){
        try{
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //check if input is a zip code (five digits)
    public static Boolean isZipCode(String string){
        Boolean returnValue = false;
        if(isInt(string)){
            if(string.length() == 5){
                returnValue = true;
            }
        }
        return returnValue;
    }

    //check if input is a phone number (ten digits)
    public static Boolean isPhone(String string){
        Boolean returnValue = false;
        if(isInt(string)){
            if(string.length() == 10){
                returnValue = true;
            }
        }
        return returnValue;
    }

    //check if input is null or empty
    public static Boolean isFilledOut(String string){
        if (string.isEmpty() || string.isBlank()) {
            return false;
        } else {
            return true;
        }
    }

    //check if country already exists (used to prevent duplicated countries)
    public static Boolean countryExists(String country){
        DBCountry.getCountries();
        Boolean returnValue = false;

        for(Country returnedCountry : countries ){
            String selectedCountry = returnedCountry.getCountry();

            if(country.equals(selectedCountry)) {
                //DBCountry.existingCountry = returnedCountry;
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    //check if city already exists
    public static Boolean cityExists(String city){
        DBCity.getCities();
        Boolean returnValue = false;

        for(City returnedCity : cities ){
            String selectedCity = returnedCity.getCity();

            if(city.equals(selectedCity)) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    //check if address already exists (used to prevent duplicated addresses)
    public static Boolean addressExists(String address, String city, String country){
        DBAddress.getAddresses();
        Boolean returnValue = false;

        for(Address returnedAddress : addresses){
            String selectedAddress = returnedAddress.getAddress();

            if(address.equals(selectedAddress) && cityExists(city) && countryExists(country)) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    //check if a customer with the same address in the same city/country already exists
    public static Boolean customerExists(String customer){
        Boolean returnValue = false;

        for(Customer returnedCustomer : customers ){
            String selectedCustomer = returnedCustomer.getCustomerName();

            if(customer.equals(selectedCustomer)) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

}
