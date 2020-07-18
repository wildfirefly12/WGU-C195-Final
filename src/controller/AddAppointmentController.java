package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import util.DBCustomer;
import util.DBUser;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public AddAppointmentController(){
        //populate list of times
        LocalTime hour = LocalTime.MIN.plusHours(9);
        for(int i = 0; i <= 16; i++){
            appointmentTimes.add(hour.format(time));
            hour = hour.plusMinutes(30);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        StartTimeChoice.setItems(appointmentTimes);
        EndTimeChoice.setItems(appointmentTimes);

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
            LocalDateTime start = LocalDateTime.of(date, LocalTime.parse(startTime, time));
            LocalDateTime end = LocalDateTime.of(date, LocalTime.parse(endTime, time));

            Appointment newAppointment = new Appointment(title, description, local, contact, type, url);
            newAppointment.setUserId(DBUser.getUserId(user));
            newAppointment.setCustomerId(DBCustomer.getCustomerId(contact));
            newAppointment.setStart(start);
            newAppointment.setEnd(end);

            Stage stage = (Stage) SubmitButton.getScene().getWindow();
            stage.close();
        });

    }
}

