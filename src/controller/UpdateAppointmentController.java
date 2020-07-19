package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import util.DBAppointment;
import util.DBCustomer;
import util.DBUser;
import util.Validation;

import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {

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

    public UpdateAppointmentController(){
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

            Appointment newAppointment = new Appointment();
            LocalDateTime start;
            LocalDateTime end;
            LocalDate today = LocalDateTime.now().toLocalDate();
            int compareDate = date.compareTo(today);
            String user = User.getLoggedUser();

            //update title
            if(Validation.isFilledOut(title)){
                newAppointment.setTitle(title);
            }

            //update description
            if(Validation.isFilledOut(description)){
                newAppointment.setDescription(description);
            }

            //update contact/customer id
            if(Validation.isFilledOut(contact)){
                newAppointment.setContact(contact);
                newAppointment.setCustomerId(DBCustomer.getCustomerId(contact));
            }

            //update appointment type
            if(Validation.isFilledOut(type)){
                newAppointment.setType(type);
            }

            //update location
            if(Validation.isFilledOut(local)){
                newAppointment.setLocation(local);
            }

            //update url
            if(Validation.isFilledOut(url)){
                newAppointment.setUrl(url);
            }

            //update start and end times
            if(Validation.isFilledOut(startTime) || Validation.isFilledOut(endTime)){
                start = LocalDateTime.of(date, LocalTime.parse(startTime, time));
                end = LocalDateTime.of(date, LocalTime.parse(endTime, time));
                int compareTime = start.compareTo(end);

                if(compareTime > 0){
                    Alert missingItems = new Alert(Alert.AlertType.ERROR);
                    missingItems.setContentText("The end time cannot be before the start time.");
                    missingItems.show();
                } else {
                    newAppointment.setStart(start);
                    newAppointment.setEnd(end);
                }

            }


            if(compareDate < 0){
                Alert missingItems = new Alert(Alert.AlertType.ERROR);
                missingItems.setContentText("Please pick a future date.");
                missingItems.show();
            } else {
                newAppointment.set
            }

            Stage stage = (Stage) SubmitButton.getScene().getWindow();
            stage.close();
        });

    }
}
