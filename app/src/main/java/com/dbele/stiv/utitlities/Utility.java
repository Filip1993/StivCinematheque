package com.dbele.stiv.utitlities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dbele on 3/23/2015.
 */
public class Utility {

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

   public static String downloadImageAndStore(Context context, String strUrl, String fileName) {

       InputStream is = null;
       FileOutputStream fos = null;

       try {
           File dir = context.getApplicationContext().getExternalFilesDir(null);
           File file = new File(dir, "/" + fileName + ".jpg");
           if (file.exists()) {
               file.delete();
           }

           URL url = new URL(strUrl);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setDoInput(true);
           conn.connect();
           is = conn.getInputStream();
           fos = new FileOutputStream(file);

           byte[] buffer = new byte[1024];
           int len;
           while ((len = is.read(buffer))!= -1) {
               fos.write(buffer, 0, len);
           }
           return file.getAbsolutePath();
       } catch(Exception e) {
            return null;
       } finally {
           if (is!=null) {
               try {
                   is.close();
               } catch (IOException e) {}
           }
           if (fos!=null) {
               try {
                   fos.close();
               } catch (IOException e) {}
           }

       }
   }

}

