package com.heinrichreimersoftware.wg_planer.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.wg_planer.databinding.LessonItem;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;

public class TimetableAdapter extends ListAdapter<Lesson, TimetableAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LessonItem binding = LessonItem.inflate(LayoutInflater.from(container.getContext()), container, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setItem(get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LessonItem binding;

        public ViewHolder(View rootView) {
            super(rootView);
            binding = DataBindingUtil.bind(rootView);
        }
    }
}
