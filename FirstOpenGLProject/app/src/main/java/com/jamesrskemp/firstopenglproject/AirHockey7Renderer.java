package com.jamesrskemp.firstopenglproject;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jamesrskemp.airhockey.objects.Mallet3D;
import com.jamesrskemp.airhockey.objects.Table;
import com.jamesrskemp.airhockey.objects.Puck;
import com.jamesrskemp.airhockey.programs.TextureShaderProgram;
import com.jamesrskemp.airhockey.programs.UniformColorShaderProgram;
import com.jamesrskemp.firstopenglproject.util.MatrixHelper;
import com.jamesrskemp.firstopenglproject.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

/**
 * Created by James on 2/22/2015.
 */
public class AirHockey7Renderer implements GLSurfaceView.Renderer {
	private final Context context;

	// Equivalent to a camera.
	private final float[] viewMatrix = new float[16];
	// Create the illusion of 3D.
	private final float[] viewProjectionMatrix = new float[16];
	// Place our objects in the scene.
	private final float[] modelViewProjectionMatrix = new float[16];

	// Hold our 4x4 matrix data.
	private final float[] projectionMatrix = new float[16];
	// Translation matrix to move the table.
	private final float[] modelMatrix = new float[16];

	private Table table;
	private Mallet3D mallet;
	private Puck puck;

	private TextureShaderProgram textureProgram;
	private UniformColorShaderProgram colorProgram;

	private int texture;

	public AirHockey7Renderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Red, Green, Blue, alpha
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		table = new Table();
		mallet = new Mallet3D(0.08f, 0.15f, 32);
		puck = new Puck(0.06f, 0.02f, 32);

		textureProgram = new TextureShaderProgram(context);
		colorProgram = new UniformColorShaderProgram(context);

		texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Set the viewport to fill the entire surface.
		glViewport(0, 0, width, height);

		MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);

		setLookAtM(
				// Start at point 0 in our viewMatrix
				viewMatrix, 0,
				// Put our camera/eye at this point (1.2 units and 2.2 units back)
				0f, 1.2f, 2.2f,
				// Look at the origin
				0f, 0f, 0f,
				// Straight up, with no rotation
				0f, 1f, 0f
		);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Clear the rendering surface.
		glClear(GL_COLOR_BUFFER_BIT);

		// Cache the results of projectionMatrix * viewMatrix.
		multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

		// Draw the table.
		positionTableInScene();
		textureProgram.useProgram();
		textureProgram.setUniforms(modelViewProjectionMatrix, texture);
		table.bindData(textureProgram);
		table.draw();

		// Draw the mallets.
		positionObjectInScene(0f, mallet.height / 2f, -0.4f);
		colorProgram.useProgram();
		colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
		mallet.bindData(colorProgram);
		mallet.draw();

		positionObjectInScene(0f, mallet.height / 2f, 0.4f);
		colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
		mallet.draw();

		// Draw the puck.
		positionObjectInScene(0f, puck.height / 2f, 0f);
		colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
		puck.bindData(colorProgram);
		puck.draw();
	}

	private void positionTableInScene() {
		// Rotate the table so it's flat, instead of on edge.
		setIdentityM(modelMatrix, 0);
		rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
		multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
	}

	private void positionObjectInScene(float x, float y, float z) {
		setIdentityM(modelMatrix, 0);
		translateM(modelMatrix, 0, x, y, z);
		multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
	}
}