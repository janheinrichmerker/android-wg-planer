package com.heinrichreimersoftware.wg_planer.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.structure.TeacherSubject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubjectAdapter extends ListAdapter<TeacherSubject, SubjectAdapter.ViewHolder> {

    private Context context;

    public SubjectAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subject, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TeacherSubject subject = get(position);

        holder.colorIndicator.setColorFilter(subject.getColor(), PorterDuff.Mode.SRC_IN);
        holder.fullName.setText(subject.getFullName() + " (" + subject.getTeacher() + ")");
        holder.room.setText(subject.getRoom());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.colorIndicator)
        ImageView colorIndicator;
        @Bind(R.id.fullName)
        TextView fullName;
        @Bind(R.id.room)
        TextView room;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
