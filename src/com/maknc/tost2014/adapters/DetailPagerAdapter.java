package com.maknc.tost2014.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maknc.tost2014.Config;
import com.maknc.tost2014.R;

public class DetailPagerAdapter extends FragmentStatePagerAdapter {
	
	
	
    public DetailPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return Config.NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        return ArrayListFragment.newInstance(position);
    }
    
    
    public static class ArrayListFragment extends ListFragment {
        int mNum;
        
        private String mPickedQuote;
    	private String mPickedTostId;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        public static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
            /*View tv = v.findViewById(R.id.detailFragmentText);
            ((TextView)tv).setText("Fragment #" + mNum);*/
            
            Intent mIntentStarted = getActivity().getIntent();
            
            mPickedQuote = mIntentStarted.getStringExtra(Config.TAG_TOST_TEXT);
    		mPickedTostId = mIntentStarted.getStringExtra(Config.TAG_TOST_ID);

    		View tv = v.findViewById(R.id.detailFragmentText);
    		
    		String curlyLeftDoubleQuote = Html.fromHtml("&ldquo;").toString();
    		String curlyRightDoubleQuote = Html.fromHtml("&rdquo;").toString();
    		
    		((TextView)tv).setText(curlyLeftDoubleQuote + mPickedQuote
    				+ curlyRightDoubleQuote);
            
            
            return v;
        }

   }

    
    
}
