package com.heinrichreimersoftware.wg_planer.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.dialog.LessonDialogBuilder;
import com.heinrichreimersoftware.wg_planer.structure.Formatter;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimetableAdapter extends ListAdapter<Lesson, TimetableAdapter.ViewHolder> {

    private Context context;

    public TimetableAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lesson, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lesson lesson = get(position);

        holder.lessonNumber.setText(Formatter.formatLessonTime(context, lesson));
        holder.colorIndicator.setColorFilter(Formatter.formatLessonColor(lesson), PorterDuff.Mode.SRC_IN);

        holder.teachers.setText(Formatter.formatTeachers(context, lesson));
        holder.rooms.setText(Formatter.formatRooms(lesson));
        holder.subjects.setText(Formatter.formatSubjects(lesson));

        final MaterialDialog dialog = new LessonDialogBuilder(context, lesson).build();

        holder.layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.layout)
        LinearLayout layout;
        @Bind(R.id.colorIndicator)
        ImageView colorIndicator;
        @Bind(R.id.lessonNumber)
        TextView lessonNumber;
        @Bind(R.id.subjects)
        TextView subjects;
        @Bind(R.id.rooms)
        TextView rooms;
        @Bind(R.id.teachers)
        TextView teachers;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
