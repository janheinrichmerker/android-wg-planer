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
import com.heinrichreimersoftware.wg_planer.structure.Subject;
import com.heinrichreimersoftware.wg_planer.structure.SubjectFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClassesAdapter extends ListAdapter<String, ClassesAdapter.ViewHolder> {

    private Context context;

    public ClassesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_class, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subject classInfo = new SubjectFactory().fromShorthand(get(position));

        holder.colorIndicator.setColorFilter(classInfo.getColor(), PorterDuff.Mode.SRC_IN);
        holder.fullName.setText(classInfo.getFullName());
        holder.shorthand.setText(classInfo.getShorthand());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.colorIndicator)
        ImageView colorIndicator;
        @Bind(R.id.fullName)
        TextView fullName;
        @Bind(R.id.shorthand)
        TextView shorthand;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
