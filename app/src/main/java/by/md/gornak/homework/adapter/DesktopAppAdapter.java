package by.md.gornak.homework.adapter;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.holder.AppViewHolder;
import by.md.gornak.homework.adapter.holder.DesktopAppViewHolder;
import by.md.gornak.homework.model.ApplicationDB;

public class DesktopAppAdapter extends RecyclerView.Adapter<DesktopAppViewHolder> {

    private List<ApplicationDB> infoList;
    private Context mContext;
    private PackageManager packageManager;
    private AppViewHolder.OnAppClickListener listener;
    private OnDragListener mDragStartListener;

    public DesktopAppAdapter(Context context, List<ApplicationDB> info,
                             AppViewHolder.OnAppClickListener listener,
                             OnDragListener dragListner) {
        this.infoList = info;
        mContext = context;
        packageManager = mContext.getPackageManager();
        this.listener = listener;
        mDragStartListener = dragListner;
    }

    @Override
    public DesktopAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.item_desktop_app;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        int height = parent.getMeasuredHeight() / 4;
        v.setMinimumHeight(height);
        return new DesktopAppViewHolder(v, listener, mContext);
    }

    @Override
    public void onBindViewHolder(final DesktopAppViewHolder holder, int position) {
        if (infoList.get(position) == null) {
            return;
        }
        ApplicationDB info = infoList.get(position);
        holder.setData(info, packageManager);
        holder.setTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDragStartListener.onStartDrag(holder);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < infoList.size() && toPosition < infoList.size()) {

            notifyItemMoved(fromPosition, toPosition);
            List<ApplicationDB> changes = new ArrayList<>();

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(infoList, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(infoList, i, i-1 );
                    }
                }

            YandexMetrica.reportEvent(mContext.getString(R.string.yandex_desktop_move));
        }
        return true;
    }

    public void update(List<ApplicationDB> info) {
        infoList.clear();
        infoList.addAll(info);
        notifyDataSetChanged();
    }


    public interface OnDragListener {

        void onStartDrag(RecyclerView.ViewHolder viewHolder);

        void changePosition(List<ApplicationDB> changes);

    }
}
