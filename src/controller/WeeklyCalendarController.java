package controller;

import com.mysql.cj.xdevapi.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Appointment;
import util.DBAppointment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static java.lang.StrictMath.ceil;

public class WeeklyCalendarController implements Initializable {

    @FXML
    private GridPane WeeklyCalendar;

    @FXML
    private Button PreviousButton;

    @FXML
    private Button NextButton;

    @FXML
    private Label MonthLabel;


    //create Calendar
    private static Calendar calendar = Calendar.getInstance();

    private int currentYear = calendar.get(Calendar.YEAR);//get integer value of current year
    private int selectedYear = currentYear;

    private int currentMonth = calendar.get(Calendar.MONTH);//get integer value of current month
    private int selectedMonth = currentMonth;

    private int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
    private int selectedWeek = currentWeek;

    private int dayOfMonth = 1;

    private int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    private int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    DateTimeFormatter hourFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    private ObservableList<Appointment> allAppointments = DBAppointment.getAllAppointments();
    public static Appointment selectedAppointment;

    public void addHourLabels(){
        LocalTime hour = LocalTime.MIDNIGHT.plusHours(9);

        for(int i = 1; i < 19; i++){
            Label hourLabel = new Label();
            hourLabel.setText(hour.format(hourFormat));
            hourLabel.setPrefWidth(90);
            hourLabel.setAlignment(Pos.CENTER);

            WeeklyCalendar.add(hourLabel, 0, i);

            hour = hour.plusMinutes(30);
        }
    }

    public void addDateLabels(){
        MonthLabel.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()).toUpperCase());

        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        WeeklyCalendar.setBorder(border);

        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1);
        }

        for(int i = 1; i < 8; i++){
            Label dateLabel = new Label();
            dateLabel.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
            dateLabel.setPrefWidth(90);
            dateLabel.setAlignment(Pos.CENTER);

            WeeklyCalendar.add(dateLabel, i, 0);

            addAppointments();

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public void addAppointments(){
        LocalTime hour = LocalTime.MIDNIGHT.plusHours(9);
        FilteredList<Appointment> appointmentFilteredList = new FilteredList<>(allAppointments, a -> a.getStart().toLocalDate().isEqual(LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate()));

        for(int i = 1; i < 19; i++){
            LocalTime finalHour = hour;
            FilteredList<Appointment> hourlyAppointments = new FilteredList<>(appointmentFilteredList, a -> a.getStart().toLocalTime().equals(finalHour));

            for(Appointment appt : hourlyAppointments){
                BackgroundFill gray = new BackgroundFill(Color.TEAL, CornerRadii.EMPTY, Insets.EMPTY);
                Background appointmentBackground = new Background(gray);
                TextArea appointment = new TextArea();
                appointment.setText(appt.getTitle());
                appointment.setPrefWidth(90);
                appointment.setPrefHeight(100);
                appointment.setBackground(appointmentBackground);
                appointment.setEditable(false);
                appointment.setWrapText(true);

                double length = ceil((Duration.between(appt.getStart(), appt.getEnd()).toMinutes()/30));

                WeeklyCalendar.add(appointment, calendar.get(Calendar.DAY_OF_WEEK), i, 1, (int)length);

                appointment.focusedProperty().addListener((obs, oldVal, newVal) -> selectedAppointment = appt);
            }

            hour = hour.plusMinutes(30);
        }
    }

    public void weekForward(){
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        WeeklyCalendar.getChildren().clear();
        addHourLabels();
        addDateLabels();
    }

    public void weekBack(){
        calendar.add(Calendar.DAY_OF_MONTH, -8);
        WeeklyCalendar.getChildren().clear();
        addHourLabels();
        addDateLabels();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         addHourLabels();
         addDateLabels();

         NextButton.setOnAction(e -> weekForward());
         PreviousButton.setOnAction(e -> weekBack());
    }
}
