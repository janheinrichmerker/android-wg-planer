package com.heinrichreimersoftware.wg_planer.utils;

import android.graphics.Color;

import java.util.List;

public class ColorUtils {

    public static int desaturate(int color, double level) {
        /*
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[1] = (float) (hsv[1] * 1 - level);
		return Color.HSVToColor(hsv);
		*/

        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int average = (Math.max(Math.max(red, green), blue) + Math.min(Math.min(red, green), blue)) / 2;

        int newRed = (int) (red * (1 - level) + average * level);
        int newGreen = (int) (green * (1 - level) + average * level);
        int newBlue = (int) (blue * (1 - level) + average * level);

        return Color.argb(Color.alpha(color), newRed, newGreen, newBlue);
    }

    public static int grey(int color) {
        int average = (Math.max(Math.max(Color.red(color), Color.green(color)), Color.blue(color))
                + Math.min(Math.min(Color.red(color), Color.green(color)), Color.blue(color))) / 2;
        return Color.argb(Color.alpha(color), average, average, average);
    }

    public static int argbToRgb(int color) {
        return Color.rgb(Color.red(color), Color.green(color), Color.blue(color));
    }

    public static int averageColor(List<Integer> colors) {
        int averageColor = 0x0000;
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        for (int i = 0; i < colors.size(); i++) {
            sumRed += Color.red(colors.get(i));
            sumGreen += Color.green(colors.get(i));
            sumBlue += Color.blue(colors.get(i));
        }
        averageColor = Color.rgb(
                sumRed / colors.size(),
                sumGreen / colors.size(),
                sumBlue / colors.size()
        );
        return averageColor;
    }

}