package com.heinrichreimersoftware.wg_planer.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.wg_planer.databinding.ItemClassBinding;
import com.heinrichreimersoftware.wg_planer.structure.SubjectFactory;

public class ClassesAdapter extends ListAdapter<String, ClassesAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        ItemClassBinding binding = ItemClassBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setItem(new SubjectFactory().fromShorthand(get(position)));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemClassBinding binding;

        public ViewHolder(View rootView) {
            super(rootView);
            binding = DataBindingUtil.bind(rootView);
        }
    }
}
