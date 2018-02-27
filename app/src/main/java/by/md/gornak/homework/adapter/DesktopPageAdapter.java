package by.md.gornak.homework.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.md.gornak.homework.fragment.AppFragment;
import by.md.gornak.homework.fragment.AppGridFragment;
import by.md.gornak.homework.fragment.AppListFragment;
import by.md.gornak.homework.fragment.DesktopFragment;
import by.md.gornak.homework.model.ApplicationDB;

public class DesktopPageAdapter extends FragmentStatePagerAdapter {

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

    public void updateDate(Map<String, ApplicationDB> apps, List<ApplicationDB> appsDesktop) {
        for (AppFragment fragment : pages) {
            fragment.setData(apps, appsDesktop);
        }
    }
}
