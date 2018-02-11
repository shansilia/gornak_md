package by.md.gornak.homework.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.metrica.YandexMetrica;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.holder.AppViewHolder;
import by.md.gornak.homework.db.DBService;
import by.md.gornak.homework.model.ApplicationDB;
import by.md.gornak.homework.util.Sorting;


public abstract class AppFragment extends Fragment {

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

            YandexMetrica.reportEvent(getString(R.string.yandex_open_app));
            incFrequency(activity.applicationInfo.packageName);
        }

        @Override
        public void onLongClick(String packageName) {
            showDialog(packageName);
        }
    };

    protected BroadcastReceiver mMonitor = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            String data = intent.getDataString();
            if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                removeApp(data.replace("package:", ""));
            } else if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                addApp(data.replace("package:", ""));
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        getActivity().registerReceiver(mMonitor, intentFilter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbService = new DBService(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(mMonitor);
        dbService.saveAll(apps.values());
    }

    protected List<ResolveInfo> getAppList() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return getContext().getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    protected void incFrequency(String packageName) {
        if (apps.containsKey(packageName)) {
            ApplicationDB app = apps.get(packageName);
            app.setFrequency(app.getFrequency() + 1);
        } else {
            apps.put(packageName, new ApplicationDB(packageName, false, 1, false, 0));
        }
    }

    protected abstract void addApp(String packageName);

    protected abstract int removeApp(String packageName);

    protected abstract void showDialog(final String packageName);

}
