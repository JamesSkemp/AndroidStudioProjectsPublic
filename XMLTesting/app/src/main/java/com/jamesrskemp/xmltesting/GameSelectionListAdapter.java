package com.jamesrskemp.xmltesting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by James on 11/24/2014.
 */
public class GameSelectionListAdapter extends ArrayAdapter<VideoGame> {
	public GameSelectionListAdapter(Context context, List<VideoGame> viewModel) {
		super(context, 0, viewModel);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VideoGame item = getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view.
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_game_selection, parent, false);
		}

		TextView title = (TextView) convertView.findViewById(R.id.list_game_selection_title);
		title.setText(item.title);

		TextView system = (TextView) convertView.findViewById(R.id.list_game_selection_system);
		system.setText(item.system);

		return convertView;
	}
}
