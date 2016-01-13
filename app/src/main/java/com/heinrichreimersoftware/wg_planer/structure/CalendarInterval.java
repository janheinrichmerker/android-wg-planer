package com.heinrichreimersoftware.wg_planer.structure;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarInterval {
    private Calendar startTime;
    private Calendar endTime;

    public CalendarInterval(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public CalendarInterval(long startTimeMillis, long endTimeMillis) {
        Calendar startTime = new GregorianCalendar();
        startTime.setTimeInMillis(startTimeMillis);
        this.startTime = startTime;

        Calendar endTime = new GregorianCalendar();
        endTime.setTimeInMillis(endTimeMillis);
        this.endTime = endTime;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarInterval interval = (CalendarInterval) o;

        if (!startTime.equals(interval.startTime)) return false;
        if (!endTime.equals(interval.endTime)) return false;
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(startTime.getTime()) + " - " + sdf.format(endTime.getTime());
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public long getStartTimeMillis() {
        return startTime.getTimeInMillis();
    }

    public void setStartTimeMillis(long startTimeMillis) {
        Calendar startTime = new GregorianCalendar();
        startTime.setTimeInMillis(startTimeMillis);
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public long getEndTimeMillis() {
        return endTime.getTimeInMillis();
    }

    public void setEndTimeMillis(long endTimeMillis) {
        Calendar endTime = new GregorianCalendar();
        endTime.setTimeInMillis(endTimeMillis);
        this.endTime = endTime;
    }

}