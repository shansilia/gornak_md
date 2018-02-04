package by.md.gornak.homework.fragment;


import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import by.md.gornak.homework.R;

public abstract class AppFragment extends Fragment {

    protected RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app, container, false);
        mRecyclerView = rootView.findViewById(R.id.appList);
        List<ResolveInfo> pkgAppsList = getAppList();
        setupRecyclerView(pkgAppsList);
        return rootView;
    }

    protected List<ResolveInfo> getAppList() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return getContext().getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    protected abstract void setupRecyclerView(List<ResolveInfo> pkgAppsList);
}
