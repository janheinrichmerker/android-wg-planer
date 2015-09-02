package com.heinrichreimersoftware.wg_planer.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ListAdapter<E, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private List<E> data = new ArrayList<>();

    public void add(int location, E object) {
        data.add(location, object);
        notifyItemInserted(location);
    }

    public void add(E object) {
        data.add(object);
        notifyItemInserted(data.size());
    }

    public void addAll(int location, Collection<? extends E> collection) {
        data.addAll(location, collection);
        notifyItemRangeInserted(location, collection.size());
    }

    public void addAll(Collection<? extends E> collection) {
        data.addAll(collection);
        notifyItemRangeInserted(data.size(), collection.size());
    }

    public void clear() {
        if (!data.isEmpty()) {
            int size = data.size();
            data.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public boolean contains(E object) {
        return data.contains(object);
    }

    public boolean containsAll(Collection<? extends E> collection) {
        return data.containsAll(collection);
    }

    public E get(int location) {
        return data.get(location);
    }

    public List<E> getAll() {
        return data;
    }

    public int indexOf(E object) {
        return data.indexOf(object);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int lastIndexOf(E object) {
        return data.lastIndexOf(object);
    }

    public E remove(int location) {
        E removedObject = data.remove(location);
        notifyItemRemoved(location);
        return removedObject;
    }

    public boolean remove(E object) {
        int locationToRemove = data.indexOf(object);
        if (locationToRemove >= 0) {
            data.remove(locationToRemove);
            notifyItemRemoved(locationToRemove);
            return true;
        }
        return false;
    }

    public E set(int location, E object) {
        E oldObject = data.set(location, object);
        notifyItemChanged(location);
        return oldObject;
    }

    public void set(List<? extends E> list) {
        if (list.size() == data.size()) {
            data.clear();
            data.addAll(list);
            notifyItemRangeChanged(0, data.size());
        } else if (list.size() < data.size()) {
            int size = data.size();
            data.clear();
            data.addAll(list);
            notifyItemRangeChanged(0, list.size());
            notifyItemRangeRemoved(list.size() - 1, size - list.size());
        } else {
            int size = data.size();
            data.clear();
            data.addAll(list);
            notifyItemRangeChanged(0, size);
            notifyItemRangeInserted(size - 1, list.size() - size);
        }
    }

    public int size() {
        return data.size();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
