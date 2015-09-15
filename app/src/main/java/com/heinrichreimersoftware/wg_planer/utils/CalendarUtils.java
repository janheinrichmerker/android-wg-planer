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

    public static String dayToString(int day) {
        switch (day) {
            case Calendar.MONDAY:
                return "Montag"; //TODO string resource
            case Calendar.TUESDAY:
                return "Dienstag";
            case Calendar.WEDNESDAY:
                return "Mitwoch";
            case Calendar.THURSDAY:
                return "Donnerstag";
            case Calendar.FRIDAY:
                return "Freitag";
            case Calendar.SATURDAY:
                return "Samstag";
            case Calendar.SUNDAY:
                return "Sonntag";
            default:
                return "";
        }
    }

    public static int monthNumberToMonth(int monthNumber) {
        switch (monthNumber) {
            case 1:
                return Calendar.JANUARY;
            case 2:
                return Calendar.FEBRUARY;
            case 3:
                return Calendar.MARCH;
            case 4:
                return Calendar.APRIL;
            case 5:
                return Calendar.MAY;
            case 6:
                return Calendar.JUNE;
            case 7:
                return Calendar.JULY;
            case 8:
                return Calendar.AUGUST;
            case 9:
                return Calendar.SEPTEMBER;
            case 10:
                return Calendar.OCTOBER;
            case 11:
                return Calendar.NOVEMBER;
            case 12:
                return Calendar.DECEMBER;
            default:
                return -1;
        }
    }
}
