package com.dbele.stiv.json;

import android.content.Context;
import android.util.Log;
import com.dbele.stiv.model.Cinema;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSONParser {

    private static final String FILENAME = "cinemas_json.txt";
    private static final String ENCODING= "UTF-8";

    public static ArrayList<Cinema> getCinemas(Context context) {
        ArrayList<Cinema> cinemas = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(FILENAME), ENCODING));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                cinemas.add(Cinema.createCinema(array.getJSONObject(i)));
            }
        } catch (IOException | JSONException e) {
            Log.e(JSONParser.class.getName(), "unable to parse JSON", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(JSONParser.class.getName(), "unable to parse JSON", e);
                }
            }
        }
        return cinemas;
    }
}
