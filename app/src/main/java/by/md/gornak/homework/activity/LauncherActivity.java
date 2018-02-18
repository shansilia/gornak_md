package by.md.gornak.homework.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.yandex.metrica.YandexMetrica;

import java.util.HashMap;
import java.util.Map;

import by.md.gornak.homework.R;
import by.md.gornak.homework.fragment.MainFragment;
import by.md.gornak.homework.fragment.SettingsFragment;
import by.md.gornak.homework.service.ImageLoaderService;
import by.md.gornak.homework.service.ImageSaver;
import by.md.gornak.homework.util.BlurImage;
import by.md.gornak.homework.util.Settings;

public class LauncherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String OPEN_SETTINGS = "openSettings";

    private Toolbar toolbar;
    private ConstraintLayout container;

    private UpdateImageBroadcastReceiver mUpdateImageBroadcastReceiver;

    private class UpdateImageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (ImageLoaderService.BROADCAST_ACTION_UPDATE_IMAGE.equals(action)) {
                final String imageName = intent.getStringExtra(ImageLoaderService.BROADCAST_PARAM_IMAGE);
                if (TextUtils.isEmpty(imageName) == false) {
                    final Bitmap bitmap = ImageSaver.getInstance().loadImage(getApplicationContext(), imageName);
                    final Drawable drawable = new BitmapDrawable(getResources(), BlurImage.blur(context, bitmap));
                    setDrawable(drawable);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Settings.getStringValue(this, R.string.pref_key_light_theme).equals("true") ?
                R.style.AppTheme : R.style.DarkAppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        initView();

        mUpdateImageBroadcastReceiver = new UpdateImageBroadcastReceiver();
        Intent intent = new Intent(ImageLoaderService.ACTION_LOAD_IMAGE);
        ImageLoaderService.enqueueWork(getApplicationContext(), intent);

        if (getIntent().getBooleanExtra(OPEN_SETTINGS, false)) {
            openSettings();
        } else if (getSupportFragmentManager().getFragments().isEmpty()) {
            openDesktop();
        }

    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.container);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView avatar = navigationView.getHeaderView(0).findViewById(R.id.imageView);

        setAvatar(avatar);
        setAvatarAction(avatar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mUpdateImageBroadcastReceiver,
                new IntentFilter(ImageLoaderService.BROADCAST_ACTION_UPDATE_IMAGE));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mUpdateImageBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void setAvatar(ImageView avatar) {
        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.drawable.avatar);
        src = Bitmap.createBitmap(
                src,
                src.getWidth() / 5,
                src.getHeight() / 2 - src.getWidth() / 2,
                src.getWidth() / 2,
                src.getWidth() / 2
        );
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(dr.getMinimumHeight() * 2);
        avatar.setImageDrawable(dr);
    }

    private void setAvatarAction(ImageView avatar) {
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getFragments().size() <= 4) {
            showCloseDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.attention)
                .setMessage(R.string.exit_warning_text)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        Map<String, Object> eventAttributes = new HashMap<String, Object>();
                        eventAttributes.put(getString(R.string.yandex_answer), "ok");
                        YandexMetrica.reportEvent(getString(R.string.yandex_close_dialog));
                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Map<String, Object> eventAttributes = new HashMap<String, Object>();
                                eventAttributes.put(getString(R.string.yandex_answer), "cancel");
                                YandexMetrica.reportEvent(getString(R.string.yandex_close_dialog));
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        super.onBackPressed();
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_list:
                MainFragment list = new MainFragment();
                list.openListFragment();
                openFragment(list);
                break;
            case R.id.nav_grid:
                MainFragment fragment = new MainFragment();
                fragment.openGridFragment();
                openFragment(fragment);
                break;
            case R.id.nav_manage:
                openSettings();
                break;
            case R.id.nav_main:
                openDesktop();
//                Intent intent = new Intent(this, LauncherActivity.class);
//                startActivity(intent);
                break;
        }

        YandexMetrica.reportEvent(getString(R.string.yandex_navigation));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openDesktop() {
        MainFragment fragment = new MainFragment();
        fragment.openDesktopFragment();
        openFragment(fragment);
    }

    private void openSettings() {
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void setDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            container.setBackground(drawable);
        } else {
            container.setBackgroundDrawable(drawable);
        }

    }
}
