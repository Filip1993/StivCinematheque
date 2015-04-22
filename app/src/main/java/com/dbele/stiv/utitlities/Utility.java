package com.dbele.stiv.utitlities;

import android.text.format.DateFormat;
import java.util.Date;

public class Utility {

    public static final String DATE_WATCHED_FORMAT = "dd-MM-yyyy";

    public static CharSequence getFormattedDate(CharSequence pattern, Date date) {
        return DateFormat.format(pattern, date);
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

