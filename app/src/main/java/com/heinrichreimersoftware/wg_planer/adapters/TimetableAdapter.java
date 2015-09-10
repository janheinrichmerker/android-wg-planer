package com.heinrichreimersoftware.wg_planer.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.wg_planer.databinding.ItemLessonBinding;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;

public class TimetableAdapter extends ListAdapter<Lesson, TimetableAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        ItemLessonBinding binding = ItemLessonBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setItem(get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemLessonBinding binding;

        public ViewHolder(View rootView) {
            super(rootView);
            binding = DataBindingUtil.bind(rootView);
        }
    }
}
