package by.md.gornak.homework.fragment;


import android.content.pm.ResolveInfo;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import by.md.gornak.homework.adapter.AppAdapter;

public class AppListFragment extends AppFragment {

    @Override
    protected void setupRecyclerView(List<ResolveInfo> pkgAppsList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AppAdapter(getContext(), pkgAppsList, false, appListener);
        mRecyclerView.setAdapter(mAdapter);

    }
}
