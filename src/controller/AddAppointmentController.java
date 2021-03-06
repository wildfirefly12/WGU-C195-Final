package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import util.DBAppointment;
import util.DBCustomer;
import util.DBUser;
import util.Validation;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    @FXML
    private TextField TitleField;

    @FXML
    private DatePicker DateField;

    @FXML
    private TextArea DescriptionText;

    @FXML
    private TextField LocationField;

    @FXML
    private ComboBox<String> ContactChoice;

    @FXML
    private TextField URLField;

    @FXML
    private ChoiceBox<String> TypeChoice;

    @FXML
    private ComboBox<String> StartTimeChoice;

    @FXML
    private ComboBox<String> EndTimeChoice;

    @FXML
    private Button SubmitButton;

    @FXML
    private Button CancelButton;

    private final DateTimeFormatter time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
    private ObservableList<String> types = FXCollections.observableArrayList();

    public AddAppointmentController(){
        //populate list of times
        LocalTime hour = LocalTime.MIN.plusHours(9);
        for(int i = 0; i <= 16; i++){
            appointmentTimes.add(hour.format(time));
            hour = hour.plusMinutes(30);
        }

        //populate list of types
        types.addAll("Meeting", "Phone call", "Video conference");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //populate choiceboxes
        StartTimeChoice.setItems(appointmentTimes);
        EndTimeChoice.setItems(appointmentTimes);
        ContactChoice.setItems(DBCustomer.getContacts());
        TypeChoice.setItems(types);

        CancelButton.setOnAction(e -> {
            Stage stage = (Stage) CancelButton.getScene().getWindow();
            stage.close();
        });

        SubmitButton.setOnAction(e -> {
            String title = TitleField.getText();
            String description = DescriptionText.getText();
            String contact = ContactChoice.getValue();
            String type = TypeChoice.getValue();
            String local = LocationField.getText();
            String url = URLField.getText();
            LocalDate date = DateField.getValue();
            String startTime = StartTimeChoice.getValue();
            String endTime = EndTimeChoice.getValue();
            String user = User.getLoggedUser();

            if(!Validation.isFilledOut(title) ||
                    !Validation.isFilledOut(description) ||
                    !Validation.isFilledOut(contact) ||
                    !Validation.isFilledOut(type) ||
                    !Validation.isFilledOut(local) ||
                    !Validation.isFilledOut(startTime) ||
                    !Validation.isFilledOut(endTime)
            ){
                Alert missingItems = new Alert(Alert.AlertType.ERROR);
                missingItems.setContentText("Please fill out all fields.");
                missingItems.show();
                return;
            }

            LocalDate today = LocalDateTime.now().toLocalDate();
            int compareDate = date.compareTo(today);

            if(compareDate < 0){
                Alert pastDate = new Alert(Alert.AlertType.ERROR);
                pastDate.setContentText("Please pick a future date.");
                pastDate.show();
                return;
            }

            LocalDateTime start = LocalDateTime.of(date, LocalTime.parse(startTime, time));
            LocalDateTime end = LocalDateTime.of(date, LocalTime.parse(endTime, time));

            int compareTime = start.compareTo(end);

            if(compareTime > 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The end time cannot be before the start time.");
                alert.show();
                return;
            }

            if(Validation.appointmentExists(start, end)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Appointment cannot overlap another appointment.");
                alert.show();
                return;
            }

            Appointment newAppointment = new Appointment(title, description, local, contact, type, url);
            newAppointment.setCustomerId(DBCustomer.getCustomerId(contact));
            newAppointment.setUserId(DBUser.getUserId(user));
            newAppointment.setStart(start);
            newAppointment.setEnd(end);
            newAppointment.setLocation(local);
            DBAppointment.insertAppointment(newAppointment);

            Stage stage = (Stage) SubmitButton.getScene().getWindow();
            stage.close();
        });

    }
}

