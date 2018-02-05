package by.md.gornak.homework.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import by.md.gornak.homework.fragment.HelloFragment;
import by.md.gornak.homework.fragment.LayoutFragment;
import by.md.gornak.homework.fragment.ThemeFragment;

public class HelloPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> pages;

    public HelloPageAdapter(FragmentManager fm) {
        super(fm);
        createPage();
    }


    private void createPage(){
        pages = new ArrayList<>();
        pages.add(new HelloFragment());
        pages.add(new ThemeFragment());
        pages.add(new LayoutFragment());
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
