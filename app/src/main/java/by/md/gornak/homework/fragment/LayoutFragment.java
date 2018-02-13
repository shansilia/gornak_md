package by.md.gornak.homework.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yandex.metrica.YandexMetrica;

import java.util.HashMap;
import java.util.Map;

import by.md.gornak.homework.R;
import by.md.gornak.homework.activity.LauncherActivity;
import by.md.gornak.homework.util.Settings;

public class LayoutFragment extends Fragment {

    private RadioButton rbStandard;
    private RadioButton rbTight;

    private static final int STANDARD = 4;
    private static final int TIGHT = 5;
    private static final int LAND = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        int defaultLayout = Integer.parseInt(Settings.getStringValue(getContext(), R.string.pref_key_layout));

        final TextView standardText = rootView.findViewById(R.id.tvStandardLayout);
        final TextView tightText = rootView.findViewById(R.id.tvTightLayout);
        standardText.setText(getString(R.string.small_layout_value, STANDARD, STANDARD + LAND));
        tightText.setText(getString(R.string.layout_value, TIGHT, TIGHT + LAND));

        rbStandard = rootView.findViewById(R.id.rbStandardLayout);
        rbTight = rootView.findViewById(R.id.rbTightLayout);

        rbStandard.setChecked(defaultLayout == STANDARD);
        rbTight.setChecked(defaultLayout != STANDARD);

        setAction(rootView);

        return rootView;
    }

    private void setAction(View rootView) {
        final View standard = rootView.findViewById(R.id.standard);
        final View tight = rootView.findViewById(R.id.tight);

        standard.setOnClickListener(new LayoutClickListener(STANDARD));
        tight.setOnClickListener(new LayoutClickListener(TIGHT));
        rbStandard.setOnClickListener(new LayoutClickListener(STANDARD));
        rbTight.setOnClickListener(new LayoutClickListener(TIGHT));

        final Button next = rootView.findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.setBooleanValue(getContext(), R.string.pref_key_show_welcome, false);
                Intent intent = new Intent(getActivity(), LauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    class LayoutClickListener implements View.OnClickListener {

        private int size;

        LayoutClickListener(int size) {
            this.size = size;
        }

        @Override
        public void onClick(View view) {
            rbStandard.setChecked(size == STANDARD);
            rbTight.setChecked(size != STANDARD);
            Settings.setStringValue(getContext(), R.string.pref_key_layout, String.valueOf(size));


            Map<String, Object> eventAttributes = new HashMap<>();
            eventAttributes.put(getString(R.string.pref_key_layout), size == STANDARD);
            YandexMetrica.reportEvent(getString(R.string.yandex_change_main_page));
        }
    }
}
