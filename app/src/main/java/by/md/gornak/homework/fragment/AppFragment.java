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
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.holder.AppViewHolder;
import by.md.gornak.homework.db.DBService;
import by.md.gornak.homework.model.ApplicationDB;


public abstract class AppFragment extends Fragment {

    protected static DBService dbService;
    public static Map<String, ApplicationDB> apps;
    protected static List<ApplicationDB> appsDesktop;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbService = new DBService(getContext());
        if (apps == null) {
            apps = dbService.readAll();
            appsDesktop = new ArrayList<>(16);
            for (int i = 0; i < 16; i++) {
                appsDesktop.add(null);
            }
            fillAppInfo();
        }
    }

    public static void saveState() {
        dbService.saveAll(apps.values());
    }

    protected List<ResolveInfo> getAppList() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        return getContext().getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    protected void fillAppInfo() {
        List<ResolveInfo> infoList = getAppList();
        for (ResolveInfo info : infoList) {
            String packageName = info.activityInfo.applicationInfo.packageName;
            if (apps.containsKey(packageName)) {
                ApplicationDB app = apps.get(packageName);
                app.setInfo(info);
                if (app.isDesktop()) {
                    appsDesktop.set(app.getPosition(), app);
                }
            } else {
                apps.put(packageName, new ApplicationDB(info));
            }
        }
    }

    protected void incFrequency(String packageName) {
        if (apps.containsKey(packageName)) {
            ApplicationDB app = apps.get(packageName);
            app.setFrequency(app.getFrequency() + 1);
        } else {
            apps.put(packageName, new ApplicationDB(packageName, false, 1, false, 0));
        }
        update();
    }

    protected void showDialog(final String packageName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.app_dialog_title);

        String[] actions = getResources().getStringArray(R.array.app_dialog_action);
        if (apps.get(packageName).isDesktop()) {
            actions[0] = getString(R.string.delete_from_desktop);
        }

        actions[2] = actions[2] + " " + getFrequency(packageName);

        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (!apps.get(packageName).isDesktop()) {
                            addToDesktop(packageName);
                        } else {
                            removeFromDesktop(packageName);
                        }
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package",
                                packageName, null));
                        startActivity(intent);

                        YandexMetrica.reportEvent(getString(R.string.yandex_delete_app));
                        break;
                    case 3:
                        openInfoApp(packageName);
                        break;
                    default:
                        break;
                }

            }
        });

        builder.create().show();
    }

    protected int getFrequency(String packageName) {
        if (apps.containsKey(packageName)) {
            return apps.get(packageName).getFrequency();
        } else {
            return 0;
        }
    }

    protected void openInfoApp(String packageName) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + packageName));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    protected void addToDesktop(String packageName) {
        apps.get(packageName).setDesktop(true);
        for (int i = 0; i < appsDesktop.size(); i++) {
            ApplicationDB app = appsDesktop.get(i);
            if (app == null) {
                app = apps.get(packageName);
                app.setPosition(i);
                appsDesktop.set(i, app);
                Toast.makeText(getContext(), R.string.desktop_done, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getContext(), R.string.desktop_error, Toast.LENGTH_SHORT).show();
    }

    protected void removeFromDesktop(String packageName) {
        apps.get(packageName).setDesktop(false);
        for (int i = 0; i < appsDesktop.size(); i++) {
            ApplicationDB app = appsDesktop.get(i);
            if (app.getAppPackage().equals(packageName)) {
                app.setPosition(-1);
                appsDesktop.set(i, null);
                return;
            }
        }
    }

    public abstract void addApp(String packageName);

    public abstract int removeApp(String packageName);

    protected abstract void update();

}
