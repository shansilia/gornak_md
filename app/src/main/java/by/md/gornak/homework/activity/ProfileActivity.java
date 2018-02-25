package by.md.gornak.homework.activity;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yandex.metrica.YandexMetrica;

import by.md.gornak.homework.R;
import by.md.gornak.homework.util.Settings;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Settings.getStringValue(this, R.string.pref_key_light_theme).equals("true") ?
                R.style.AppTheme : R.style.DarkAppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();

        YandexMetrica.reportEvent(getString(R.string.yandex_open_profile));
    }

    private void initView() {
        LinearLayout llNews = findViewById(R.id.news);
        String news = Settings.getStringValue(this, R.string.pref_key_news);
        if(news == null) {
            llNews.setVisibility(View.GONE);
        } else {
            llNews.setVisibility(View.VISIBLE);
            TextView txtNews = findViewById(R.id.newsText);
            txtNews.setText(news);
        }

        setAvatar();
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setAvatar() {
        ImageView avatar = findViewById(R.id.avatar);
        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.drawable.avatar);
        src = Bitmap.createBitmap(
                src,
                0,
                src.getHeight() / 2 - src.getWidth() / 2,
                src.getWidth(),
                src.getWidth() / 2
        );
        avatar.setImageBitmap(src);
    }
}
