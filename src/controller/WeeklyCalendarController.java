package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.Appointment;
import util.DBAppointment;

import java.net.URL;
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

    private static int currentYear = calendar.get(Calendar.YEAR);//get integer value of current year
    private static int selectedYear = currentYear;

    private static int currentMonth = calendar.get(Calendar.MONTH);//get integer value of current month
    private static int selectedMonth = currentMonth;

    private static int dayOfMonth = 1;

    private static int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    private static int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    DateTimeFormatter hourFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    private DBAppointment dbAppointment = new DBAppointment();
    private ObservableList<Appointment> allAppointments = dbAppointment.getAllAppointments();
    public static Appointment selectedAppointment;

    public void addHourLabels(){
        LocalTime hour = LocalTime.MIDNIGHT.plusHours(9);

        for(int i = 0; i < 18; i++){
            Label hourLabel = new Label();
            hourLabel.setText(hour.format(hourFormat));
            hourLabel.setPrefWidth(90);
            hourLabel.setAlignment(Pos.CENTER);

            WeeklyCalendar.add(hourLabel, 0, i);

            hour = hour.plusMinutes(30);
            System.out.println(hour);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         addHourLabels();
    }
}
