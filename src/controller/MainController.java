package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import util.DBCustomer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private AnchorPane mainRootPane;

    @FXML
    private TableView<?> MonthlyCalendarTable;

    @FXML
    private TableColumn<?, ?> MondayMonthlyColumn;

    @FXML
    private TableColumn<?, ?> TuesdayMonthlyColumn;

    @FXML
    private TableColumn<?, ?> WednesdayMonthlyColumn;

    @FXML
    private TableColumn<?, ?> ThursdayMonthlyColumn;

    @FXML
    private TableColumn<?, ?> FridayMonthlyColumn;

    @FXML
    private Button AddEventButton;

    @FXML
    private Button DeleteEventButton;

    @FXML
    private Button UpdateEventButton;

    @FXML
    private Button WeeklyButton;

    @FXML
    private TableView<Customer> CustomerTable;

    @FXML
    private TableColumn<Customer, String> CustomerColumn;

    @FXML
    private Button AddCustomerButton;

    @FXML
    private Button UpdateCustomerButton;

    @FXML
    private Button DeleteCustomerButton;

    @FXML
    private Button ExitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*ADD CUSTOMER WINDOW*/
        AddCustomerButton.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/AddCustomer.fxml"));
                Parent parent = fxmlLoader.load();
                Stage newWindow = new Stage();
                newWindow.initModality(Modality.APPLICATION_MODAL);
                newWindow.setTitle("Add Customer");
                newWindow.setScene(new Scene(parent));
                newWindow.show();
            } catch (IOException error) {
                error.printStackTrace();
            }
        });

        UpdateCustomerButton.setOnAction(e -> {
            selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No customer is selected.");
                alert.showAndWait();
            } else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/UpdateCustomer.fxml"));
                    Parent parent = fxmlLoader.load();
                    Stage newWindow = new Stage();
                    newWindow.initModality(Modality.APPLICATION_MODAL);
                    newWindow.setTitle("Update Customer");
                    newWindow.setScene(new Scene(parent));
                    newWindow.show();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        });

        DeleteCustomerButton.setOnAction(e -> {
            selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();
            System.out.println(selectedCustomer.getAddressId());
            DBCustomer.deleteCustomer(selectedCustomer);
            DBCustomer.customers.remove(selectedCustomer);
        });

        //update content to customer table
        DBCustomer.getCustomers();
        CustomerColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        CustomerTable.setItems(DBCustomer.customers);

        UpdateCustomerButton.setOnAction(e -> {
            selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No customer is selected.");
                alert.showAndWait();
            } else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/UpdateCustomer.fxml"));
                    Parent parent = fxmlLoader.load();
                    Stage newWindow = new Stage();
                    newWindow.initModality(Modality.APPLICATION_MODAL);
                    newWindow.setTitle("Update Customer");
                    newWindow.setScene(new Scene(parent));
                    newWindow.show();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        });

        AddEventButton.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/AddAppointment.fxml"));
                Parent parent = fxmlLoader.load();
                Stage newWindow = new Stage();
                newWindow.initModality(Modality.APPLICATION_MODAL);
                newWindow.setScene(new Scene(parent));
                newWindow.show();
            } catch (IOException error) {
                error.printStackTrace();
            }
        });
    }

    //get selected customer
    private static Customer selectedCustomer;
    public static void setSelectedCustomer(Customer selected){
        selectedCustomer = selected;
    }
    public static Customer getSelectedCustomer(){
        return selectedCustomer;
    }

}