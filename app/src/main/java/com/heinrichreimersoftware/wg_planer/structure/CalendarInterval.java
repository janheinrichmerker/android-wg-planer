package com.heinrichreimersoftware.wg_planer.structure;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarInterval {

    public Calendar startTime;
    public Calendar endTime;

    public CalendarInterval() {
    }

    public CalendarInterval(Calendar startTime,
                            Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarInterval representation = (CalendarInterval) o;

        if (startTime != representation.startTime) return false;
        if (endTime != representation.endTime) return false;
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(startTime.getTime()) + " - " + sdf.format(endTime.getTime());
    }

}