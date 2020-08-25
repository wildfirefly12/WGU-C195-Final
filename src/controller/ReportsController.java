package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.MonthlyReport;
import util.DBAppointment;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private TableView<MonthlyReport> ReportsTable;

    @FXML
    private TableColumn<MonthlyReport, String> MonthColumn;

    @FXML
    private TableColumn<MonthlyReport, String> AppointmentsColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<MonthlyReport> monthlyReport = DBAppointment.getMonthlyReport();
        MonthColumn.setCellValueFactory(new PropertyValueFactory<MonthlyReport, String>("month"));
        AppointmentsColumn.setCellValueFactory(new PropertyValueFactory<MonthlyReport, String>("moncountth"));
        ReportsTable.setItems(monthlyReport);
    }
}
