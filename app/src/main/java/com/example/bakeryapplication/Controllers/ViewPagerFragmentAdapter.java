package com.example.bakeryapplication.Controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bakeryapplication.Views.MyTabs;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {
ArrayList<MyTabs> tabs = new ArrayList<>();

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }

    public void addTab(MyTabs tab){
        this.tabs.add(tab);

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
       return this.tabs.get(position).getTabFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabs.get(position).getTabName();
    }

    @Override
    public int getCount() {
        return this.tabs.size();
    }
}
