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

import by.md.gornak.homework.R;
import by.md.gornak.homework.activity.LauncherActivity;
import by.md.gornak.homework.activity.StartActivity;

public class LayoutFragment extends Fragment {


    private static final int STANDARD = 4;
    private static final int TIGHT = 5;
    private static final int LAND = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        final TextView standardText = rootView.findViewById(R.id.tvStandardLayout);
        final TextView tightText = rootView.findViewById(R.id.tvTightLayout);
        standardText.setText(getString(R.string.small_layout_value, STANDARD, STANDARD+LAND));
        tightText.setText(getString(R.string.layout_value, TIGHT, TIGHT+LAND));

        final RadioButton rbStandard = rootView.findViewById(R.id.rbStandardLayout);
        final RadioButton rbTight = rootView.findViewById(R.id.rbTightLayout);
        final View standard = rootView.findViewById(R.id.standard);
        final View tight = rootView.findViewById(R.id.tight);

        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbStandard.setChecked(true);
                rbTight.setChecked(false);
            }
        });

        tight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbStandard.setChecked(false);
                rbTight.setChecked(true);
            }
        });

        final Button next = rootView.findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LauncherActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
