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

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        setAvatar();

        final Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getBaseContext(), ThemeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAvatar(){
        ImageView avatar = findViewById(R.id.avatar);
        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        src = Bitmap.createBitmap(
                src,
                (int)(src.getWidth()*0.15),
                (int)(src.getWidth()*0.15),
                (int)(src.getWidth()*0.7),
                (int)(src.getWidth()*0.7)
        );
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCircular(true);
        avatar.setImageDrawable(dr);
    }
}
