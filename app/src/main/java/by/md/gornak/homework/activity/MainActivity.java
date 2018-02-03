package by.md.gornak.homework.activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import net.hockeyapp.android.CrashManager;

import by.md.gornak.homework.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView avatar = findViewById(R.id.avatar);
        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.drawable.avatar);
        src = Bitmap.createBitmap(
                src,
                0,
                src.getHeight()/2 - src.getWidth()/2,
                src.getWidth(),
                src.getWidth()
        );
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(dr.getMinimumHeight()*2);
        avatar.setImageDrawable(dr);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
    }

    private void checkForCrashes() {
        CrashManager.register(this);

    }
}
