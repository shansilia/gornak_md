package by.md.gornak.homework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yandex.metrica.YandexMetrica;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.HelloPageAdapter;
import by.md.gornak.homework.util.Settings;
import by.md.gornak.homework.view.PageIndicatorView;

public class StartActivity extends AppCompatActivity {


    public static final String START_ITEM = "startItem";

    private ViewPager vpHello;
    private HelloPageAdapter mAdapter;
    private PageIndicatorView indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        YandexMetrica.reportEvent(getString(R.string.yandex_start_app));
        if (!Settings.getBooleanValue(this, R.string.pref_key_show_welcome, true)) {
            super.onCreate(savedInstanceState);
            final Intent intent = new Intent(this, LauncherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        YandexMetrica.reportEvent(getString(R.string.yandex_open_welcome));
        if (Settings.getBooleanValue(this, R.string.pref_key_first_start, true)) {
            PreferenceManager.setDefaultValues(this, R.xml.pref, false);
            Settings.setBooleanValue(this, R.string.pref_key_first_start, false);
        }
        setTheme(Settings.getStringValue(this, R.string.pref_key_light_theme).equals("true") ?
                R.style.AppTheme : R.style.DarkAppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        applyPager();

        if (savedInstanceState != null) {
            vpHello.setCurrentItem(savedInstanceState.getInt(START_ITEM, 0));
        } else {
            vpHello.setCurrentItem(getIntent().getIntExtra(START_ITEM, 0));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(START_ITEM, vpHello == null ? 0 : vpHello.getCurrentItem());
    }

    private void applyPager() {
        indicator = findViewById(R.id.rgPage);
        vpHello = findViewById(R.id.vpHello);
        mAdapter = new HelloPageAdapter(getSupportFragmentManager());
        vpHello.setAdapter(mAdapter);
        vpHello.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentPage(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        initRadioButton();
    }

    private void initRadioButton() {
        indicator.setPageCount(mAdapter.getCount());
        indicator.setCurrentPage(1);
    }
}
