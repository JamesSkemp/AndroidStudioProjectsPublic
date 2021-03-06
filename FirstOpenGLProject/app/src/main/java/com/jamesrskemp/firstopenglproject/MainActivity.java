package com.jamesrskemp.firstopenglproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurfaceView = new GLSurfaceView(this);

		final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2) {
			// Request a 2.0 compatible context.
			glSurfaceView.setEGLContextClientVersion(2);
			// Assign the renderer.
			glSurfaceView.setRenderer(new FirstOpenGLProjectRenderer());
			rendererSet = true;
		} else {
			Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_SHORT).show();
		}
		setContentView(glSurfaceView);

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		Button b1 = new Button(this);
		b1.setText(R.string.title_activity_air_hockey);
		b1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockeyActivity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b1,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		Button b2 = new Button(this);
		b2.setText(R.string.title_activity_air_hockey2);
		b2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockey2Activity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b2,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		Button b3 = new Button(this);
		b3.setText(R.string.title_activity_air_hockey3);
		b3.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockey3Activity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b3,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		Button b4 = new Button(this);
		b4.setText(R.string.title_activity_air_hockey4);
		b4.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockey4Activity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b4,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		Button b5 = new Button(this);
		b5.setText(R.string.title_activity_air_hockey5);
		b5.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockey5Activity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b5,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		Button b6 = new Button(this);
		b6.setText(R.string.title_activity_air_hockey6);
		b6.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockey6Activity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b6,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		Button b7 = new Button(this);
		b7.setText(R.string.title_activity_air_hockey7);
		b7.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockey7Activity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b7,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		Button b8 = new Button(this);
		b8.setText(R.string.title_activity_air_hockey8);
		b8.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockey8Activity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b8,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		Button b9 = new Button(this);
		b9.setText(R.string.title_activity_air_hockey9);
		b9.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockey9Activity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b9,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		Button b10 = new Button(this);
		b10.setText(R.string.title_activity_air_hockey10);
		b10.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), AirHockey10Activity.class);
				startActivity(intent);
			}
		});
		linearLayout.addView(b10,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		ScrollView scrollView = new ScrollView(this);
		scrollView.addView(linearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		this.addContentView(scrollView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (rendererSet) {
			glSurfaceView.onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (rendererSet) {
			glSurfaceView.onResume();
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

	public void viewAirHockey(View view) {
		Intent intent = new Intent(this, AirHockeyActivity.class);
		startActivity(intent);
	}
}
