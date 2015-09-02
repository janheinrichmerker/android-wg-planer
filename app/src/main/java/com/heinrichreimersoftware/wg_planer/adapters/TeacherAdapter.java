package com.heinrichreimersoftware.wg_planer.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.structure.Teacher;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TeacherAdapter extends ListAdapter<Teacher, TeacherAdapter.ViewHolder> {

    private Context context;

    public TeacherAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_teacher, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Teacher teacher = get(position);

        holder.shorthand.setText(teacher.getShorthand());
        holder.fullName.setText(teacher.getFullName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Patterns.WEB_URL.matcher(teacher.getWebLink()).matches()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(teacher.getWebLink()));
                    context.startActivity(intent);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout)
        LinearLayout layout;
        @Bind(R.id.shorthand)
        TextView shorthand;
        @Bind(R.id.fullName)
        TextView fullName;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
