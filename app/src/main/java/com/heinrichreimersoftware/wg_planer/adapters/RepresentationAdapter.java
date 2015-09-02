package com.heinrichreimersoftware.wg_planer.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.data.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.structure.Formatter;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.structure.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepresentationAdapter extends ListAdapter<Representation, RepresentationAdapter.ViewHolder> {

    private Context context;

    private boolean hasSetSchoolClass = false;

    public RepresentationAdapter(Context context) {
        this.context = context;


        User user = UserContentHelper.getUser(context);
        if (user != null && user.getSchoolClass() != null && !user.getSchoolClass().equals(""))
            hasSetSchoolClass = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_representation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Representation representation = get(position);

        holder.lessonNumber.setText(Formatter.formatLessonTime(context, representation));
        holder.colorIndicator.setColorFilter(Formatter.formatLessonColor(representation), PorterDuff.Mode.SRC_IN);
        holder.subject.setText(representation.getRepresentedSubjectText(hasSetSchoolClass));

        String info = Formatter.formatRepresentationInfo(context, representation);
        if (!TextUtils.isEmpty(info)) {
            holder.info.setText(info);
            holder.info.setVisibility(View.VISIBLE);
        } else {
            holder.info.setVisibility(View.VISIBLE);
        }

        holder.text.setVisibility(View.VISIBLE);

        holder.type.setText(Formatter.formatRepresentationType(representation));

        String text = Formatter.formatRepresentationText(representation);
        if (!TextUtils.isEmpty(text)) {
            holder.text.setText(text);
            holder.text.setVisibility(View.VISIBLE);
        } else {
            holder.text.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.colorIndicator)
        ImageView colorIndicator;
        @Bind(R.id.lessonNumber)
        TextView lessonNumber;
        @Bind(R.id.subject)
        TextView subject;
        @Bind(R.id.info)
        TextView info;
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.text)
        TextView text;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
