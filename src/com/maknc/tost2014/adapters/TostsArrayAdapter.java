package com.maknc.tost2014.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maknc.tost2014.Config;
import com.maknc.tost2014.R;

/**
 * Adapter for ListView in quotes list activity
 */
public class TostsArrayAdapter extends ArrayAdapter<HashMap<String, String>> {
	
	private static final int ROW_LAYOUT_ID = R.layout.row_tost;

	private final Context context;
	private List<HashMap<String, String>> values;

	public TostsArrayAdapter(Context context,
			List<HashMap<String, String>> values) {
		super(context, ROW_LAYOUT_ID, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(ROW_LAYOUT_ID, parent, false);

		TextView tv1 = (TextView) rowView.findViewById(R.id.tvQuoteLine);
		TextView tv2 = (TextView) rowView.findViewById(R.id.tvQuoteTagLine);
		TextView tv3 = (TextView) rowView.findViewById(R.id.tvQuoteNum);
		ImageView ivStar = (ImageView) rowView.findViewById(R.id.imageRowStar);

		HashMap<String, String> currentValue = values.get(position);

		tv1.setText(currentValue.get(Config.KEY_TEXT));
		tv2.setText("пример");
		tv3.setText(currentValue.get(Config.KEY_ID));
		/*int itemNum = position + 1;
		tv3.setText(itemNum + "");*/
		
		String isFavorite = currentValue.get(Config.KEY_FAV);
		if (isFavorite.equals("true")) {
			ivStar.setImageResource(R.drawable.ic_action_important);
		}
		
		return rowView;
	}
	
	public void updateItemsList(List<HashMap<String, String>> items) {
		this.values.clear();
	    this.values.addAll(items);
	    notifyDataSetChanged();
	}
}
