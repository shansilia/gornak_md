package by.md.gornak.homework;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;


public class LauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetrica.activate(getApplicationContext(), getString(R.string.yandex_api_key));
        YandexMetrica.enableActivityAutoTracking(this);
    }
}
