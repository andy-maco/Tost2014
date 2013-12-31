package com.maknc.tost2014.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maknc.tost2014.AboutActivity;
import com.maknc.tost2014.Config;
import com.maknc.tost2014.DetailActivity;
import com.maknc.tost2014.MainActivity;
import com.maknc.tost2014.R;

public class DetailPagerAdapter extends FragmentStatePagerAdapter {
	
	private List<HashMap<String, String>> values;

	/* Add my List parameters to constructor */
	public DetailPagerAdapter(FragmentManager fragmentManager,
			List<HashMap<String, String>> values) {
		super(fragmentManager);
		this.values = values;
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Fragment getItem(int position) {
		
		HashMap<String, String> currentValue = values.get(position);
		String text = currentValue.get(Config.KEY_TEXT);
		String currentId = currentValue.get(Config.KEY_FAV);
		
		boolean isFav = false;
		
		String isFavorite = currentValue.get(Config.KEY_FAV);
		if (isFavorite.equals("true")) {
			isFav = true;
		}
		
		return DetailFragment.newInstance(position, text, isFav);
	}

	public static class DetailFragment extends Fragment {
		
		int mNum;
		String mDetailText;
		boolean mIsFavorite;
		
		private static SharedPreferences sharedPref;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		public static DetailFragment newInstance(int num, String val, boolean fv) {
			DetailFragment f = new DetailFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			
			args.putInt("num", num);			
			args.putString("detail_text", val);
			args.putBoolean("detail_fav", fv);
			
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
			mDetailText = getArguments() != null ? getArguments().getString("detail_text") : "No data";
			mIsFavorite = getArguments() != null ? getArguments().getBoolean("detail_fav") : false;
			
			/* 
			 * Show action bar menu for fragment
			 * http://www.grokkingandroid.com/adding-action-items-from-within-fragments/
			 */
			//setHasOptionsMenu(true);
			
			/*try {
				favButton = mainMenuFragment.findItem(R.id.menu_favorites);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_pager_list, container,
					false);
			
			//getActivity().getActionBar().setIcon(R.drawable.ic_action_important);

			
			 View tvText = v.findViewById(R.id.detailFragmentText);
			 ((TextView)tvText).setText(mDetailText);
			 
			 View tvNum = v.findViewById(R.id.tvDetailId);
			 ((TextView)tvNum).setText((mNum + 1) + "");
			 

			/*View tv = v.findViewById(R.id.detailFragmentText);

			String curlyLeftDoubleQuote = Html.fromHtml("&ldquo;").toString();
			String curlyRightDoubleQuote = Html.fromHtml("&rdquo;").toString();

			((TextView) tv).setText(curlyLeftDoubleQuote + mPickedQuote
					+ curlyRightDoubleQuote);
					*/

			return v;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}
		
		
/*		@Override
		public void onStart() {
			super.onStart();	
			
			//TODO: Set fav ico here!
			 ((DetailActivity) getActivity()).updateFavIcon(mIsFavorite);
		}
		
		@Override
		public void onResume() {
			super.onResume();	
			
			//TODO: Set fav ico here!
			 ((DetailActivity) getActivity()).updateFavIcon(mIsFavorite);
		}*/
		
		@Override
		public void setUserVisibleHint(boolean isVisibleToUser) {
		    super.setUserVisibleHint(isVisibleToUser);
		    if (isVisibleToUser) {
		    	((DetailActivity) getActivity()).currentFragmentNum = (mNum + 1) + "";
		    	((DetailActivity) getActivity()).updateFavIcon(mIsFavorite);
		    }
		    else {  }
		}
		
		
		/*@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// Inflate the menu; this adds items to the action bar if it is present.
			inflater.inflate(R.menu.tost_menu_frag, menu);
			//menu.clear();
			//mainMenuFragment = menu;
		}*/
		
		/*@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				Intent mIntent = new Intent(getActivity(),MainActivity.class);
				mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
				return true;
			case R.id.menu_share:
				Intent mIntentAbout = new Intent(getActivity(),AboutActivity.class);
				startActivity(mIntentAbout);
				return true;
			case R.id.menu_favorites:
				 //TODO: Save or remove from favorites 

				return true;
			}
			return super.onOptionsItemSelected(item);
		}*/

	}
	
	
	
	/**
	 * Check if already in favorites
	 */
	private boolean checkIsFavorite(String fv, String currentId) {

		boolean checkResult = false;

		String[] fvArray = fv.split(",");
		for (int i = 0; i < fvArray.length; i++) {
			if (fvArray[i].equals(currentId)) {
				// set already in favorites
				checkResult = true;
			}
		}
		return checkResult;
	}

}
