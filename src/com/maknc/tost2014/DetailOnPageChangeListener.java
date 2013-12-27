package com.maknc.tost2014;

import android.support.v4.view.ViewPager;

/**
 * Get the current view position from the ViewPager by
 * extending SimpleOnPageChangeListener class and adding your method
 */
public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

    private int currentPage;

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
        
    }

    public final int getCurrentPage() {
        return currentPage;
    }

}
