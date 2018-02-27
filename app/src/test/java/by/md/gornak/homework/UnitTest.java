package by.md.gornak.homework;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import by.md.gornak.homework.activity.LauncherActivity;
import by.md.gornak.homework.db.DBService;
import by.md.gornak.homework.fragment.MainFragment;
import by.md.gornak.homework.model.ApplicationDB;
import by.md.gornak.homework.util.ContactGenerator;
import by.md.gornak.homework.util.Settings;
import by.md.gornak.homework.util.Sorting;

import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
public class UnitTest {

    private Context context = Robolectric.buildActivity(LauncherActivity.class).get();

    private SharedPreferences sharedPreferences;

    @Before
    public void start() {
        sharedPreferences =
                context.getSharedPreferences(context.getString(R.string.shared_preferences), Context.MODE_PRIVATE);
    }

    @Test
    public void saveStringPref() {
        String test = "true";
        Settings.setStringValue(context, R.string.pref_key_layout, test);
        assertEquals(test, sharedPreferences.getString(context.getString(R.string.pref_key_layout), null));
    }

    @Test
    public void getStringPref() {
        String test = "true";
        sharedPreferences.edit().putString(context.getString(R.string.pref_key_layout), test).commit();
        String buffer = Settings.getStringValue(context, R.string.pref_key_layout);
        assertEquals(buffer, test);
    }

    @Test
    public void saveBooleanPref() {
        Settings.setBooleanValue(context, R.string.pref_key_show_welcome, true);
        assertEquals(true, sharedPreferences.getBoolean(context.getString(R.string.pref_key_show_welcome), false));
    }

    @Test
    public void getBooleanPref() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.pref_key_show_welcome), false).commit();
        boolean buffer = Settings.getBooleanValue(context, R.string.pref_key_show_welcome, true);
        assertEquals(buffer, false);
    }

    @Test
    public void sortingAZ() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        if(list.size() < 2) {
            return;
        }
        List<ApplicationDB> appAZ = new ArrayList<>();
        List<ApplicationDB> appZA = new ArrayList<>();
        for (ResolveInfo info : list) {
            ApplicationDB app = new ApplicationDB(info);
            appAZ.add(app);
            appZA.add(app);
        }
        sharedPreferences.edit().putString(context.getString(R.string.pref_key_sorting), Sorting.AZ).commit();
        Collections.sort(appAZ, Sorting.getComparable(context));
        sharedPreferences.edit().putString(context.getString(R.string.pref_key_sorting), Sorting.ZA).commit();
        Collections.sort(appZA, Sorting.getComparable(context));
        assertFalse(appAZ.get(0).equals(appZA.get(0)));
        assertEquals(appAZ.get(0), appZA.get(appZA.size()-1));
    }

    @Test
    public void sortingNone() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        if(list.size() < 2) {
            return;
        }
        List<ApplicationDB> appTest = new ArrayList<>();
        List<ApplicationDB> appCompare = new ArrayList<>();
        for (ResolveInfo info : list) {
            ApplicationDB app = new ApplicationDB(info);
            appTest.add(app);
            appCompare.add(app);
        }
        sharedPreferences.edit().putString(context.getString(R.string.pref_key_sorting), Sorting.NONE).commit();
        Collections.sort(appTest, Sorting.getComparable(context));
        assertEquals(appTest.get(0), appCompare.get(0));
        assertEquals(appTest.get(appTest.size()-1), appCompare.get(appCompare.size()-1));
    }

    @Test
    public void sortingStart() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);

        if(list.size() < 2) {
            return;
        }

        List<ApplicationDB> appTest = new ArrayList<>();
        int fr = list.size();
        for (ResolveInfo info : list) {
            ApplicationDB app = new ApplicationDB(info);
            app.setFrequency(fr);
            appTest.add(app);
            fr--;
        }
        sharedPreferences.edit().putString(context.getString(R.string.pref_key_sorting), Sorting.START).commit();
        Collections.sort(appTest, Sorting.getComparable(context));
        assertTrue(appTest.get(appTest.size()-1).getFrequency() < appTest.get(0).getFrequency());
    }

    @Test
    public void dbSave() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);

        List<ApplicationDB> appTest = new ArrayList<>();
        for (ResolveInfo info : list) {
            ApplicationDB app = new ApplicationDB(info);
            appTest.add(app);
        }

        DBService service = new DBService(context);
        service.saveAll(appTest);
        Map<String, ApplicationDB> appMap = service.readAll();
        assertEquals(appTest.size(), appMap.size());
    }

    @Test
    public void dbRemove() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);

        List<ApplicationDB> appTest = new ArrayList<>();
        for (ResolveInfo info : list) {
            ApplicationDB app = new ApplicationDB(info);
            appTest.add(app);
        }

        DBService service = new DBService(context);
        service.saveAll(appTest);
        service.remove(appTest.get(0).getAppPackage());
        Map<String, ApplicationDB> appMap = service.readAll();
        assertFalse(appMap.containsKey(appTest.get(0).getAppPackage()));
    }

    @Test
    public void dbChangeApp() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);

        List<ApplicationDB> appTest = new ArrayList<>();
        for (ResolveInfo info : list) {
            ApplicationDB app = new ApplicationDB(info);
            appTest.add(app);
        }

        DBService service = new DBService(context);
        service.saveAll(appTest);
        int test = 10;
        appTest.get(0).setFrequency(test);
        service.saveAll(appTest);
        Map<String, ApplicationDB> appMap = service.readAll();
        assertEquals(appMap.get(appTest.get(0).getAppPackage()).getFrequency(), test);
    }
}