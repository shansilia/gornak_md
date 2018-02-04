package by.md.gornak.homework.fragment;


import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.AppGridAdapter;

public class AppGridFragment extends Fragment {


    protected RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_grid, container, false);

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = getContext().getPackageManager().queryIntentActivities( mainIntent, 0);

        mRecyclerView = rootView.findViewById(R.id.appList);
        int numberOfColumns = 4;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));


        mRecyclerView.setAdapter(new AppGridAdapter(getContext(), pkgAppsList));

        return rootView;
    }
}
