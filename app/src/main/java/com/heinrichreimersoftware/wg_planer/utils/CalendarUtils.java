package com.heinrichreimersoftware.wg_planer.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarUtils {

    public static Calendar today() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

    public static Calendar tomorrow() {
        Calendar date = new GregorianCalendar();
        date.add(Calendar.DATE, 1);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

    public static Calendar nextMonday() {
        Calendar date = new GregorianCalendar();
        while (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            date.add(Calendar.DATE, 1);
        }
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

    public static boolean isFridayOrSaturday() {
        Calendar date = new GregorianCalendar();
        return date.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }
}
