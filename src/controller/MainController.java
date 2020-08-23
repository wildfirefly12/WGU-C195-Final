package controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import util.DBAppointment;
import util.DBCustomer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.time.LocalDateTime.now;

public class MainController implements Initializable {
    @FXML
    private AnchorPane MainRootPane;

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

    @FXML
    private AnchorPane CalendarPane;

    @FXML
    private Button AddEventButton;

    @FXML
    private Button DeleteEventButton;

    @FXML
    private Button UpdateEventButton;

    @FXML
    private Button WeeklyButton;

    @FXML
    private Button MonthlyButton;

    @FXML
    private Button ByMonthButton;

    @FXML
    private ComboBox<String> ConsultantChoice;

    @FXML
    private ComboBox<String> CustomerChoice;

    //customer from choice box for schedule
    private static String customerChoice;
    public static void setCustomerChoice(String choice){
        customerChoice = choice;
    }
    public static String getCustomerChoice(){
        return customerChoice;
    }

    //load monthly calendar
    public void loadMonthlyCal() throws IOException{
        AnchorPane calendar = FXMLLoader.load(getClass().getClassLoader().getResource("view/MonthlyCalendar.fxml"));
        calendar.prefHeight(600);
        calendar.prefWidth(448);
        CalendarPane.getChildren().setAll(calendar);
    }

    public void loadWeeklyCal() throws IOException{
        AnchorPane calendar = FXMLLoader.load(getClass().getClassLoader().getResource("view/WeeklyCalendar.fxml"));
        calendar.prefHeight(600);
        calendar.prefWidth(448);
        CalendarPane.getChildren().setAll(calendar);
    }

    //get selected customer
    private static Customer selectedCustomer;
    public static Customer getSelectedCustomer(){
        return selectedCustomer;
    }

    public static void appointmentAlert(){

        ObservableList<Appointment> allAppointments = DBAppointment.getAllAppointments();
        for(Appointment a : allAppointments){
            if (a.getStart().isAfter(now()) && a.getStart().isBefore(now().plusMinutes(15))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Appointment in 15 minutes or less.");
                alert.show();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appointmentAlert();

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

        //load monthly calendar by default
        try {
            loadMonthlyCal();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //choose a customer to display appointments with that customer
        CustomerChoice.getItems().add("All Appointments");
        CustomerChoice.setItems(DBCustomer.getContacts());
        CustomerChoice.getItems().add("Default");
        CustomerChoice.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue< ? extends String> observable, String oldValue, String newValue) ->{
                    setCustomerChoice(newValue);
                    MonthlyCalendarController.resetMonthlyCalendar();
                    try {
                        loadMonthlyCal();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        MonthlyButton.setOnAction(e -> {
            try {
                MonthlyCalendarController.resetMonthlyCalendar();
                loadMonthlyCal();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        WeeklyButton.setOnAction(e -> {
            try {
                loadWeeklyCal();
            } catch (IOException ioException) {
                ioException.printStackTrace();
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

        UpdateEventButton.setOnAction(e -> {
            if (MonthlyCalendarController.selectedAppointment == null && WeeklyCalendarController.selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No appointment is selected.");
                alert.showAndWait();
            } else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/UpdateAppointment.fxml"));
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

        DeleteEventButton.setOnAction(e -> {
            Appointment appointment;
            if (WeeklyCalendarController.selectedAppointment == null) {
                appointment = MonthlyCalendarController.selectedAppointment;
            } else {
                appointment = WeeklyCalendarController.selectedAppointment;
            }
            DBAppointment.deleteAppointment(appointment);
        });

        ByMonthButton.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/Reports.fxml"));
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
}
