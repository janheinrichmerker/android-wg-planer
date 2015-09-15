package com.heinrichreimersoftware.wg_planer.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.heinrichreimersoftware.wg_planer.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    public static final String PATH_SD_IMAGES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Wilhelm-Gymnasium";

    public static boolean saveBitmapToSd(Bitmap bitmap, String filename) {
        if (bitmap == null) return false;
        /* Create folder if not exists */
        File directory = new File(PATH_SD_IMAGES);
        if (!directory.exists()) directory.mkdirs();

        File file = new File(directory, filename);

        /* Delete old file if exists */
        if (file.exists()) file.delete();

        Log.d(MainActivity.TAG, "Save bitmap to sd: " + file.getAbsolutePath());

        try {
            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            fOut.flush();
            fOut.close();

            if (file.exists()) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap loadBitmapFromSd(String filename) {
    	/* Create folder if not exists */
        File directory = new File(PATH_SD_IMAGES);
        if (!directory.exists()) return null;

        String filePath = PATH_SD_IMAGES + "/" + filename;

        Log.d(MainActivity.TAG, "Load bitmap from sd: " + filePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
