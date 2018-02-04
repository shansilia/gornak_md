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
import by.md.gornak.homework.adapter.holder.AppViewHolder;

public class AppAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private List<ResolveInfo> infoList;
    private Context mContext;
    private PackageManager packageManager;
    private AppViewHolder.OnAppClickListener listener;
    private boolean isGrid;

    public AppAdapter(Context context, List<ResolveInfo> info, boolean isGrid, AppViewHolder.OnAppClickListener listener) {
        this.infoList = info;
        mContext = context;
        packageManager = mContext.getPackageManager();
        this.isGrid = isGrid;
        this.listener = listener;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = isGrid ? R.layout.item_app : R.layout.item_line_app;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new AppViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        ResolveInfo info = infoList.get(position);
        holder.setData(info, packageManager);
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


}
