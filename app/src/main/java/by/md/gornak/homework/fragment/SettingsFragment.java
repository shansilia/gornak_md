package by.md.gornak.homework.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.md.gornak.homework.R;

public class SettingsFragment extends PreferenceFragment {

    private ListPreference favourite;
    private ListPreference favouriteTime;


    public SettingsFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        TypedValue a = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            view.setBackgroundColor(a.data);
        } else {
            view.setBackgroundDrawable(getActivity().getResources().getDrawable(a.resourceId));
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
        new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (!key.equals(getString(R.string.pref_key_light_theme))
                        && !key.equals(getString(R.string.pref_key_layout))) {
                    return;
                }

                getActivity().finish();
                final Intent intent = getActivity().getIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
            }
        };

//        favouriteTime  = (ListPreference) findPreference(getString(R.string.pref_key_favourite_time));
//        favourite  = (ListPreference) findPreference(getString(R.string.pref_key_favourite));
//        favourite.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                favouriteTime.setEnabled(Boolean.parseBoolean(favourite.getValue()));
//                return true;
//            }
//        });
    }

}
