package by.md.gornak.homework.fragment;


import android.content.pm.ResolveInfo;
import android.support.v7.widget.GridLayoutManager;

import java.util.List;

import by.md.gornak.homework.adapter.AppAdapter;

public class AppGridFragment extends AppFragment {
    @Override
    protected void setupRecyclerView(List<ResolveInfo> pkgAppsList) {
        int numberOfColumns = 4;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        mAdapter = new AppAdapter(getContext(), pkgAppsList, true, appListener);
        mRecyclerView.setAdapter(mAdapter);

    }
}
