package by.md.gornak.homework;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import by.md.gornak.homework.model.ApplicationDB;
import by.md.gornak.homework.util.Settings;
import by.md.gornak.homework.util.Sorting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    private final Context context = InstrumentationRegistry.getTargetContext();

    private String test;
    private SharedPreferences sharedPreferences;

    @Before
    public void start() {
        test = "test";
        sharedPreferences =
                context.getSharedPreferences(context.getString(R.string.shared_preferences), Context.MODE_PRIVATE);
    }

    @Test
    public void saveStringPref() {
        Settings.setStringValue(context, R.string.pref_key_layout, test);
        assertEquals(test, sharedPreferences.getString(context.getString(R.string.pref_key_layout), null));
    }

    @Test
    public void getStringPref() {
        String buffer = Settings.getStringValue(context, R.string.pref_key_layout);
        assertEquals(buffer, sharedPreferences.getString(context.getString(R.string.pref_key_layout), null));
    }

    @Test
    public void saveBooleanPref() {
        Settings.setBooleanValue(context, R.string.pref_key_show_welcome, true);
        assertEquals(true, sharedPreferences.getBoolean(context.getString(R.string.pref_key_show_welcome), false));
    }

    @Test
    public void getBooleanPref() {
        boolean buffer = Settings.getBooleanValue(context, R.string.pref_key_show_welcome, true);
        assertEquals(buffer, sharedPreferences.getBoolean(context.getString(R.string.pref_key_show_welcome), false));
    }

    @Test
    public void sorting() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(mainIntent, 0);

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
        assertNotEquals(appAZ.get(0), appZA.get(0));
        assertEquals(appAZ.get(0), appZA.get(appZA.size()-1));
    }
}
