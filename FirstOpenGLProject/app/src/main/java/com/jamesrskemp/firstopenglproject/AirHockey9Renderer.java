package com.jamesrskemp.firstopenglproject;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jamesrskemp.airhockey.objects.Mallet3D;
import com.jamesrskemp.airhockey.objects.Puck;
import com.jamesrskemp.airhockey.objects.Table;
import com.jamesrskemp.airhockey.programs.TextureShaderProgram;
import com.jamesrskemp.airhockey.programs.UniformColorShaderProgram;
import com.jamesrskemp.firstopenglproject.util.Geometry;
import com.jamesrskemp.firstopenglproject.util.MatrixHelper;
import com.jamesrskemp.firstopenglproject.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

/**
 * Created by James on 2/22/2015.
 */
public class AirHockey9Renderer implements GLSurfaceView.Renderer {
	private final Context context;

	// Equivalent to a camera.
	private final float[] viewMatrix = new float[16];
	// Create the illusion of 3D.
	private final float[] viewProjectionMatrix = new float[16];
	// Place our objects in the scene.
	private final float[] modelViewProjectionMatrix = new float[16];
	// Inverted matrix to undo the view and projection matrices.
	// Helps with converting a 2D touch to a Ray in 3D.
	private final float[] invertedViewProjectionMatrix = new float[16];

	// Hold our 4x4 matrix data of where objects are in space (x, y, z, w).
	private final float[] projectionMatrix = new float[16];
	// Translation matrix to move an object.
	private final float[] modelMatrix = new float[16];

	private Table table;
	private Mallet3D mallet;
	private Puck puck;

	private boolean malletPressed = false;
	private Geometry.Point blueMalletPosition;

	// Create the bounds of our table.
	private final float leftBound = -0.5f;
	private final float rightBound = 0.5f;
	private final float farBound = -0.8f;
	private final float nearBound = 0.8f;

	private TextureShaderProgram textureProgram;
	private UniformColorShaderProgram colorProgram;

	private int texture;

	public AirHockey9Renderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Red, Green, Blue, alpha
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		table = new Table();
		mallet = new Mallet3D(0.08f, 0.15f, 32);
		puck = new Puck(0.06f, 0.02f, 32);

		// Initialize the position of the player's mallet.
		blueMalletPosition = new Geometry.Point(0f, mallet.height / 2f, 0.4f);

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
		// Will help to convert a 2D touch to 3D.
		invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);

		// Draw the table.
		positionTableInScene();
		textureProgram.useProgram();
		textureProgram.setUniforms(modelViewProjectionMatrix, texture);
		table.bindData(textureProgram);
		table.draw();

		// Draw the mallets.
		// Red mallet.
		positionObjectInScene(0f, mallet.height / 2f, -0.4f);
		colorProgram.useProgram();
		colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
		mallet.bindData(colorProgram);
		mallet.draw();

		// Blue / player mallet.
		positionObjectInScene(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z);
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

	public void handleTouchPress(float normalizedX, float normalizedY) {
		Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);

		Geometry.Sphere malletBoundingSphere = new Geometry.Sphere(new Geometry.Point(
				blueMalletPosition.x,
				blueMalletPosition.y,
				blueMalletPosition.z
		), mallet.height / 2f);

		malletPressed = Geometry.intersects(malletBoundingSphere, ray);
	}

	public void handleTouchDrag(float normalizedX, float normalizedY) {
		if (malletPressed) {
			Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
			// Define a plane representing our air hockey table.
			Geometry.Plane plane = new Geometry.Plane(new Geometry.Point(0, 0, 0), new Geometry.Vector(0, 1, 0));
			// Find where the touched point intersects the plane. Move the object along this plane.
			Geometry.Point touchedPoint = Geometry.intersectionPoint(ray, plane);
			// Move the player mallet to that spot.
			blueMalletPosition = new Geometry.Point(
					clamp(touchedPoint.x, leftBound + mallet.radius, rightBound - mallet.radius),
					mallet.height / 2f,
					// 0f because we don't want it to cross the center line.
					clamp(touchedPoint.z, 0f + mallet.radius, nearBound - mallet.radius)
			);
		}
	}

	private Geometry.Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY) {
		// Pick a point on the near and far planes, with the normalized device coordinates, and draw a line between them.
		final float[] nearPointNdc = { normalizedX, normalizedY, -1, 1 };
		final float[] farPointNdc = { normalizedX, normalizedY, 1, 1 };

		final float[] nearPointWorld = new float[4];
		final float[] farPointWorld = new float[4];

		// Undo the perspective divide.
		multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
		multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);
		divideByW(nearPointWorld);
		divideByW(farPointWorld);

		Geometry.Point nearPointRay = new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
		Geometry.Point farPointRay = new Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

		return new Geometry.Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));
	}

	/**
	 * Divide x, y, and z by their w value.
	 * @param vector x, y, z, and w coordinates.
	 */
	private void divideByW(float[] vector) {
		vector[0] /= vector[3];
		vector[1] /= vector[3];
		vector[2] /= vector[3];
	}

	/**
	 * Keeps a value within a particular range.
	 * @param value Value to check.
	 * @param min Minimum value.
	 * @param max Maximum value.
	 * @return Value within the range.
	 */
	private float clamp(float value, float min, float max) {
		return Math.min(max, Math.max(value, min));
	}
}