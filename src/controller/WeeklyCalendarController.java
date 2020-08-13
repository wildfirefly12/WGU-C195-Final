package controller;

import javafx.collections.ObservableList;
import model.Appointment;
import util.DBAppointment;

import java.util.Calendar;

public class WeeklyCalendarController {
    //create Calendar
    private static Calendar calendar = Calendar.getInstance();

    private static int currentYear = calendar.get(Calendar.YEAR);//get integer value of current year
    private static int selectedYear = currentYear;

    private static int currentMonth = calendar.get(Calendar.MONTH);//get integer value of current month
    private static int selectedMonth = currentMonth;

    private static int dayOfMonth = 1;

    private static int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    private static int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    private DBAppointment dbAppointment = new DBAppointment();
    private ObservableList<Appointment> allAppointments = dbAppointment.getAllAppointments();
    public static Appointment selectedAppointment;


}
