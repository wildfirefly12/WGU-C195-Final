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
    private static Calendar monthlyCalendar = new Calendar.Builder().build();

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
