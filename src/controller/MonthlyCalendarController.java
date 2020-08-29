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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Appointment;
import util.DBAppointment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class MonthlyCalendarController implements Initializable {

    @FXML
    private GridPane MonthlyCalendar;

    @FXML
    private Button PreviousButton;

    @FXML
    private Button NextButton;

    @FXML
    private Label MonthLabel;

    //create Calendar
    private static Calendar calendar = Calendar.getInstance();

    private static int currentYear = calendar.get(Calendar.YEAR);//get integer value of current year
    private static int selectedYear = currentYear;

    private static int currentMonth = calendar.get(Calendar.MONTH);//get integer value of current month
    private static int selectedMonth = currentMonth;

    private static int dayOfMonth;
    public static void setDayOfMonth(int day){
        dayOfMonth = day;
    }

    private static int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    private static int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public static Appointment selectedAppointment;

    public void getAppointments(String choice){
        if(choice == "Default" || choice == null){
            appointments = DBAppointment.getAllAppointments();
        } else {
            appointments = DBAppointment.getAppointmentsByCustomer(choice);
        }
    }

    //create day of month fields
    public BorderPane createDayPane(int date){
        //create border pane and add to the grid
        BorderPane datePane = new BorderPane();
        datePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //create date label and add to the grid
        Label dateLabel = new Label();
        dateLabel.setText(Integer.toString(date));
        datePane.setTop(dateLabel);
        BorderPane.setAlignment(dateLabel, Pos.TOP_RIGHT);
        Insets insets = new Insets(5);
        BorderPane.setMargin(dateLabel, insets);

        //create filtered list of appointments using a lambda expression to simplify it
        FilteredList<Appointment> appointmentFilteredList = new FilteredList<>(appointments, appointment ->
                appointment.getStart().toLocalDate().equals(LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate()));


        //create list view from filtered list
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

        //lambda used on event listener
        appointmentListView.getSelectionModel().selectedItemProperty().addListener((observableValue, appointment, t1) -> {
            selectedAppointment = appointmentListView.getSelectionModel().getSelectedItem();
        });

        //add appointment list to grid
        datePane.setCenter(appointmentListView);
        return datePane;

    }

    //add fields to calendar
    public void setCalendarDates(){
        int weekOfMonth = 0;
        MonthlyCalendar.getChildren().clear();
        MonthLabel.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()).toUpperCase());

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

    public static void resetMonthlyCalendar(){
        calendar = Calendar.getInstance();

        currentYear = calendar.get(Calendar.YEAR);//get integer value of current year
        selectedYear = currentYear;

        currentMonth = calendar.get(Calendar.MONTH);//get integer value of current month
        selectedMonth = currentMonth;

        setDayOfMonth(1);

        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }

    public void setMonthForward(){
        dayOfMonth = 1;
        calendar.set(selectedYear, selectedMonth, dayOfMonth);
        calendar.add(Calendar.MONTH, 1);
        selectedMonth = calendar.get(Calendar.MONTH);
        setCalendarDates();
    }

    public void setMonthBack(){
        dayOfMonth = 1;
        calendar.set(selectedYear, selectedMonth, dayOfMonth);
        calendar.add(Calendar.MONTH, -1);
        selectedMonth = calendar.get(Calendar.MONTH);
        setCalendarDates();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAppointments(MainController.getCustomerChoice());
        setCalendarDates();
        NextButton.setOnAction(e -> setMonthForward());
        PreviousButton.setOnAction(e -> setMonthBack());


    }


}
