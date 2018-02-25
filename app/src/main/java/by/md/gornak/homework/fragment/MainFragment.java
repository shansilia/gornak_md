package by.md.gornak.homework.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.DesktopPageAdapter;
import by.md.gornak.homework.db.DBService;
import by.md.gornak.homework.model.ApplicationDB;

import static by.md.gornak.homework.model.ApplicationDB.TYPE.APP;

public class MainFragment extends Fragment {

    private static final int DESKTOP_SIZE = 16;

    private ViewPager vpDesktop;
    protected DesktopPageAdapter mAdapter;
    private int curPage = 1;

    protected DBService dbService;
    public Map<String, ApplicationDB> apps;
    protected List<ApplicationDB> appsDesktop;


    protected BroadcastReceiver mMonitor = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                String data = intent.getDataString();
                removeApp(data.replace("package:", ""));
                YandexMetrica.reportEvent(getString(R.string.yandex_uninstall_app));
            } else if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                addApp(intent);
                YandexMetrica.reportEvent(getString(R.string.yandex_install_app));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(rootView);
        initAction();

        dbService = new DBService(getContext());
        apps = new HashMap<>();
        appsDesktop = new ArrayList<>(DESKTOP_SIZE);
        AppLoader loader = new AppLoader();
        loader.execute();

        return rootView;
    }

    protected void initView(View rootView) {
        vpDesktop = rootView.findViewById(R.id.vpDesktop);
        mAdapter = new DesktopPageAdapter(getActivity().getSupportFragmentManager());
        vpDesktop.setAdapter(mAdapter);
        vpDesktop.setCurrentItem(curPage);
        vpDesktop.setOffscreenPageLimit(4);
    }

    protected void initAction() {
        vpDesktop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vpDesktop.getAdapter().notifyDataSetChanged();
                Map<String, Object> eventAttributes = new HashMap<>();
                eventAttributes.put(getString(R.string.yandex_change_main_page), position);
                YandexMetrica.reportEvent(getString(R.string.yandex_change_main_page));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

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
    public void onPause() {
        super.onPause();
        dbService.saveAll(apps.values());
        YandexMetrica.reportEvent(getContext().getString(R.string.yandex_save_state));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mMonitor);
    }

    protected void addApp(Intent intent) {
        Intent infoIntent = new Intent();
        infoIntent.setPackage(intent.getDataString().replace("package:", ""));
        infoIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo result = getContext().getPackageManager().resolveActivity(intent, 0);
        apps.put(result.activityInfo.applicationInfo.packageName, new ApplicationDB(result));
        for (int i = 0; i < appsDesktop.size(); i++) {
            ApplicationDB app = appsDesktop.get(i);
            if (app == null) {
                app = apps.get(result.activityInfo.applicationInfo.packageName);
                app.setDesktop(true);
                app.setPosition(i);
                appsDesktop.set(i, app);
                Toast.makeText(getContext(), R.string.desktop_done, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mAdapter.updateDate(apps, appsDesktop);
    }

    protected void removeApp(String packageName) {
        apps.remove(packageName);
        dbService.remove(packageName);
        int pos = 0;
        for (ApplicationDB app : appsDesktop) {
            if (app != null && app.getAppPackage().equals(packageName)) {
                pos = app.getPosition();
                appsDesktop.set(pos, null);
                break;
            }
            pos++;
        }
        mAdapter.updateDate(apps, appsDesktop);
    }

    public void openListFragment() {
        curPage = 0;
    }

    public void openGridFragment() {
        curPage = 2;
    }

    public void openDesktopFragment() {
        curPage = 1;
    }

    class AppLoader extends AsyncTask<Void, Void, Void> {
        public Map<String, ApplicationDB> buffer;

        @Override
        protected Void doInBackground(Void... voids) {
            buffer = dbService.readAll();
            for (int i = 0; i < DESKTOP_SIZE; i++) {
                appsDesktop.add(null);
            }
            fillAppInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.updateDate(apps, appsDesktop);
        }


        protected void fillAppInfo() {
            for (ApplicationDB app : apps.values()) {
                if (!app.getType().equals(APP.toString()) && app.isDesktop()) {
                    appsDesktop.set(app.getPosition(), app);
                }
            }
            List<ResolveInfo> infoList = getAppList();
            for (ResolveInfo info : infoList) {
                String packageName = info.activityInfo.applicationInfo.packageName;
                if (buffer.containsKey(packageName)) {
                    ApplicationDB app = buffer.get(packageName);
                    app.setInfo(info);
                    apps.put(packageName, app);
                    if (app.isDesktop()) {
                        appsDesktop.set(app.getPosition(), app);
                    }
                } else {
                    apps.put(packageName, new ApplicationDB(info));
                }
            }

            YandexMetrica.reportEvent(getString(R.string.yandex_init_data));
        }

        protected List<ResolveInfo> getAppList() {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            return getContext().getPackageManager().queryIntentActivities(mainIntent, 0);
        }
    }
}
