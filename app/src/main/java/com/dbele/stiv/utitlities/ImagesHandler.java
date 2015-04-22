package com.dbele.stiv.utitlities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImagesHandler {

    private static final int MOVIE_PICTURE_WIDTH = 252;
    private static final int MOVIE_PICTURE_HEIGHT = 405;
    private static final int MOVIE_TICKET_WIDTH = 512;
    private static final int MOVIE_TICKET_HEIGHT = 768;
    private static final String DELIMITER = "/";
    private static final String EXTENSION = ".jpg";

    public static String storeBitmap(Context context, String fileName, Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            File file = getFile(context, fileName);
            fos = new FileOutputStream(file);
            Bitmap resizedBitmap;
            if(bitmap.getHeight()>bitmap.getWidth()) {
                resizedBitmap = getResizedBitmap(bitmap, MOVIE_TICKET_WIDTH, MOVIE_TICKET_HEIGHT);
            } else {
                resizedBitmap = getResizedBitmap(bitmap, MOVIE_TICKET_HEIGHT, MOVIE_TICKET_WIDTH);
            }
            byte[] buffer = getBytesFromBitmap(resizedBitmap);
            fos.write(buffer);
            return file.getAbsolutePath();
        } catch(Exception e) {
            Log.e(ImagesHandler.class.getName(), "Exception", e);
        } finally {
            if (fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(ImagesHandler.class.getName(), "Exception", e);
                }
            }
        }
        return null;
    }

    public static String downloadImageAndStore(Context context, String strUrl, String fileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File file = getFile(context, fileName);
            fos = new FileOutputStream(file);

            HttpURLConnection conn = getHttpURLConnection(strUrl);
            is = conn.getInputStream();
            Bitmap bm = getBitmap(is);
            Bitmap resizedBitmap = getResizedBitmap(bm, MOVIE_PICTURE_WIDTH, MOVIE_PICTURE_HEIGHT);
            byte[] buffer = getBytesFromBitmap(resizedBitmap);

            fos.write(buffer);
            return file.getAbsolutePath();
        } catch(Exception e) {
            Log.e(ImagesHandler.class.getName(), "Exception", e);
        } finally {
            if (is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(ImagesHandler.class.getName(), "Exception", e);
                }
            }
            if (fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(ImagesHandler.class.getName(), "Exception", e);
                }
            }
        }
        return null;
    }

    private static File getFile(Context context, String fileName) {
        File dir = context.getApplicationContext().getExternalFilesDir(null);
        File file = new File(dir, DELIMITER + fileName + EXTENSION);
        if (file.exists()) {
            file.delete();
        }
        return file;
    }

    private static HttpURLConnection getHttpURLConnection(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();
        return conn;
    }

    private static Bitmap getBitmap(InputStream is) {
        BitmapFactory.Options options=new BitmapFactory.Options();
        return BitmapFactory.decodeStream(is,null,options);
    }

    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return  Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    private static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , bos);
        return bos.toByteArray();
    }
}
