package com.maknc.tost2014;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.maknc.tost2014.adapters.DetailPagerAdapter;
import com.maknc.tost2014.adapters.DetailPagerAdapter.ArrayListFragment;

public class DetailActivity extends ActionBarActivity {
    static final int NUM_ITEMS = 10;

    DetailPagerAdapter mAdapter;
    ViewPager mDetailPager;
    
	private String mPickedTostId;
	private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        mAdapter = new DetailPagerAdapter(getSupportFragmentManager());

        mDetailPager = (ViewPager)findViewById(R.id.detailPager);
        mDetailPager.setAdapter(mAdapter);
        
        
        Intent mIntentStarted = getIntent();
		mPickedTostId = mIntentStarted.getStringExtra(Config.TAG_TOST_ID);
		
		int pageId = Integer.parseInt(mPickedTostId);
		
		mDetailPager.setCurrentItem(pageId);
		
        /*
        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.goto_first);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });
        button = (Button)findViewById(R.id.goto_last);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(NUM_ITEMS-1);
            }
        });\
        */
    }

    public static class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }
    }

}
