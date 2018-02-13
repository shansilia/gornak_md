package by.md.gornak.homework.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.DesktopAppAdapter;
import by.md.gornak.homework.adapter.TouchHelper;
import by.md.gornak.homework.model.ApplicationDB;

public class DesktopFragment extends AppFragment implements DesktopAppAdapter.OnDragListener {

    protected DesktopAppAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected ItemTouchHelper mItemTouchHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_desktop, container, false);
        mRecyclerView = rootView.findViewById(R.id.appList);
        setupRecyclerView();
        return rootView;
    }

    public void update() {
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void addApp(String packageName) {
        for (int i = 0; i < appsDesktop.size(); i++) {
            ApplicationDB app = appsDesktop.get(i);
            if (app == null) {
                app = apps.get(packageName);
                app.setDesktop(true);
                app.setPosition(i);
                appsDesktop.set(i, app);
                Toast.makeText(getContext(), R.string.desktop_done, Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Override
    public int removeApp(String packageName) {
        int pos = -1;
        for (ApplicationDB app : appsDesktop) {
            if (app != null && app.getAppPackage().equals(packageName)) {
                pos = app.getPosition();
                appsDesktop.set(pos, null);
                break;
            }
        }
        return pos;
    }

    protected void openInfoApp(String packageName) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + packageName));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    protected int getFrequency(String packageName) {
        if (apps.containsKey(packageName)) {
            return apps.get(packageName).getFrequency();
        } else {
            return 0;
        }
    }

    protected void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mAdapter = new DesktopAppAdapter(getContext(), appsDesktop, appListener, this);

        ItemTouchHelper.Callback callback =
                new TouchHelper(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void removeFromDesktop(String packageName) {
        super.removeFromDesktop(packageName);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void changePosition(List<ApplicationDB> changes) {
        for(ApplicationDB app : changes) {
            apps.put(app.getAppPackage(), app);
        }
    }
}
