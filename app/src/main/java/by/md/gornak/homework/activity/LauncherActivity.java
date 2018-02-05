package by.md.gornak.homework.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import by.md.gornak.homework.R;
import by.md.gornak.homework.fragment.AppGridFragment;
import by.md.gornak.homework.fragment.AppListFragment;
import by.md.gornak.homework.fragment.SettingsFragment;
import by.md.gornak.homework.util.Settings;

public class LauncherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Settings.getStringValue(this, R.string.pref_key_light_theme).equals("true") ?
                R.style.AppTheme : R.style.DarkAppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        openGrid();

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
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            AppListFragment fragment = new AppListFragment();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_grid) {
            openGrid();
        } else if (id == R.id.nav_manage) {
            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SettingsFragment fragment = new SettingsFragment();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openGrid() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AppGridFragment fragment = new AppGridFragment();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
