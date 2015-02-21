package com.jamesrskemp.firstopenglproject;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jamesrskemp.airhockey.objects.Mallet;
import com.jamesrskemp.airhockey.objects.SimpleTable;
import com.jamesrskemp.airhockey.programs.ColorShaderProgram;
import com.jamesrskemp.firstopenglproject.util.MatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by James on 2/21/2015.
 */
public class AirHockey6Renderer implements GLSurfaceView.Renderer {
	private final Context context;
	// Hold our 4x4 matrix data.
	private final float[] projectionMatrix = new float[16];
	// Translation matrix to move the table.
	private final float[] modelMatrix = new float[16];

	private SimpleTable table;
	private Mallet mallet;

	private ColorShaderProgram tableColorProgram;
	private ColorShaderProgram malletColorProgram;

	public AirHockey6Renderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Red, Green, Blue, alpha
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		table = new SimpleTable();
		mallet = new Mallet();

		tableColorProgram = new ColorShaderProgram(context);
		malletColorProgram = new ColorShaderProgram(context);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Set the viewport to fill the entire surface.
		glViewport(0, 0, width, height);

		MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
		// Set the matrix to an identity matrix.
		setIdentityM(modelMatrix, 0);
		// Move our table -2.5 along the z-axis (back).
		translateM(modelMatrix, 0, 0f, 0f, -2.5f);
		// Rotate our table -60 degrees along the x-axis (move the top back).
		rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

		// Temporary matrix to put the result into.
		final float[] temp = new float[16];
		// Multiply the projection and model matrices. Results go into temp.
		multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
		// Put the results back into the projection matrix.
		System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Clear the rendering surface.
		glClear(GL_COLOR_BUFFER_BIT);

		// Draw the table.
		tableColorProgram.useProgram();
		tableColorProgram.setUniforms(projectionMatrix);
		table.bindData(tableColorProgram);
		table.draw();

		// Draw the mallets.
		malletColorProgram.useProgram();
		malletColorProgram.setUniforms(projectionMatrix);
		mallet.bindData(malletColorProgram);
		mallet.draw();
	}
}
