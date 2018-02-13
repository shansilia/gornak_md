package by.md.gornak.homework.adapter;


import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import by.md.gornak.homework.fragment.AppFragment;
import by.md.gornak.homework.fragment.AppGridFragment;
import by.md.gornak.homework.fragment.AppListFragment;
import by.md.gornak.homework.fragment.DesktopFragment;
import by.md.gornak.homework.model.ApplicationDB;

public class DesktopPageAdapter extends FragmentPagerAdapter {

    private List<AppFragment> pages;

    public DesktopPageAdapter(FragmentManager fm) {
        super(fm);
        createPage();
    }


    private void createPage() {
        pages = new ArrayList<>();
        pages.add(new AppListFragment());
        pages.add(new DesktopFragment());
        pages.add(new AppGridFragment());
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof DesktopFragment) {
            ((DesktopFragment) object).update();
        }
        return super.getItemPosition(object);
    }

    public void removeApp(String packageName) {
        AppFragment.apps.remove(packageName);
        for (AppFragment fr : pages) {
            fr.removeApp(packageName);
        }
    }

    public void addApp(ResolveInfo info) {
        AppFragment.apps.put(info.activityInfo.applicationInfo.packageName, new ApplicationDB(info));
        for (AppFragment fr : pages) {
            fr.addApp(info.activityInfo.applicationInfo.packageName);
        }
    }

    @Override
    public Parcelable saveState() {
        AppFragment.saveState();
        return super.saveState();
    }
}
