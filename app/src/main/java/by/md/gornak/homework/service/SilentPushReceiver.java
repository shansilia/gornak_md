package by.md.gornak.homework.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yandex.metrica.push.YandexMetricaPush;

import by.md.gornak.homework.R;
import by.md.gornak.homework.util.Settings;


public class SilentPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Settings.setStringValue(context, R.string.pref_key_news,
                intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD));
    }
}
