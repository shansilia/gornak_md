package by.md.gornak.homework;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_start);

        setAvatar();

        final Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getBaseContext(), DescriptionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAvatar(){
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
}
