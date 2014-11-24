package com.jamesrskemp.xmltesting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;


public class GamesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games);

		parseData();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_games, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void parseData() {
		String jsonFileName = "VideoGames.json";
		String filePath = Environment.getExternalStorageDirectory() + "/Download/" + jsonFileName;
		try {
			File file = new File(filePath);
			InputStream fis = null;
			fis = new BufferedInputStream(new FileInputStream(file));
			GamesJsonParser parser = new GamesJsonParser();

			List<VideoGame> videoGames = parser.readJsonStream(fis);
			VideoGameComparer comparer = new VideoGameComparer();

			Collections.sort(videoGames, comparer);

			ArrayAdapter<VideoGame> gamesAdapter = new GameSelectionListAdapter(this, videoGames);

			ListView listView = (ListView)findViewById(R.id.list_games);
			listView.setAdapter(gamesAdapter);


		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
