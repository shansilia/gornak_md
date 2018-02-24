package by.md.gornak.homework.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.util.Settings;

public class ImageLoaderService extends JobIntentService {

    public static final int JOB_ID_LOAD_IMAGE = 21234;

    public static final String ACTION_LOAD_IMAGE = "com.example.shad2018_practical6.simpleexample.LOAD_IMAGE";

    public static final String BROADCAST_ACTION_UPDATE_IMAGE =
            "com.example.shad2018_practical6.simpleexample.UPDATE_IMAGE";
    public static final String BROADCAST_PARAM_IMAGE = "com.example.shad2018_practical6.simpleexample.IMAGE";


    private final ImageLoader mImageLoader;
    private static Context mContext;

    public ImageLoaderService() {
        mImageLoader = new ImageLoader();
    }

    public static void enqueueWork(Context context, Intent work) {
        mContext = context;
        enqueueWork(context, ImageLoaderService.class, JOB_ID_LOAD_IMAGE, work);
    }

    @Override
    protected void onHandleWork(@NonNull final Intent intent) {
        String action = intent.getAction();

        if (ACTION_LOAD_IMAGE.equals(action)) {
            // if (TextUtils.isEmpty(imageUrl) == false) {
            int index = Settings.getInt(mContext, R.string.pref_key_image_index, -1);
            Settings.setInt(mContext, R.string.pref_key_image_index, index + 1);
            String imageName = "0.png";
            if (index == -1) {
                final List<String> imageUrls = mImageLoader.getImageUrls();

                int i = 0;

                for (String imageUrl : imageUrls) {
                    final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
                    ImageSaver.getInstance().saveImage(getApplicationContext(), bitmap, i + ".png");
                    if(i == 0) {
                        updateImage(imageName);
                    }
                    i++;
                }

            } else {
                imageName = index + ".png";
                updateImage(imageName);
            }

            // }
        }
    }

    protected void updateImage(String imageName) {

        final Intent broadcastIntent = new Intent(BROADCAST_ACTION_UPDATE_IMAGE);
        broadcastIntent.putExtra(BROADCAST_PARAM_IMAGE, imageName);
        sendBroadcast(broadcastIntent);
    }
}

