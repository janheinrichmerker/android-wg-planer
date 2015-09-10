package com.heinrichreimersoftware.wg_planer.utils;

import android.graphics.Color;

import java.util.List;

public class ColorUtils {

    public static int grey(int color) {
        int average = (Math.max(Math.max(Color.red(color), Color.green(color)), Color.blue(color))
                + Math.min(Math.min(Color.red(color), Color.green(color)), Color.blue(color))) / 2;
        return Color.argb(Color.alpha(color), average, average, average);
    }

    public static int averageColor(List<Integer> colors) {
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        for (int i = 0; i < colors.size(); i++) {
            sumRed += Color.red(colors.get(i));
            sumGreen += Color.green(colors.get(i));
            sumBlue += Color.blue(colors.get(i));
        }
        return Color.rgb(
                sumRed / colors.size(),
                sumGreen / colors.size(),
                sumBlue / colors.size()
        );
    }

}