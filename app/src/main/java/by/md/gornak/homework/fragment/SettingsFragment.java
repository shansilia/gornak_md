package by.md.gornak.homework.fragment;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        favouriteTime  = (ListPreference) findPreference(getString(R.string.pref_key_favourite_time));
        favourite  = (ListPreference) findPreference(getString(R.string.pref_key_favourite));
        favourite.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                favouriteTime.setEnabled(Boolean.parseBoolean(favourite.getValue()));
                return true;
            }
        });
    }

}
