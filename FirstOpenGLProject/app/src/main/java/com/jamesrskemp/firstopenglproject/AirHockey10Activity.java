package com.jamesrskemp.firstopenglproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class AirHockey10Activity extends ActionBarActivity {

	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurfaceView = new GLSurfaceView(this);

		final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		// Store a reference to the renderer for touch events.
		final AirHockey10Renderer airHockeyRenderer = new AirHockey10Renderer(this);

		if (supportsEs2) {
			// Request a 2.0 compatible context.
			glSurfaceView.setEGLContextClientVersion(2);
			// Assign the renderer.
			glSurfaceView.setRenderer(airHockeyRenderer);
			rendererSet = true;
		} else {
			Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_SHORT).show();
		}
		setContentView(glSurfaceView);

		glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event != null) {
					// Convert the touch coordinates into normalized device coordinates.
					// Upper left is 0, 0, while the bottom right is the view dimensions in pixels.
					// We'll normalize these to the range of -1 to 1.
					final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
					// Android's y coordinates are inverted.
					final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						// Pass the action to the OpenGL thread.
						glSurfaceView.queueEvent(new Runnable() {
							@Override
							public void run() {
								airHockeyRenderer.handleTouchPress(normalizedX, normalizedY);
							}
						});
					} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
						// Pass the action to the OpenGL thread.
						glSurfaceView.queueEvent(new Runnable() {
							@Override
							public void run() {
								airHockeyRenderer.handleTouchDrag(normalizedX, normalizedY);
							}
						});
					}
					// We've consumed the event.
					return true;
				} else {
					return false;
				}
			}
		});
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
		getMenuInflater().inflate(R.menu.menu_air_hockey2, menu);
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
