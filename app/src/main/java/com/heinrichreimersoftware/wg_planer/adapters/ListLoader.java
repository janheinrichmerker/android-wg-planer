package com.heinrichreimersoftware.wg_planer.adapters;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class ListLoader<T, A extends ListAdapter<T, ? extends RecyclerView.ViewHolder>> {

    Task<T> loaderTask;
    private A adapter;
    private AsyncTask<Void, Void, Void> loadAsync;

    private List<T> list;

    public ListLoader(A adapter, final Task<T> loaderTask) {
        this.adapter = adapter;
        this.loaderTask = loaderTask;
    }

    public void load() {
        if (loadAsync == null || loadAsync.getStatus() != AsyncTask.Status.RUNNING) {
            start();
        }
    }

    public void forceLoad() {
        cancel();
        start();
    }

    public void show() {
        adapter.set(list);
    }

    public void cancel() {
        if (loadAsync != null && loadAsync.getStatus() == AsyncTask.Status.RUNNING) {
            loadAsync.cancel(true);
        }
    }

    private void start() {
        loadAsync = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                list = loaderTask.load();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                show();
            }
        };
        loadAsync.execute();
    }

    public interface Task<L> {
        List<L> load();
    }
}
