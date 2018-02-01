package by.md.gornak.homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class LayoutActivity extends AppCompatActivity {

    private static final int STANDARD = 4;
    private static final int TIGHT = 5;
    private static final int LAND = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);


        final TextView standardText = findViewById(R.id.tvStandardLayout);
        final TextView tightText = findViewById(R.id.tvTightLayout);
        standardText.setText(getString(R.string.small_layout_value, STANDARD, STANDARD+LAND));
        tightText.setText(getString(R.string.layout_value, TIGHT, TIGHT+LAND));

        final RadioButton standard = findViewById(R.id.rbStandardLayout);
        final RadioButton tight = findViewById(R.id.rbTightLayout);
        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tight.setChecked(false);
            }
        });

        tight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                standard.setChecked(false);
            }
        });

        final Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getBaseContext(), LauncherActivity.class);
                startActivity(intent);
            }
        });
    }
}
