package com.dbele.stiv.utitlities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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

    private static final int MOVIE_PICTURE_WIDTH = 252;
    private static final int MOVIE_PICTURE_HEIGHT = 405;

    private static final int MOVIE_TICKET_WIDTH = 512;
    private static final int MOVIE_TICKET_HEIGHT = 768;

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


           BitmapFactory.Options options=new BitmapFactory.Options();
           Bitmap bm = BitmapFactory.decodeStream(is,null,options);

           Bitmap resizedBitmap = getResizedBitmap(bm, MOVIE_PICTURE_WIDTH, MOVIE_PICTURE_HEIGHT);

           ByteArrayOutputStream bos = new ByteArrayOutputStream();
           resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , bos);
           byte[] buffer = bos.toByteArray();

           fos.write(buffer);
           /*
           byte[] buffer = new byte[1024];
           int len;
           while ((len = is.read(buffer))!= -1) {
               fos.write(buffer, 0, len);
           }
           */
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

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static String storeBitmap(Context context, String fileName, Bitmap bitmap) {

        FileOutputStream fos = null;
        try {
            File dir = context.getApplicationContext().getExternalFilesDir(null);
            File file = new File(dir, "/" + fileName + ".jpg");
            if (file.exists()) {
                file.delete();
            }

            fos = new FileOutputStream(file);

            Bitmap resizedBitmap = getResizedBitmap(bitmap, MOVIE_TICKET_WIDTH, MOVIE_TICKET_HEIGHT);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , bos);
            byte[] buffer = bos.toByteArray();

            fos.write(buffer);
            return file.getAbsolutePath();
        } catch(Exception e) {
            return null;
        } finally {
            if (fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {}
            }

        }
    }


}

