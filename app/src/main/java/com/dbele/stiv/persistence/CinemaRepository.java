package com.dbele.stiv.persistence;

import android.content.Context;
import com.dbele.stiv.utilities.JSONParser;
import com.dbele.stiv.model.Cinema;
import java.util.ArrayList;

public class CinemaRepository {

    private static ArrayList<Cinema> cinemas;

    public static ArrayList<Cinema> getCinemas(Context context) {
        if (cinemas == null) {
            cinemas = JSONParser.getCinemas(context);
        }
        return cinemas;
    }

    public static ArrayList<String> getCinemaNames(Context context) {
        ArrayList<String> cinemaNames = new ArrayList<>();
        for (Cinema cinema : getCinemas(context)) {
            cinemaNames.add(cinema.getName());
        }
        return cinemaNames;
    }
}
