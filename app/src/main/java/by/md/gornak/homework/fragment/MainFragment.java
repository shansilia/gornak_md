package by.md.gornak.homework.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.md.gornak.homework.R;
import by.md.gornak.homework.adapter.DesktopPageAdapter;

public class MainFragment extends Fragment {

    private ViewPager vpDesktop;
    private DesktopPageAdapter mAdapter;
    private int curPage = 1;


    protected BroadcastReceiver mMonitor = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            String data = intent.getDataString();
            if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                mAdapter.removeApp(data.replace("package:", ""));
            } else if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                Intent infoIntent = new Intent();
                infoIntent.setPackage(data.replace("package:", ""));
                infoIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                ResolveInfo result = getContext().getPackageManager().resolveActivity(intent, 0);
                mAdapter.addApp(result);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        vpDesktop = rootView.findViewById(R.id.vpDesktop);
        mAdapter = new DesktopPageAdapter(getActivity().getSupportFragmentManager());
        vpDesktop.setAdapter(mAdapter);
        vpDesktop.setCurrentItem(curPage);
        vpDesktop.setOffscreenPageLimit(0);
        vpDesktop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vpDesktop.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        getActivity().registerReceiver(mMonitor, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.saveState();
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mMonitor);
    }

    public void openListFragment() {
        curPage = 0;
    }

    public void openGridFragment() {
        curPage = 2;
    }

    public void openDesktopFragment() {
        curPage = 1;
    }
}
