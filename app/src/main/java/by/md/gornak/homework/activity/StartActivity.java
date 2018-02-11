package by.md.gornak.homework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.crashlytics.android.Crashlytics;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.HelloPageAdapter;
import by.md.gornak.homework.util.Settings;
import io.fabric.sdk.android.Fabric;

public class StartActivity extends AppCompatActivity {


    public static final String START_ITEM = "startItem";

    private ViewPager vpHello;
    private HelloPageAdapter mAdapter;
    private List<RadioButton> rbPage;
    private RadioGroup rgPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        YandexMetrica.reportEvent(getString(R.string.yandex_start_app));
        if (!Settings.getBooleanValue(this, R.string.pref_key_show_welcome, true)) {
            super.onCreate(savedInstanceState);
            final Intent intent = new Intent(this, LauncherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        if (Settings.getBooleanValue(this, R.string.pref_key_first_start, true)) {
            PreferenceManager.setDefaultValues(this, R.xml.pref, false);
            Settings.setBooleanValue(this, R.string.pref_key_first_start, false);
        }
        setTheme(Settings.getStringValue(this, R.string.pref_key_light_theme).equals("true") ?
                R.style.AppTheme : R.style.DarkAppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        applyPager();

        if(savedInstanceState != null) {
            vpHello.setCurrentItem(savedInstanceState.getInt(START_ITEM, 0));
        }else {
            vpHello.setCurrentItem(getIntent().getIntExtra(START_ITEM, 0));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(START_ITEM, vpHello.getCurrentItem());
    }

    private void applyPager() {

        vpHello = findViewById(R.id.vpHello);
        mAdapter = new HelloPageAdapter(getSupportFragmentManager());
        vpHello.setAdapter(mAdapter);
        vpHello.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int i = 0;
                for (RadioButton rb : rbPage) {
                    rb.setScaleX(i == position ? 1f : 0.5f);
                    rb.setScaleY(i == position ? 1f : 0.5f);
                    i++;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        initRadioButton();
    }

    private void initRadioButton() {
        rgPage = findViewById(R.id.rgPage);
        rbPage = new ArrayList<>();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            RadioButton rb = new RadioButton(this);
            rb.setButtonDrawable(R.drawable.ic_big_point);
            rb.setScaleX(i == 0 ? 1f : 0.5f);
            rb.setScaleY(i == 0 ? 1f : 0.5f);
            rgPage.addView(rb);
            rbPage.add(rb);
        }
    }
}
