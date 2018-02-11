package by.md.gornak.homework.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.AppAdapter;
import by.md.gornak.homework.adapter.DesktopAppAdapter;
import by.md.gornak.homework.model.ApplicationDB;
import by.md.gornak.homework.util.Sorting;

public class DesktopFragment extends AppFragment {

    protected DesktopAppAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    private List<ApplicationDB> items;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        apps = dbService.readDesktop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_desktop, container, false);
        mRecyclerView = rootView.findViewById(R.id.appList);
        setupRecyclerView();
        return rootView;
    }

    @Override
    protected void showDialog(final String packageName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.app_dialog_title);

        String[] actions = getResources().getStringArray(R.array.app_dialog_action);
        actions[1] = actions[1] + " " + getFrequency(packageName);

        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package",
                                packageName, null));
                        startActivity(intent);

                        YandexMetrica.reportEvent(getString(R.string.yandex_delete_app));
                        break;
                    case 2:
                        openInfoApp(packageName);
                        break;
                    default:
                        break;
                }

            }
        });

        builder.create().show();
    }

    @Override
    protected void addApp(String packageName) {
//        Intent intent = new Intent();
//        intent.setPackage(packageName);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        ResolveInfo result = getContext().getPackageManager().resolveActivity(intent, 0);
//        infoList.add(result);
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected int removeApp(String packageName) {
        int pos = 0;
//        for (ResolveInfo info : infoList) {
//            if (info.activityInfo.applicationInfo.packageName.equals(packageName)) {
//                mAdapter.remove(info);
//                break;
//            }
//            pos++;
//        }
//        apps.remove(packageName);
//        dbService.remove(packageName);
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
        fillData();

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mAdapter = new DesktopAppAdapter(getContext(), items, appListener);
        mRecyclerView.setAdapter(mAdapter);

    }

    protected void fillData() {

        items = new ArrayList<>(16);
        List<ResolveInfo> infoList = getAppList();

        for(ResolveInfo info : infoList) {
            if(apps.containsKey(info.activityInfo.applicationInfo.packageName)) {
                ApplicationDB app = apps.get(info.activityInfo.applicationInfo.packageName);
                items.set(app.getPosition(), app);
            }
        }
    }
}
