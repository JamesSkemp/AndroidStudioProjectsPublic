package com.jamesrskemp.gridlayouttesting;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int gridSize = 15;

		GridLayout mainGrid = (GridLayout)findViewById(R.id.main_grid);
		mainGrid.setColumnCount(gridSize);
		mainGrid.setRowCount(gridSize);

		for (int col = 0; col < gridSize; col++) {
			for (int row = 0; row < gridSize; row++) {
				Button button = new Button(this);
				button.setText(String.format("%1$s %2$s", col, row));

				GridLayout.LayoutParams gridLayoutParams = new GridLayout.LayoutParams();
				gridLayoutParams.columnSpec = GridLayout.spec(col, 1);
				gridLayoutParams.rowSpec = GridLayout.spec(row, 1);
				gridLayoutParams.setMargins(0, 0, 0, 0);
				mainGrid.addView(button, gridLayoutParams);
			}
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
