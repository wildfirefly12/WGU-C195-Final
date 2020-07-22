package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;


public class MonthlyCalendarController implements Initializable {

    @FXML
    private GridPane MonthlyCalendar;

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
    private static int dayOfWeek = monthlyCalendar.get(Calendar.DAY_OF_WEEK);

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
    public static BorderPane createDayPane(int date){
        BorderPane datePane = new BorderPane();

        Label dateLabel = new Label();
        dateLabel.setText(Integer.toString(date));
        datePane.setTop(dateLabel);
        BorderPane.setAlignment(dateLabel, Pos.TOP_RIGHT);
        Insets insets = new Insets(5);
        BorderPane.setMargin(dateLabel, insets);



        return datePane;
    }

    //add fields to calendar
    public void setCalendarDates(){
        int weekOfMonth = 0;

        for(int i = 1; i <= maxDay; i++){
            monthlyCalendar.set(selectedYear, selectedMonth, dayOfMonth);
            dayOfWeek = monthlyCalendar.get(Calendar.DAY_OF_WEEK);
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
