package com.heinrichreimersoftware.wg_planer.utils.factories;

import com.heinrichreimersoftware.wg_planer.structure.CalendarInterval;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.Representation;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class LessonTimeFactory {

    public static CalendarInterval fromLessonNumber(int lessonNumber) {
        /* Startzeit der ersten Stunde */
        Calendar startTime = new GregorianCalendar();
        startTime.set(Calendar.HOUR_OF_DAY, 7);
        startTime.set(Calendar.MINUTE, 45);
        startTime.set(Calendar.SECOND, 0);

    	/* Länge einer Stunde in Minuten */
        int lessonLength = 45;

    	/* Länge der normalen Pause */
        int normalBreakLength = 5;
    	
    	/* Pausen definieren, die vom Standard abweichen ('Stunde, nachdem die Pause hinzugefügt werden soll', 'Länge in Minuten') */
        int[][] breakAfter = {
                {2, 15},
                {4, 15},
                {6, 0}
        };

    	/* So oft Stundenlänge hinzufügen, wie benötigt */
        for (int i = 1; i < lessonNumber; i++) {
            startTime.add(Calendar.MINUTE, lessonLength);

            boolean unnormalBreak = false;
            for (int[] aBreakAfter : breakAfter) {
                if (aBreakAfter[0] == i) {
                    unnormalBreak = true;
					/* Wenn nötig spezielle Pause statt der normalen hinzufügen */
                    startTime.add(Calendar.MINUTE, aBreakAfter[1]);
                }
            }
            if (!unnormalBreak) {
                startTime.add(Calendar.MINUTE, normalBreakLength);
            }
        }

        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.MINUTE, lessonLength);

        return new CalendarInterval(startTime, endTime);
    }

    public static CalendarInterval fromLesson(Lesson lesson) {
        CalendarInterval firstLessonTime = fromLessonNumber(lesson.getFirstLessonNumber());
        CalendarInterval lastLessonTime = fromLessonNumber(lesson.getLastLessonNumber());

        Calendar startTime = firstLessonTime.getStartTime();
        Calendar endTime = lastLessonTime.getEndTime();

        startTime.set(Calendar.DAY_OF_WEEK, lesson.getDay());
        endTime.set(Calendar.DAY_OF_WEEK, lesson.getDay());

        return new CalendarInterval(startTime, endTime);
    }

    public static CalendarInterval fromRepresentationFromTo(Representation.FromTo fromTo) {
        CalendarInterval firstLessonTime = fromLessonNumber(fromTo.getFirstLessonNumber());
        CalendarInterval lastLessonTime = fromLessonNumber(fromTo.getLastLessonNumber());

        Calendar startTime = firstLessonTime.getStartTime();
        Calendar endTime = lastLessonTime.getEndTime();

        return new CalendarInterval(startTime, endTime);
    }

}