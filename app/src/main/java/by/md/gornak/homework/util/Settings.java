package by.md.gornak.homework.util;


import android.content.Context;
import android.content.SharedPreferences;

import by.md.gornak.homework.R;

public class Settings {
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.shared_preferences), Context.MODE_PRIVATE);
    }

    public static String getStringValue(Context context, int idParamName) {
        return getSharedPreferences(context).getString(context.getString(idParamName), null);
    }

    public static void setStringValue(Context context, int idParamName, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(context.getString(idParamName), newValue);
        editor.apply();
    }

    public static boolean getBooleanValue(Context context, int idParamName, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(context.getString(idParamName), defaultValue);
    }

    public static void setBooleanValue(Context context, int idParamName, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(context.getString(idParamName), newValue);
        editor.apply();
    }
}
