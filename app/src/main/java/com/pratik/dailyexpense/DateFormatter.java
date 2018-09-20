package com.pratik.dailyexpense;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {


    public static String dateChanged;
    public static int year, month, day;



    public static String ChangeDateFormat(String selectedDate) {

        SimpleDateFormat inputSimpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        SimpleDateFormat outputSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateChanged = selectedDate;
        Date date = null;
        try {
            date = inputSimpleDateFormat.parse(selectedDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        dateChanged = outputSimpleDateFormat.format(date);

        return dateChanged;
    }
     public static String ChangeDateFormat2(String selectedDate) {

        SimpleDateFormat inputSimpleDateFormat = new SimpleDateFormat("M/d/yyyy");
        SimpleDateFormat outputSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dateChanged = selectedDate;
        Date date = null;
        try {
            date = inputSimpleDateFormat.parse(selectedDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        dateChanged = outputSimpleDateFormat.format(date);

        return dateChanged;
    }

}
