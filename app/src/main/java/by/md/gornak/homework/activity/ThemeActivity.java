package by.md.gornak.homework.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import by.md.gornak.homework.R;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        final RadioButton light = findViewById(R.id.lightThemeRb);
        final RadioButton dark = findViewById(R.id.darkThemeRb);

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


        final Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getBaseContext(), LayoutActivity.class);
                startActivity(intent);
            }
        });
    }
}
