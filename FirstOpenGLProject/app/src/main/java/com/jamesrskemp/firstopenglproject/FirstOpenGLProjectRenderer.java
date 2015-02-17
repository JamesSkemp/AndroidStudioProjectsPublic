package com.jamesrskemp.firstopenglproject;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by James on 2/16/2015.
 */
public class FirstOpenGLProjectRenderer implements GLSurfaceView.Renderer {

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Red, Green, Blue, alpha
		glClearColor(1.0f, 0.0f, 1.0f, 0.0f);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Set the viewport to fill the entire surface.
		glViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Clear the rendering surface.
		glClear(GL_COLOR_BUFFER_BIT);
	}
}
