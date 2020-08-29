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
import util.Validation;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    private Appointment appointment;


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

    public void setFieldPrompts(){
        if (WeeklyCalendarController.selectedAppointment == null) {
            appointment = MonthlyCalendarController.selectedAppointment;
        } else {
            appointment = WeeklyCalendarController.selectedAppointment;
        }

        TitleField.setText(appointment.getTitle());
        DateField.setValue(appointment.getStart().toLocalDate());
        StartTimeChoice.setValue(appointment.getStart().toLocalTime().toString());
        EndTimeChoice.setValue(appointment.getEnd().toLocalTime().toString());
        DescriptionText.setText(appointment.getDescription());
        LocationField.setText(appointment.getLocation());
        ContactChoice.setValue(appointment.getContact());
        TypeChoice.setValue(appointment.getType());
        URLField.setText(appointment.getUrl());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //populate text fields
        setFieldPrompts();

        //populate choiceboxes
        ContactChoice.setItems(DBCustomer.getContacts());
        TypeChoice.setItems(types);
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

            if(appointment == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No appointment is selected.");
                alert.showAndWait();
            } else {
                LocalDateTime start = null;
                LocalDateTime end = null;
                int compareDate = 0;
                LocalDate today = LocalDateTime.now().toLocalDate();

                //update title
                if(Validation.isFilledOut(title)){
                    appointment.setTitle(title);
                }

                //update description
                if(Validation.isFilledOut(description)){
                    appointment.setDescription(description);
                }

                //update contact/customer id
                if(ContactChoice.getValue() != null){
                    appointment.setContact(contact);
                    appointment.setCustomerId(DBCustomer.getCustomerId(contact));
                }

                //update appointment type
                if(TypeChoice.getValue() != null){
                    appointment.setType(type);
                }

                //update location
                if(LocationField != null){
                    appointment.setLocation(local);
                }

                //update url
                if(Validation.isFilledOut(URLField.toString())){
                    appointment.setUrl(url);
                }

                //update date
                if(DateField.getValue() != null){
                    LocalDate date = DateField.getValue();
                    LocalTime startTime = appointment.getStart().toLocalTime();
                    LocalTime endTime = appointment.getEnd().toLocalTime();
                    start = LocalDateTime.of(date, startTime);
                    end = LocalDateTime.of(date, endTime);
                    compareDate = date.compareTo(today);

                    if(compareDate < 0){
                        Alert missingItems = new Alert(Alert.AlertType.ERROR);
                        missingItems.setContentText("Please pick a future date.");
                        missingItems.show();
                        return;
                    } else {
                        appointment.setStart(start);
                        appointment.setEnd(end);
                    }
                }

                if(StartTimeChoice.getValue() != null && EndTimeChoice.getValue() != null){
                    LocalDate date = appointment.getStart().toLocalDate();
                    String startTime = StartTimeChoice.getValue();
                    String endTime = EndTimeChoice.getValue();
                    start = LocalDateTime.of(date, LocalTime.parse(startTime, time));
                    end = LocalDateTime.of(date, LocalTime.parse(endTime, time));
                    int compareTime = start.compareTo(end);

                    if(compareTime > 0){
                        Alert missingItems = new Alert(Alert.AlertType.ERROR);
                        missingItems.setContentText("The end time cannot be before the start time.");
                        missingItems.show();
                    } else {
                        appointment.setStart(start);
                        appointment.setEnd(end);
                    }
                }

                DBAppointment.updateAppointment(appointment);

            }

            Stage stage = (Stage) SubmitButton.getScene().getWindow();
            stage.close();
        });

    }
}

