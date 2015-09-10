package com.heinrichreimersoftware.wg_planer.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.wg_planer.databinding.ItemTeacherBinding;
import com.heinrichreimersoftware.wg_planer.structure.Teacher;

public class TeacherAdapter extends ListAdapter<Teacher, TeacherAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        ItemTeacherBinding binding = ItemTeacherBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setItem(get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemTeacherBinding binding;

        public ViewHolder(View rootView) {
            super(rootView);
            binding = DataBindingUtil.bind(rootView);
        }
    }
}
