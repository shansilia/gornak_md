package by.md.gornak.homework.adapter.holder;


import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import by.md.gornak.homework.R;
import by.md.gornak.homework.model.ApplicationDB;

public class AppViewHolder extends RecyclerView.ViewHolder {

    protected ImageView icon;
    protected TextView name;
    protected ApplicationDB info;
    protected OnAppClickListener mListener;

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
                if (info != null) {
                    mListener.onClick(info);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onLongClick(info == null ? null : info.getAppPackage(), getAdapterPosition());
                return true;
            }
        });
    }

    public void setData(ApplicationDB info, PackageManager packageManager) {
        this.info = info;
        icon.setImageDrawable(info.getInfo().loadIcon(packageManager));
        name.setText(info.getInfo().loadLabel(packageManager));
    }

    public interface OnAppClickListener {
        void onClick(ApplicationDB info);

        void onLongClick(String packageName, int position);
    }
}
