package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;


public class MonthlyCalendarController implements Initializable {

    @FXML
    private AnchorPane MonthlyCalendar;

    @FXML
    private Button PreviousButton;

    @FXML
    private Button NextButton;

    //create Calendar
    private static Calendar monthlyCalendar = Calendar.getInstance();

    private static int currentYear = monthlyCalendar.get(Calendar.YEAR);//get integer value of current year
    private static int selectedYear = currentYear;

    private static int currentMonth = monthlyCalendar.get(Calendar.MONTH);//get integer value of current month
    private static int selectedMonth = currentMonth;

    private static int dayOfMonth = 1;
    private static int maxDay = monthlyCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

    //print out date info for testing
    public static void printCalendar(){
        monthlyCalendar.set(currentYear, currentMonth, dayOfMonth);
        System.out.println(monthlyCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        System.out.println(monthlyCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(monthlyCalendar.get(Calendar.DAY_OF_WEEK));

        for(int i = 1; i <= maxDay; i++){

            System.out.println(monthlyCalendar.get(Calendar.DAY_OF_WEEK));
            dayOfMonth = dayOfMonth + 1;
            monthlyCalendar.set(currentYear, currentMonth, dayOfMonth);
        }
    }

    //create day of month fields
    public static BorderPane setDayPane(int date){
        BorderPane dayPane = new BorderPane();
        Label dateLabel = new Label();
        return dayPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }


}
