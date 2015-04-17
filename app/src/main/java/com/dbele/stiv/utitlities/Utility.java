package com.dbele.stiv.utitlities;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dbele on 3/23/2015.
 */
public class Utility {

    public static final String DATE_WATCHED_FORMAT = "dd-MM-yyyy";


    public static CharSequence getFormattedDate(CharSequence pattern, Date date) {
        DateFormat df = new DateFormat();
        return df.format(pattern, date);
    }

    public static Date parseStringToDate(String pattern, String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = (Date)formatter.parse(dateString);
        } catch (ParseException e) {
            Log.d(Utility.class.getName(), "Date parse collapsed");
        }
        return date;
    }


    public static String extractDescription(String description){
        int index = description.indexOf("\">");
        if (index != -1) {
            description = description.substring(index+2);
        }

        index = description.indexOf("<br");
        if (index != -1) {
            description = description.substring(0, index);
        }
        return description;
    }


    public static String extractImagePathFromDescription(String description){
        int index = description.indexOf("src=\"");
        if (index != -1) {
            description = description.substring(index+5);
        }

        index = description.indexOf("\"");
        if (index != -1) {
            description = description.substring(0, index);
        }
        return description;
   }



}

