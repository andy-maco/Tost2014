package com.maknc.tost2014;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.maknc.tost2014.adapters.DetailPagerAdapter;
import com.maknc.tost2014.adapters.DetailPagerAdapter.DetailFragment;
import com.maknc.tost2014.parsers.JSONParser;

public class DetailActivity extends ActionBarActivity {
    static final int NUM_ITEMS = 10;

    DetailPagerAdapter mAdapter;
    ViewPager mDetailPager;
    DetailOnPageChangeListener mPageListener;
    
	private String mPickedTostId;
	public boolean isFavorite;
	
	/* Menu used for hardware button behavior onKeyUp */
	private Menu mainMenu;
	private MenuItem favButton;
	
	private AdView mAdView;
	
	private SharedPreferences sharedPref;
	List<HashMap<String, String>> tList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
        
        /* Read favorites list */
		sharedPref = getSharedPreferences(Config.PREFS, Context.MODE_PRIVATE);
		String defValue = "";
		String fav = sharedPref.getString(Config.PREFS_FAVORITES_KEY, defValue);
		
		//favButton = mainMenu.findItem(R.id.menu_favorites);
		
		tList = getTosts(R.raw.newtost2014, fav);
		

        mAdapter = new DetailPagerAdapter(getSupportFragmentManager(), tList);

        mDetailPager = (ViewPager)findViewById(R.id.detailPager);
        mDetailPager.setAdapter(mAdapter);
        
        mPageListener = new DetailOnPageChangeListener();
        
        Intent mIntentStarted = getIntent();
		mPickedTostId = mIntentStarted.getStringExtra(Config.TAG_TOST_ID);
		
		int pageId = Integer.parseInt(mPickedTostId) - 1;
		
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

  
    
	/**
	 * Method for read JSON and save into List of HashMaps
	 * 
	 * @param url
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getTosts(int ResID, String fv) {
		
		ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();
		
		boolean isFav = false;
	
		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();
	
		// getting JSON string from URL
		JSONObject jObject = jParser.getJSONFromRaw(getApplicationContext(), ResID);
	
		JSONArray jArray = null;
		try {
			jArray = jObject.getJSONArray(Config.JSON_TOSTS_ARRAY);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		//String[] Items = new String[jArray.length()];
	
		for (int i = 0; i < jArray.length(); i++) {
			try {
	
				JSONObject oneObject = jArray.getJSONObject(i);
				// Pulling items from the array
				//Items[i] = oneObject.getString(Config.JSON_ID);
				
				String currentId = oneObject.getString(Config.JSON_ID);
				String currentText = oneObject.getString(Config.JSON_TEXT);
				
				isFav = checkIsFavorite(fv, currentId);
	
				mItemList.add(putData(currentId,currentText,isFav + ""));
	
	
				// JSONArray subArray =
				// oneObject.getJSONArray(Config.JSON_QUOTES);
	
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	
		return mItemList;
	}

	/**
	 * Method save quote data into several HashMap
	 * 
	 * @param text
	 * @return
	 */
	private HashMap<String, String> putData(String id, String text, String fav) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(Config.KEY_ID, id);
		item.put(Config.KEY_TEXT, text);
		item.put(Config.KEY_FAV, fav);
		return item;
	}
	
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}


@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tost_menu, menu);
		mainMenu = menu;
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		 //Dynamically change favorites icon in action bar 

		 //Check if already in favorites 
		sharedPref = getSharedPreferences(Config.PREFS, Context.MODE_PRIVATE);
		String fav = sharedPref.getString(Config.PREFS_FAVORITES_KEY,
				Config.FAV_DEF_VALUE);
		isFavorite = checkIsFavorite(fav, mPickedTostId);

		MenuItem favoritesButton = menu.findItem(R.id.menu_favorites);
		if (isFavorite) {
			favoritesButton.setIcon(R.drawable.ic_action_important);
		} else {
			favoritesButton.setIcon(R.drawable.ic_action_not_important);
		}
		return super.onPrepareOptionsMenu(menu);

	}

	/*		@Override
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
			Intent mIntent = new Intent(getApplicationContext(),
					MainActivity.class);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mIntent);
			return true;
		case R.id.submenu_about:
			Intent mIntentAbout = new Intent(getApplicationContext(),
					AboutActivity.class);
			startActivity(mIntentAbout);
			return true;
		case R.id.menu_favorites:
			 Save or remove from favorites 
			
			
			sharedPref = getSharedPreferences(Config.PREFS,
					Context.MODE_PRIVATE);
			String fav = sharedPref.getString(Config.PREFS_FAVORITES_KEY,
					Config.FAV_DEF_VALUE);
			
			// TODO: Temporary
			fav = mPageListener.getCurrentPage() + "";

			 Check if already in favorites 
			isFavorite = checkIsFavorite(fav, mPickedTostId);

			 Write to favorites or remove 
			if (isFavorite) {
				fav = removeFromFavorite(fav, mPickedTostId);
				
				// Write to favorites
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(Config.PREFS_FAVORITES_KEY, fav);
				editor.commit();
				Toast.makeText(getApplicationContext(), "Удалено из избранного",
						Toast.LENGTH_SHORT).show();
				
				favButton.setIcon(R.drawable.ic_action_not_important);
			} else {
				
				// Add to favorites string
				if (fav.equals("")) {
					fav = mPickedTostId;
				} else {
					fav = fav + "," + mPickedTostId;
				}
				
				// Write to favorites
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(Config.PREFS_FAVORITES_KEY, fav);
				editor.commit();
				Toast.makeText(getApplicationContext(), "Добавлено в избранное" + fav,
						Toast.LENGTH_SHORT).show();

				// Change menu icon		
				favButton.setIcon(R.drawable.ic_action_important);
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	*//**
	 * Menu hardware button open action bar menu
	 * 
	 * http://stackoverflow.com/questions/12277262/
	 * opening-submenu-in-action-bar-on-hardware-menu-button-click
	 *//*
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:

				mainMenu.performIdentifierAction(R.id.menu_more, 0);
				// Log.d("Menu", "menu button pressed");
				return true;
			}

		}
		return super.onKeyUp(keyCode, event);
	}
*/
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
	
	/**
	 * Remove from favorites string
	 */
	private String removeFromFavorite(String fv, String currentId) {
		
		String clearResult = "";

		String[] fvArray = fv.split(",");
		for (int i = 0; i < fvArray.length; i++) {
			if (fvArray[i].equals(currentId)) {
				// do not add this element
			} else {
				// leave element in favorites
				if (clearResult.equals("")) {
					clearResult = fvArray[i];
				} else {
					clearResult = clearResult + "," + fvArray[i];
				}
			}
		}
		return clearResult;
	}
	
	@Override
	public void onAttachFragment (Fragment fragment) {
	    Log.i("TEST", "onAttachFragment: "+fragment);
	    if(fragment.getClass()==DetailFragment.class){
	        //fragList.add(new WeakReference<EventListFragment>((EventListFragment)fragment));
	    	//updateFavIcon();
	    	//TODO: Implement favorites icon set
	    }
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i("TEST", "onStart");
		//TODO: Remove
	}
	
	/**
	 * Set ActionBar Favorites icon
	 * @param isFav
	 */
	private void setFavIcon (boolean isFav) {
		// Change menu icon
		if (isFav) {
			favButton.setIcon(R.drawable.ic_action_important);
		} else {
			favButton.setIcon(R.drawable.ic_action_not_important);
		}
	}
	
	/**
	 * Update ActionBar Favorites icon
	 */
	public void updateFavIcon () {
		setFavIcon(isFavorite);
	}
}
