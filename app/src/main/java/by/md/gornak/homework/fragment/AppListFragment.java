package by.md.gornak.homework.fragment;


import android.content.pm.ResolveInfo;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import by.md.gornak.homework.adapter.AppAdapter;

public class AppListFragment extends AppFragment {

    @Override
    protected void setupRecyclerView(List<ResolveInfo> pkgAppsList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new AppAdapter(getContext(), pkgAppsList, false, appListener));

    }
}
