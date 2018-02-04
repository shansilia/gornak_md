package by.md.gornak.homework.adapter.holder;


import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.AppAdapter;

public class AppViewHolder extends RecyclerView.ViewHolder {

    private ImageView icon;
    private TextView name;
    private ResolveInfo info;
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
                mListener.onLongClick(info);
                return true;
            }
        });
    }

    public void setData(ResolveInfo info, PackageManager packageManager) {
        this.info = info;

        icon.setImageDrawable(info.loadIcon(packageManager));
        name.setText(info.loadLabel(packageManager));
    }

    public interface OnAppClickListener {
        void onClick(ResolveInfo info);
        void onLongClick(ResolveInfo info);
    }
}
