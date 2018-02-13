package by.md.gornak.homework.adapter.holder;


import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

import by.md.gornak.homework.R;
import by.md.gornak.homework.model.ApplicationDB;

public class AppViewHolder extends RecyclerView.ViewHolder {

    private ImageView icon;
    private TextView name;
    private ApplicationDB info;
    private OnAppClickListener mListener;

    public AppViewHolder(View itemView, OnAppClickListener listener) {
        super(itemView);
        mListener = listener;
        icon = itemView.findViewById(R.id.appImage);
        name = itemView.findViewById(R.id.appName);
        setActions();
    }

    public void setActions() {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(info);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onLongClick(info == null ? null : info.getInfo().activityInfo.applicationInfo.packageName, getAdapterPosition());
                return true;
            }
        });
    }

    public void setData(ApplicationDB info, PackageManager packageManager) {
        this.info = info;
        if(info.getType().equals(ApplicationDB.TYPE.APP.toString())) {
            icon.setImageDrawable(info.getInfo().loadIcon(packageManager));
            name.setText(info.getInfo().loadLabel(packageManager));
        } else {
            byte[] outImage=info.getImage();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            icon.setImageBitmap(theImage);

            name.setText(info.getAppPackage());
        }
    }

    public void setTouchListener(View.OnTouchListener touchListener) {
        itemView.setOnTouchListener(touchListener);
    }

    public interface OnAppClickListener {
        void onClick(ApplicationDB info);

        void onLongClick(String packageName, int position);
    }
}
