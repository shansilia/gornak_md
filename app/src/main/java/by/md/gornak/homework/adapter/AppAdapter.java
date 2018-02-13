package by.md.gornak.homework.adapter;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.holder.AppViewHolder;
import by.md.gornak.homework.model.ApplicationDB;

public class AppAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private List<ApplicationDB> infoList;
    private Context mContext;
    private PackageManager packageManager;
    private AppViewHolder.OnAppClickListener listener;
    private boolean isGrid;

    public AppAdapter(Context context, List<ApplicationDB> info, boolean isGrid, AppViewHolder.OnAppClickListener listener) {
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
        ApplicationDB info = infoList.get(position);
        holder.setData(info.getInfo(), packageManager);
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public void update(List<ApplicationDB> info) {
//        List<ApplicationDB> buf = new ArrayList<>();
//        buf.addAll(info);
        infoList.clear();
        infoList.addAll(info);
        notifyDataSetChanged();
    }


    public void remove(ApplicationDB info) {
        try {
            int pos = infoList.indexOf(info);
            infoList.remove(pos);
            notifyItemRemoved(pos);
        } catch (Exception e) {
            Log.i("SHAD", "remove app");
        }
    }
}
