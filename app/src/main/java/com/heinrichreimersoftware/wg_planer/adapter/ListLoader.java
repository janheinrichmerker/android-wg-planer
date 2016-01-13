package com.heinrichreimersoftware.wg_planer.adapter;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class ListLoader<T, A extends ListAdapter<T, ? extends RecyclerView.ViewHolder>> {

    Task<T> loaderTask;
    private A adapter;
    private AsyncTask<Void, Void, List<T>> loadAsync;

    public ListLoader(A adapter, Task<T> loaderTask) {
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

    public void show(List<T> list) {
        adapter.setAll(list);
    }

    public void cancel() {
        if (loadAsync != null && loadAsync.getStatus() == AsyncTask.Status.RUNNING) {
            loadAsync.cancel(true);
        }
    }

    private void start() {
        loadAsync = new AsyncTask<Void, Void, List<T>>() {
            @Override
            protected List<T> doInBackground(Void... params) {
                return loaderTask.load();
            }

            @Override
            protected void onPostExecute(List<T> result) {
                show(result);
            }
        };
        loadAsync.execute();
    }

    public interface Task<L> {
        List<L> load();
    }
}
