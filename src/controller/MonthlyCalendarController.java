package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Appointment;
import util.DBAppointment;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.stream.Collectors;



public class MonthlyCalendarController implements Initializable {

    @FXML
    private GridPane MonthlyCalendar;

    @FXML
    private Button PreviousButton;

    @FXML
    private Button NextButton;

    //create Calendar
    private static Calendar calendar = Calendar.getInstance();

    private static int currentYear = calendar.get(Calendar.YEAR);//get integer value of current year
    private static int selectedYear = currentYear;

    private static int currentMonth = calendar.get(Calendar.MONTH);//get integer value of current month
    private static int selectedMonth = currentMonth;

    private static int dayOfMonth = 1;

    private static int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    private static int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    private ObservableList<Appointment> allAppointments = DBAppointment.getAllAppointments();


    //create day of month fields
    public BorderPane createDayPane(int date){
        BorderPane datePane = new BorderPane();

        Label dateLabel = new Label();
        dateLabel.setText(Integer.toString(date));
        datePane.setTop(dateLabel);
        BorderPane.setAlignment(dateLabel, Pos.TOP_RIGHT);
        Insets insets = new Insets(5);
        BorderPane.setMargin(dateLabel, insets);

        FilteredList<Appointment> appointmentFilteredList = new FilteredList<>(allAppointments, appointment -> appointment.getStart().getDayOfMonth() == date);

        ListView<Appointment> appointmentListView = new ListView<>();
        appointmentListView.setItems(appointmentFilteredList);
        appointmentListView.setCellFactory(param -> new ListCell<Appointment>() {
            @Override
            protected void updateItem(Appointment appointment, boolean empty){
                super.updateItem(appointment, empty);
                if(empty || appointment == null || appointment.getTitle() == null){
                    setText(null);
                } else {
                    setText(appointment.getTitle());
                }
            }
        });

        appointmentListView.getSelectionModel().getSelectedItems();
        datePane.setCenter(appointmentListView);
        return datePane;

    }

    //add fields to calendar
    public void setCalendarDates(){
        int weekOfMonth = 0;

        for(int i = 1; i <= maxDay; i++){
            calendar.set(selectedYear, selectedMonth, dayOfMonth);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek == 1) {
                weekOfMonth = weekOfMonth + 1;
            }
            MonthlyCalendar.add(createDayPane(dayOfMonth), dayOfWeek - 1, weekOfMonth);
            dayOfMonth = dayOfMonth + 1;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCalendarDates();

    }


}