package by.md.gornak.homework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import by.md.gornak.homework.R;


public class ThemeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_theme, container, false);
        final RadioButton light = rootView.findViewById(R.id.lightThemeRb);
        final RadioButton dark = rootView.findViewById(R.id.darkThemeRb);

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dark.setChecked(false);
            }
        });

        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                light.setChecked(false);
            }
        });
        return rootView;
    }
}
