package by.md.gornak.homework.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.AppAdapter;
import by.md.gornak.homework.model.ApplicationDB;
import by.md.gornak.homework.util.Sorting;

public abstract class AllAppFragment extends AppFragment {

    protected AppAdapter mAdapter;
    protected RecyclerView mRecyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app, container, false);
        mRecyclerView = rootView.findViewById(R.id.appList);
        List<ApplicationDB> data = new ArrayList<>(apps.values());
        Collections.sort(data, Sorting.getComparable(getContext()));
        setupRecyclerView(data);
        return rootView;
    }


    @Override
    public void addApp(String packageName) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int removeApp(String packageName) {
        int pos = 0;
        for (ApplicationDB info : apps.values()) {
            if (info.getAppPackage().equals(packageName)) {
                mAdapter.remove(info);
                break;
            }
            pos++;
        }
        apps.remove(packageName);
        dbService.remove(packageName);
        return pos;
    }

    public void update() {
        List<ApplicationDB> data = new ArrayList<>(apps.values());
        Collections.sort(data, Sorting.getComparable(getContext()));
        mAdapter.update(data);
    }

    protected abstract void setupRecyclerView(List<ApplicationDB> pkgAppsList);
}
