package controller;


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
import java.time.LocalTime;
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
    private ChoiceBox<String> ContactChoice;

    @FXML
    private TextField URLField;

    @FXML
    private ChoiceBox<String> TypeChoice;

    @FXML
    private ChoiceBox<LocalTime> StartTimeChoice;

    @FXML
    private ChoiceBox<LocalTime> EndTimeChoice;

    @FXML
    private Button SubmitButton;

    @FXML
    private Button CancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String title = TitleField.getText();
        String description = DescriptionText.getText();
        String contact = ContactChoice.getValue();
        String type = TypeChoice.getValue();
        String local = LocationField.getText();
        String url = URLField.getText();
        LocalDate date = DateField.getValue();
        String user = User.getLoggedUser();

        CancelButton.setOnAction(e -> {
            Stage stage = (Stage) CancelButton.getScene().getWindow();
            stage.close();
        });

        SubmitButton.setOnAction(e -> {
            Appointment newAppointment = new Appointment(title, description, local, contact, type, url);
            newAppointment.setUserId(DBUser.getUserId(user));
            newAppointment.setCustomerId(DBCustomer.getCustomerId(contact));


        });

    }
}

