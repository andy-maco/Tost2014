package com.maknc.tost2014;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.maknc.tost2014.adapters.TostsArrayAdapter;
import com.maknc.tost2014.parsers.JSONParser;

public class TostListFragment extends Fragment {
	
	List<HashMap<String, String>> tList = new ArrayList<HashMap<String, String>>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_tost_list,
				container, false);
		
		
		tList = getTosts(R.raw.newtost2014);
		
		//Log.d("AAA", "qList: " + qList);

		final TostsArrayAdapter mArrayAdapter = new TostsArrayAdapter(getActivity(), tList);
		
		ListView mListView = (ListView) rootView.findViewById(R.id.lvQuotesFrag);
		
		//Log.d("AAA", "mList: " + mListView);
		
		// Set the ArrayAdapter as the ListView's adapter.
		mListView.setAdapter(mArrayAdapter);
		mListView.setTextFilterEnabled(true);
		
		/* --- Making list clickable --- */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String pickedQuote = mArrayAdapter.getItem(position).get(
						Config.KEY_TEXT);
				String pickedTostId = mArrayAdapter.getItem(position).get(
						Config.KEY_ID);

				Intent mIntent = new Intent(getActivity(),
						TostActivity.class);
				mIntent.putExtra(Config.TAG_TOST_TEXT, pickedQuote);
				mIntent.putExtra(Config.TAG_TOST_ID, pickedTostId);
				startActivity(mIntent);

			}
		});
		
		
		

		return rootView;
	}
	
	
	
	/**
	 * Method for read JSON and save into List of HashMaps
	 * 
	 * @param url
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getTosts(int ResID) {

		ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();

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

				mItemList.add(putData(oneObject.getString(Config.JSON_ID),oneObject.getString(Config.JSON_TEXT)));


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
	private HashMap<String, String> putData(String id, String text) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(Config.KEY_ID, id);
		item.put(Config.KEY_TEXT, text);
		return item;
	}

	/**
	 * Method load images from assets (input imageUrl like "folder/image.jpg")
	 * 
	 * http://xjaphx.wordpress.com/2011/10/02/store-and-use-files-in-assets/
	 * 
	 * @param context
	 * @param imageUrl
	 * @return
	 */
	private Drawable getImageFromAsset(Context context, String imageUrl) {
		// TODO: pull out into separate helper class
		Drawable mDrawable = null;
		try {
			AssetManager mAssetManager = context.getAssets();
			// get input stream
			InputStream is = mAssetManager.open(imageUrl);
			// load image as Drawable
			mDrawable = Drawable.createFromStream(is, null);
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return mDrawable;
	}
}
