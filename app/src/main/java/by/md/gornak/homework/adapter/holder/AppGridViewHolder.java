package by.md.gornak.homework.adapter.holder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import by.md.gornak.homework.R;

public class AppGridViewHolder extends RecyclerView.ViewHolder {

    private ImageView icon;
    private TextView name;

    public AppGridViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.appImage);
        name = itemView.findViewById(R.id.appName);
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getName() {
        return name;
    }
}
