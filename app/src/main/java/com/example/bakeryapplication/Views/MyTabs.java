package com.example.bakeryapplication.Views;

import androidx.fragment.app.Fragment;

public class MyTabs {
    String tabName ;
    Fragment tabFragment ;

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Fragment getTabFragment() {
        return tabFragment;
    }

    public void setTabFragment(Fragment tabFragment) {
        this.tabFragment = tabFragment;
    }

    public MyTabs(String tabName, Fragment tabFragment) {
        this.tabName = tabName;
        this.tabFragment = tabFragment;
    }
}
