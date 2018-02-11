package by.md.gornak.homework.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import by.md.gornak.homework.fragment.AppGridFragment;
import by.md.gornak.homework.fragment.AppListFragment;
import by.md.gornak.homework.fragment.DesktopFragment;

public class DesktopPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> pages;

    public DesktopPageAdapter(FragmentManager fm) {
        super(fm);
        createPage();
    }


    private void createPage(){
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

}
