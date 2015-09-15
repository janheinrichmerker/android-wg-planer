package com.heinrichreimersoftware.wg_planer.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class BitmapUtils {

    public static final String PATH_SD_IMAGES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Wilhelm-Gymnasium";

    public static boolean saveBitmapToSd(Context context, Bitmap bitmap, String filename) {
        try {
            FileOutputStream fOut = context.openFileOutput(filename, Context.MODE_PRIVATE);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            fOut.flush();
            fOut.close();

            return Arrays.asList(context.fileList()).contains(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap loadBitmapFromSd(Context context, String filename) {
        if (!Arrays.asList(context.fileList()).contains(filename)) return null;
        try {
            FileInputStream fIn = context.openFileInput(filename);


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeStream(fIn);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
