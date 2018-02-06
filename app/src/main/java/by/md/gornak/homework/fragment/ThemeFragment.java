package by.md.gornak.homework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import by.md.gornak.homework.R;
import by.md.gornak.homework.activity.LauncherActivity;
import by.md.gornak.homework.activity.StartActivity;
import by.md.gornak.homework.util.Settings;


public class ThemeFragment extends Fragment {

    private RadioButton rbLight;
    private RadioButton rbDark;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_theme, container, false);

        boolean isLight = Boolean.parseBoolean(Settings.getStringValue(getContext(), R.string.pref_key_light_theme));
        rbLight = rootView.findViewById(R.id.lightThemeRb);
        rbDark = rootView.findViewById(R.id.darkThemeRb);
        rbLight.setChecked(isLight);
        rbDark.setChecked(!isLight);
        setAction(rootView);

        return rootView;
    }

    private void setAction(View rootView) {
        final View light = rootView.findViewById(R.id.light);
        final View dark = rootView.findViewById(R.id.dark);

        light.setOnClickListener(new ThemeClickListener(true));
        dark.setOnClickListener(new ThemeClickListener(false));
        rbLight.setOnClickListener(new ThemeClickListener(true));
        rbDark.setOnClickListener(new ThemeClickListener(false));
    }

    class ThemeClickListener implements View.OnClickListener {

        private boolean isLight;

        ThemeClickListener(boolean isLight) {
            this.isLight = isLight;
        }

        @Override
        public void onClick(View view) {
            rbLight.setChecked(isLight);
            rbDark.setChecked(!isLight);
            Settings.setStringValue(getContext(), R.string.pref_key_light_theme, String.valueOf(isLight));
            getActivity().finish();
            final Intent intent = getActivity().getIntent();
            intent.putExtra(StartActivity.START_ITEM, 1);
            getActivity().startActivity(intent);
        }
    }
}
