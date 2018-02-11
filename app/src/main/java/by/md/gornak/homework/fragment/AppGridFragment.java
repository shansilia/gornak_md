package by.md.gornak.homework.fragment;


import android.content.pm.ResolveInfo;
import android.support.v7.widget.GridLayoutManager;

import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.AppAdapter;
import by.md.gornak.homework.util.Settings;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class AppGridFragment extends AllAppFragment {

    private static final int LAND = 2;

    @Override
    protected void setupRecyclerView(List<ResolveInfo> pkgAppsList) {
        int numberOfColumns = Integer.parseInt(Settings.getStringValue(getContext(), R.string.pref_key_layout));
        if (getContext().getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            numberOfColumns += LAND;
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        mAdapter = new AppAdapter(getContext(), pkgAppsList, true, appListener);
        mRecyclerView.setAdapter(mAdapter);

    }
}
