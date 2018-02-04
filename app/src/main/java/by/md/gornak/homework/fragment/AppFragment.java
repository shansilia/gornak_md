package by.md.gornak.homework.fragment;


import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.holder.AppViewHolder;
import by.md.gornak.homework.db.DBService;
import by.md.gornak.homework.model.ApplicationDB;

public abstract class AppFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected DBService dbService;
    protected Map<String, ApplicationDB> apps;

    protected AppViewHolder.OnAppClickListener appListener = new AppViewHolder.OnAppClickListener() {
        @Override
        public void onClick(ResolveInfo info) {
            ActivityInfo activity = info.activityInfo;
            ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                    activity.name);
            Intent i = new Intent(Intent.ACTION_MAIN);

            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);

            startActivity(i);

            incFrequency(activity.applicationInfo.packageName);
        }

        @Override
        public void onLongClick(String packageName) {
            showDialog(packageName);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbService = new DBService(getContext());
        apps = dbService.readAll();
    }

    @Override
    public void onPause() {
        super.onPause();
        dbService.saveAll(apps.values());
    }

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

    private void showDialog(final String packageName) {
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

    protected void removeApp(String packageName) {
        apps.remove(packageName);
        dbService.remove(packageName);
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

    protected void incFrequency(String packageName) {
        if (apps.containsKey(packageName)) {
            ApplicationDB app = apps.get(packageName);
            app.setFrequency(app.getFrequency() + 1);
        } else {
            apps.put(packageName, new ApplicationDB(packageName, false, 1));
        }
    }

    protected abstract void setupRecyclerView(List<ResolveInfo> pkgAppsList);
}