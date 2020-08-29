package controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.ConsultantReport;
import model.MonthlyReport;
import util.DBAppointment;
import util.DBCustomer;
import util.DBUser;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AppointmentsByConsultantController implements Initializable {

    @FXML
    private TableView<ConsultantReport> ReportsTable;

    @FXML
    private TableColumn<ConsultantReport, String> TitleColumn;

    @FXML
    private TableColumn<ConsultantReport, String> CustomerColumn;

    @FXML
    private TableColumn<ConsultantReport, LocalDateTime> StartColumn;

    @FXML
    private TableColumn<ConsultantReport, LocalDateTime> EndColumn;

    @FXML
    private TableColumn<ConsultantReport, String> LocationColumn;

    @FXML
    private ChoiceBox<String> UserChoice;

    private static ObservableList<ConsultantReport> consultantReport = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        UserChoice.setItems(DBUser.getUserNames());
        UserChoice.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue< ? extends String> observable, String oldValue, String newValue) -> {
                    consultantReport.clear();
                    consultantReport = DBAppointment.getConsultantReport(newValue);

                    TitleColumn.setCellValueFactory(new PropertyValueFactory<ConsultantReport, String>("title"));
                    CustomerColumn.setCellValueFactory(new PropertyValueFactory<ConsultantReport, String>("customer"));
                    StartColumn.setCellValueFactory(new PropertyValueFactory<ConsultantReport, LocalDateTime>("start"));
                    EndColumn.setCellValueFactory(new PropertyValueFactory<ConsultantReport, LocalDateTime>("end"));
                    LocationColumn.setCellValueFactory(new PropertyValueFactory<ConsultantReport, String>("location"));
                    ReportsTable.setItems(consultantReport);
                });
    }
}
