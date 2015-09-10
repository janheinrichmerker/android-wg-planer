package com.heinrichreimersoftware.wg_planer.databinding;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.text.TextUtils;
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
    @BindingAdapter("app:representationColor")
    public static void indicatorColor(ImageView indicator, Representation representation) {
        indicator.setColorFilter(representation.getFormatter(indicator.getContext()).color(), PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter("app:representationTime")
    public static void time(TextView textView, Representation representation) {
        textView.setText(representation.getFormatter(textView.getContext()).time());
    }

    @BindingAdapter("app:representationInfo")
    public static void info(TextView textView, Representation representation) {
        String info = representation.getFormatter(textView.getContext()).info();
        if (TextUtils.isEmpty(info)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(info);
        }
    }

    @BindingAdapter("app:representationType")
    public static void representationType(TextView textView, Representation representation) {
        textView.setText(representation.getFormatter(textView.getContext()).type());
    }

    @BindingAdapter("app:representationSubject")
    public static void representationSubject(TextView textView, Representation representation) {
        textView.setText(representation.getFormatter(textView.getContext()).subject());
    }

    /* Lesson binding adapters */
    @BindingAdapter("app:lessonColor")
    public static void indicatorColor(ImageView indicator, Lesson lesson) {
        indicator.setColorFilter(lesson.getFormatter(indicator.getContext()).color(), PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter("app:lessonTime")
    public static void time(TextView textView, Lesson lesson) {
        textView.setText(lesson.getFormatter(textView.getContext()).time());
    }

    @BindingAdapter("app:lessonSubjects")
    public static void subjects(TextView textView, Lesson lesson) {
        textView.setText(lesson.getFormatter(textView.getContext()).subjects());
    }

    @BindingAdapter("app:lessonRooms")
    public static void rooms(TextView textView, Lesson lesson) {
        textView.setText(lesson.getFormatter(textView.getContext()).rooms());
    }

    @BindingAdapter("app:lessonTeachers")
    public static void teachers(TextView textView, Lesson lesson) {
        textView.setText(lesson.getFormatter(textView.getContext()).teachers());
    }

    @BindingAdapter("app:lessonDialog")
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
    @BindingAdapter("app:colorFilter")
    public static void indicatorColor(ImageView indicator, int color) {
        indicator.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter("app:webLink")
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
