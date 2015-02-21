package com.jamesrskemp.firstopenglproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class AirHockey6Activity extends ActionBarActivity {

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
			glSurfaceView.setRenderer(new AirHockey6Renderer(this));
			rendererSet = true;
		} else {
			Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_SHORT).show();
		}
		setContentView(glSurfaceView);
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
