package by.md.gornak.homework.fragment;


import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import by.md.gornak.homework.adapter.AppAdapter;
import by.md.gornak.homework.model.ApplicationDB;

public class AppListFragment extends AllAppFragment {

    @Override
    protected void setupRecyclerView(List<ApplicationDB> pkgAppsList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AppAdapter(getContext(), pkgAppsList, false, appListener);
        mRecyclerView.setAdapter(mAdapter);

    }
}
