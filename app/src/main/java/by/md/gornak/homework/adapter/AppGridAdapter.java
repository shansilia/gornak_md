package by.md.gornak.homework.adapter;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.holder.AppGridViewHolder;

public class AppGridAdapter extends RecyclerView.Adapter<AppGridViewHolder> {

    private List<ResolveInfo> infoList;
    private Context mContext;
    private PackageManager packageManager;

    public AppGridAdapter(Context context, List<ResolveInfo> info) {
        this.infoList = info;
        mContext = context;
        packageManager = mContext.getPackageManager();
    }

    @Override
    public AppGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new AppGridViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppGridViewHolder holder, int position) {
        ResolveInfo info = infoList.get(position);
        holder.getIcon().setImageDrawable(info.loadIcon(packageManager));
        holder.getName().setText(info.loadLabel(packageManager));
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }
}
