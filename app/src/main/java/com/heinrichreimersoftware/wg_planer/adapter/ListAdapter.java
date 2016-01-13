package com.heinrichreimersoftware.wg_planer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * A {@code ListAdapter} is a {@code RecyclerView.Adapter} which can be used as a {@code List} and
 * notifies the {@code RecyclerView} of every changes to the contained data.
 */
public abstract class ListAdapter<E, V extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<V> implements List<E>, Serializable, RandomAccess {

    private static final long serialVersionUID = 4521218986828925183L;

    /**
     * The elements in this adapter.
     */
    private List<E> data = new ArrayList<>();

    /**
     * Constructs a new {@code ListAdapter} instance with zero initial capacity.
     */
    public ListAdapter() {
        data = new ArrayList<>();
    }

    /**
     * Constructs a new instance of {@code ListAdapter} with the specified
     * initial capacity.
     *
     * @param capacity the initial capacity of this {@code ArrayList}.
     * @throws IllegalArgumentException if capacity < 0.
     */
    public ListAdapter(int capacity) {
        data = new ArrayList<>(capacity);
    }

    /**
     * Constructs a new instance of {@code ListAdapter} containing the elements of
     * the specified collection.
     *
     * @param collection the collection of elements to add.
     */
    public ListAdapter(@NonNull Collection<? extends E> collection) {
        data = new ArrayList<>(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int location, E object) {
        data.add(location, object);
        notifyItemInserted(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(E object) {
        boolean modified = data.add(object);
        notifyItemInserted(data.size());
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(int location, @NonNull Collection<? extends E> collection) {
        boolean modified = data.addAll(location, collection);
        notifyItemRangeInserted(location, collection.size());
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(@NonNull Collection<? extends E> collection) {
        boolean modified = data.addAll(collection);
        notifyItemRangeInserted(data.size(), collection.size());
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        if (!data.isEmpty()) {
            int size = data.size();
            data.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object object) {
        return data.contains(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return data.containsAll(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ListAdapter)) return false;
        if (!object.getClass().equals(getClass())) return false;
        if (!data.equals(((ListAdapter) object).data)) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E get(int location) {
        return data.get(location);
    }

    /**
     * Returns all elements in this {@code List}.
     *
     * @return all elements in this {@code List}.
     */
    public List<E> getAll() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return data.hashCode() + 425;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object object) {
        return data.indexOf(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Iterator<E> iterator() {
        return data.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object object) {
        return data.lastIndexOf(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<E> listIterator() {
        return data.listIterator();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ListIterator<E> listIterator(int location) {
        return data.listIterator(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E remove(int location) {
        E object = data.remove(location);
        notifyItemRemoved(location);
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean remove(Object object) {
        int locationToRemove = data.indexOf(object);
        if (locationToRemove >= 0) {
            data.remove(locationToRemove);
            notifyItemRemoved(locationToRemove);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        boolean modified = false;
        for (Object object : collection) {
            int locationToRemove = data.indexOf(object);
            if (locationToRemove >= 0) {
                data.remove(locationToRemove);
                notifyItemRemoved(locationToRemove);
                modified = true;
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        boolean modified = false;
        for (int i = data.size() - 1; i >= 0; i--) {
            if (!collection.contains(data.get(i))) {
                data.remove(i);
                notifyItemRemoved(i);
                modified = true;
                i--;
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E set(int location, E object) {
        E oldObject = data.set(location, object);
        notifyItemChanged(location);
        return oldObject;
    }

    /**
     * Replaces all elements in this {@code ListAdapter} with the
     * specified new {@code List}. This operation can change the size of the {@code List}.
     *
     * @param list the list to insert.
     * @return the previous elements.
     * @throws UnsupportedOperationException if replacing elements in this {@code List} is not supported.
     * @throws ClassCastException            if the class of an object is inappropriate for this
     *                                       {@code List}.
     * @throws IllegalArgumentException      if an object cannot be added to this {@code List}.
     */
    public List<E> setAll(List<? extends E> list) {
        List<E> oldList = new ArrayList<>(data);
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
        return oldList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return data.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<E> subList(int start, int end) {
        return data.subList(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SuspiciousToArrayCall")
    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] array) {
        return data.toArray(array);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(data.size());
        for (int i = 0; i < data.size(); i++) {
            stream.writeObject(data.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int cap = stream.readInt();
        if (cap < data.size()) {
            throw new InvalidObjectException(
                    "Capacity: " + cap + " < data.size(): " + data.size());
        }

        data = new ArrayList<>(cap);
        for (int i = 0; i < data.size(); i++) {
            data.set(i, (E) stream.readObject());
        }
    }
}