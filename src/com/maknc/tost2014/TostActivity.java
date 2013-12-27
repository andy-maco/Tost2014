package com.maknc.tost2014;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


/**
 * Display single quote selected from quotes list activity
 */
public class TostActivity extends ActionBarActivity {

	private static final String FAV_DEF_VALUE = "";

	private String mPickedQuote;
	private String mPickedTostId;
	private boolean isFavorite;

	private SharedPreferences sharedPref;

	/* Menu used for hardware button behavior onKeyUp */
	private Menu mainMenu;
	
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tost);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent mIntentStarted = getIntent();
		mPickedQuote = mIntentStarted.getStringExtra(Config.TAG_TOST_TEXT);

		mPickedTostId = mIntentStarted.getStringExtra(Config.TAG_TOST_ID);

		TextView mTextViewQouteText = (TextView) findViewById(R.id.tvQuoteText);
		String curlyLeftDoubleQuote = Html.fromHtml("&ldquo;").toString();
		String curlyRightDoubleQuote = Html.fromHtml("&rdquo;").toString();
		mTextViewQouteText.setText(curlyLeftDoubleQuote + mPickedQuote
				+ curlyRightDoubleQuote);

		/*
		 * TextView tv = (TextView) findViewById(R.id.tvAuthorQuoteName); String
		 * emDash = Html.fromHtml("&mdash;").toString(); tv.setText(emDash + " "
		 * + mPickedAuthor);
		 * 
		 * ImageView iv = (ImageView) findViewById(R.id.ivAuthorQuoteIcon); if
		 * (mPickedAuthorImage.equals("none")) {
		 * iv.setImageResource(R.drawable.avatar); } else {
		 * iv.setImageDrawable(getImageFromAsset(getApplicationContext(),
		 * mPickedAuthorImage)); }
		 */
		TextView tvId = (TextView) findViewById(R.id.tvDetailTostId); 
		tvId.setText(mPickedTostId);
		
		mAdView = new AdView(this);
        mAdView.setAdUnitId(Config.AD_UNIT_ID);
        mAdView.setAdSize(AdSize.BANNER);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.adView);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.addView(mAdView, params);
        mAdView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
	}
	
    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
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
		/* Dynamically change favorites icon in action bar */

		/* Check if already in favorites */
		sharedPref = getSharedPreferences(Config.PREFS, Context.MODE_PRIVATE);
		String fav = sharedPref.getString(Config.PREFS_FAVORITES_KEY,
				FAV_DEF_VALUE);
		isFavorite = checkIsFavorite(fav, mPickedTostId);

		MenuItem favoritesButton = menu.findItem(R.id.menu_favorites);
		if (isFavorite) {
			favoritesButton.setIcon(R.drawable.ic_action_important);
		} else {
			favoritesButton.setIcon(R.drawable.ic_action_not_important);
		}
		return super.onPrepareOptionsMenu(menu);

	}

	@Override
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
			/* Save or remove from favorites */
			MenuItem favButton = mainMenu.findItem(R.id.menu_favorites);
			
			sharedPref = getSharedPreferences(Config.PREFS,
					Context.MODE_PRIVATE);
			String fav = sharedPref.getString(Config.PREFS_FAVORITES_KEY,
					FAV_DEF_VALUE);

			/* Check if already in favorites */
			isFavorite = checkIsFavorite(fav, mPickedTostId);

			/* Write to favorites or remove */
			if (isFavorite) {
				// TODO: should remove from favorites
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
				Toast.makeText(getApplicationContext(), "Добавлено в избранное",
						Toast.LENGTH_SHORT).show();

				// Change menu icon		
				favButton.setIcon(R.drawable.ic_action_important);
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Menu hardware button open action bar menu
	 * 
	 * http://stackoverflow.com/questions/12277262/
	 * opening-submenu-in-action-bar-on-hardware-menu-button-click
	 */
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

}
