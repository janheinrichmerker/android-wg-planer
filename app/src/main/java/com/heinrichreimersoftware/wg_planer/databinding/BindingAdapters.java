package com.heinrichreimersoftware.wg_planer.databinding;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.heinrichreimersoftware.wg_planer.dialog.LessonDialogBuilder;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.Representation;

public class BindingAdapters {

    /* Representation binding adapters */
    @BindingAdapter("representationColor")
    public static void indicatorColor(ImageView indicator, Representation representation) {
        indicator.setColorFilter(Representation.Formatter.color(representation), PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter("time")
    public static void time(TextView textView, Representation.FromTo fromTo) {
        textView.setText(Representation.Formatter.time(textView.getContext(), fromTo));
    }


    /* Lesson binding adapters */
    @BindingAdapter("lessonColor")
    public static void indicatorColor(ImageView indicator, Lesson lesson) {
        indicator.setColorFilter(lesson.getFormatter(indicator.getContext()).color(), PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter("lessonTime")
    public static void time(TextView textView, Lesson lesson) {
        textView.setText(lesson.getFormatter(textView.getContext()).time());
    }

    @BindingAdapter("lessonSubjects")
    public static void subjects(TextView textView, Lesson lesson) {
        textView.setText(lesson.getFormatter(textView.getContext()).subjects());
    }

    @BindingAdapter("lessonRooms")
    public static void rooms(TextView textView, Lesson lesson) {
        textView.setText(lesson.getFormatter(textView.getContext()).rooms());
    }

    @BindingAdapter("lessonTeachers")
    public static void teachers(TextView textView, Lesson lesson) {
        textView.setText(lesson.getFormatter(textView.getContext()).teachers());
    }

    @BindingAdapter("lessonDialog")
    public static void dialog(View view, Lesson lesson) {
        final MaterialDialog dialog = new LessonDialogBuilder(view.getContext(), lesson).build();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }


    /* General binding adapters */
    @BindingAdapter("colorFilter")
    public static void colorFilter(ImageView view, int color) {
        view.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    /* General binding adapters */
    @BindingAdapter("strikeThrough")
    public static void strikeThrough(TextView view, boolean strikeThrough) {
        view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @BindingAdapter("webLink")
    public static void indicatorColor(final View view, final String url) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Patterns.WEB_URL.matcher(url).matches()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    view.getContext().startActivity(intent);
                }
            }
        });
    }
}
