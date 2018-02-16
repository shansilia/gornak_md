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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app, container, false);
        mRecyclerView = rootView.findViewById(R.id.appList);
        List<ApplicationDB> data = prepareData();
        Collections.sort(data, Sorting.getComparable(getContext()));
        setupRecyclerView(data);
        return rootView;
    }

    public void update() {
        List<ApplicationDB> data = prepareData();
        Collections.sort(data, Sorting.getComparable(getContext()));
        mAdapter.update(data);
    }

    private List<ApplicationDB> prepareData() {
        List<ApplicationDB> data = new ArrayList<>();
        for (ApplicationDB app : apps.values()) {
            if (app.getType().equals(ApplicationDB.TYPE.APP.toString())) {
                data.add(app);
            }
        }
        return data;
    }

    protected abstract void setupRecyclerView(List<ApplicationDB> pkgAppsList);
}
