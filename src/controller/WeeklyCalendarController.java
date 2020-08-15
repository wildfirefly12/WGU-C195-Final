package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Appointment;
import util.DBAppointment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.ResourceBundle;

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

    private DBAppointment dbAppointment = new DBAppointment();
    private ObservableList<Appointment> allAppointments = dbAppointment.getAllAppointments();
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
        FilteredList<Appointment> appointmentFilteredList = new FilteredList<>(allAppointments, appointment -> appointment.getStart().toLocalDate().isEqual(LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate()));
        LocalTime hour = LocalTime.MIDNIGHT.plusHours(9);

        for(int i = 1; i < 19; i++){
            LocalTime finalHour = hour;
            FilteredList<Appointment> hourlyAppointments = new FilteredList<>(appointmentFilteredList, a -> a.getStart().toLocalTime().equals(finalHour));

            for(Appointment appointment : hourlyAppointments){
                BackgroundFill gray = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
                Background appointmentBackground = new Background(gray);
                Label title = new Label();
                title.setText(appointment.getTitle());
                title.setPrefWidth(90);
                title.setPrefHeight(100);
                title.setAlignment(Pos.CENTER);
                title.setBackground(appointmentBackground);
                int length = appointment.getEnd().getHour() - appointment.getStart().getHour();

                System.out.println(length);

                WeeklyCalendar.add(title, i, calendar.get(Calendar.DAY_OF_WEEK), 1, length);
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
