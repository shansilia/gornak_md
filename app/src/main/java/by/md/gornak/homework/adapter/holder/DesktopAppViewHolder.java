package by.md.gornak.homework.adapter.holder;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

import by.md.gornak.homework.R;
import by.md.gornak.homework.model.ApplicationDB;

public class DesktopAppViewHolder extends AppViewHolder {

    private ImageView type;
    private Context mContext;

    public DesktopAppViewHolder(View itemView, OnAppClickListener listener, Context context) {
        super(itemView, listener);
        mContext = context;
        type = itemView.findViewById(R.id.typeImage);
    }

    public void setData(ApplicationDB info, PackageManager packageManager) {
        this.info = info;
        if(info.getType().equals(ApplicationDB.TYPE.APP.toString())) {
            type.setVisibility(View.GONE);
            icon.setImageDrawable(info.getInfo().loadIcon(packageManager));
            name.setText(info.getInfo().loadLabel(packageManager));
        } else {
            type.setVisibility(View.VISIBLE);
            byte[] outImage=info.getImage();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            if(theImage != null) {
                RoundedBitmapDrawable dr =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), theImage);
                dr.setCircular(true);
                icon.setImageDrawable(dr);
            } else if(info.getType().equals(ApplicationDB.TYPE.PHONE.toString())) {
                icon.setImageResource(R.drawable.ic_account_circle_black_48dp);
            }

            name.setText(info.getAppPackage());
        }
    }

    public void setTouchListener(View.OnTouchListener touchListener) {
        itemView.setOnTouchListener(touchListener);
    }
}
