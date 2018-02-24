package by.md.gornak.homework.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import by.md.gornak.homework.util.BlurImage;

public class UpdateImageBroadcastReceiver extends BroadcastReceiver {

    private UpdateListener listener;

    public UpdateImageBroadcastReceiver(UpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if (ImageLoaderService.BROADCAST_ACTION_UPDATE_IMAGE.equals(action)) {
            final String imageName = intent.getStringExtra(ImageLoaderService.BROADCAST_PARAM_IMAGE);
            if (TextUtils.isEmpty(imageName) == false) {
                final Bitmap bitmap = ImageSaver.getInstance().loadImage(context.getApplicationContext(), imageName);
                final Drawable drawable = new BitmapDrawable(context.getResources(), BlurImage.blur(context, bitmap));
                listener.setDrawable(drawable);
            }
        }
    }

    public interface UpdateListener {
        void setDrawable(Drawable drawable);
    }
}