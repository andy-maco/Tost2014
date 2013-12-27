package com.maknc.tost2014;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.maknc.tost2014.adapters.TostsArrayAdapter;
import com.maknc.tost2014.parsers.JSONParser;

public class TostListStarFragment extends Fragment {
	
	private SharedPreferences sharedPref;
	private TostsArrayAdapter mArrayAdapter;
	
	List<HashMap<String, String>> tList = new ArrayList<HashMap<String, String>>();
	
	private View mRootView;
	private TextView mStarEmpty;
	private ListView mListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_tost_list_star,
				container, false);

		
		/* Read favorites list */
		sharedPref = getActivity().getSharedPreferences(Config.PREFS, Context.MODE_PRIVATE);
		String defValue = "";
		String fav = sharedPref.getString(Config.PREFS_FAVORITES_KEY, defValue);
		
		//mStarEmpty = (TextView)mRootView.findViewById(R.id.tvStarEmpty);
		//mStarEmpty.setText(fav);
		
		/* Load list */
		tList = getStarTosts(R.raw.newtost2014, fav);
		
		//Log.d("AAA", "qList: " + qList);

		mArrayAdapter = new TostsArrayAdapter(getActivity(), tList);
		
		mListView = (ListView) mRootView.findViewById(R.id.lvTostsStarFrag);
		
		// Set the ArrayAdapter as the ListView's adapter.
		mListView.setAdapter(mArrayAdapter);
		mListView.setTextFilterEnabled(true);
		
		/* --- Making list clickable --- */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String pickedTost = mArrayAdapter.getItem(position).get(
						Config.KEY_TEXT);
				String pickedTostId = mArrayAdapter.getItem(position).get(
						Config.KEY_ID);

				Intent mIntent = new Intent(getActivity(),
						DetailActivity.class);
				mIntent.putExtra(Config.TAG_TOST_TEXT, pickedTost);
				mIntent.putExtra(Config.TAG_TOST_ID, pickedTostId);
				startActivity(mIntent);

			}
		});
				


		return mRootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		/* Read favorites list */
		sharedPref = getActivity().getSharedPreferences(Config.PREFS, Context.MODE_PRIVATE);
		String defValue = "";
		String favReload = sharedPref.getString(Config.PREFS_FAVORITES_KEY, defValue);
		
		//mStarEmpty.setText(favReload);
		
		/* Reload */
		tList.clear();
		tList = getStarTosts(R.raw.newtost2014, favReload);	
		//Log.d("OUT", tList + "");


		if (mArrayAdapter == null) {
			mArrayAdapter = new TostsArrayAdapter(getActivity(), tList);
	        mListView.setAdapter(mArrayAdapter);
	        //Log.d("OUT", "Adapter NULL");
	    } else { 
	    	//Log.d("OUT", "Adapter EXIST");
	    	mArrayAdapter.updateItemsList(tList);
	    }
		
		/* 
		// Works but not good
		mArrayAdapter = new TostsArrayAdapter(getActivity(), tList);
        mListView.setAdapter(mArrayAdapter);
        */
			
	};
	
	/**
	 * Method for read JSON and save into List of HashMaps
	 * 
	 * @param url
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getStarTosts(int ResID, String fv) {

		ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();
		
		boolean isFav = false;

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject jObject = jParser.getJSONFromRaw(getActivity(), ResID);

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
				
				if (isFav) {
					mItemList.add(putData(currentId,currentText,isFav + ""));
				}


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
