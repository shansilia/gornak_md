package by.md.gornak.homework;

import android.app.Application;
import android.content.Intent;

import com.yandex.metrica.YandexMetrica;

import by.md.gornak.homework.service.ImageLoaderService;


public class LauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetrica.activate(getApplicationContext(), getString(R.string.yandex_api_key));
        YandexMetrica.enableActivityAutoTracking(this);

        Intent intent = new Intent(ImageLoaderService.ACTION_LOAD_IMAGE);
        ImageLoaderService.enqueueWork(getApplicationContext(), intent);
    }
}
