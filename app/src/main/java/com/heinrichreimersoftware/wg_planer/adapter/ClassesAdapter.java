package com.heinrichreimersoftware.wg_planer.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.wg_planer.databinding.ClassItem;
import com.heinrichreimersoftware.wg_planer.utils.factories.SubjectFactory;

public class ClassesAdapter extends ListAdapter<String, ClassesAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        ClassItem binding = ClassItem.inflate(LayoutInflater.from(container.getContext()), container, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setItem(new SubjectFactory().fromShorthand(get(position)));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ClassItem binding;

        public ViewHolder(View rootView) {
            super(rootView);
            binding = DataBindingUtil.bind(rootView);
        }
    }
}
