package com.jamesrskemp.firstopenglproject;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jamesrskemp.firstopenglproject.util.ShaderHelper;
import com.jamesrskemp.firstopenglproject.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by James on 2/16/2015.
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {
	// Two components per vertex.
	private static final int POSITION_COMPONENT_COUNT = 2;

	// float is 32 bits and byte has 8 bits of precision
	private static final int BYTES_PER_FLOAT = 4;
	// Will store data in native memory.
	private final FloatBuffer vertexData;

	private final Context context;

	// Store the linked program (shaders).
	private int program;

	public AirHockeyRenderer(Context context) {
		this.context = context;

		// Rectangle that's 9x14.
		float[] tableVertices = {
				0f, 0f,
				0f, 14f,
				9f, 14f,
				9f, 0f
		};

		float[] tableVerticesWithTriangles = {
				// Define in counter-clockwise order (winding order)
				// Triangle 1
				0f, 0f,
				9f, 14f,
				0f, 14f,

				// Triangle 2
				0f, 0f,
				9f, 0f,
				9f, 14f,

				// Line 1 (center line)
				0f, 7f,
				9f, 7f,

				// Mallets
				4.5f, 2f,
				4.5f, 12f
		};

		vertexData = ByteBuffer
				// allocate native memory that won't be garbage collected
				.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		// copy data from Dalvik's memory to navtive memory
		vertexData.put(tableVerticesWithTriangles);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Red, Green, Blue, alpha
		glClearColor(0.0f, 1.0f, 1.0f, 0.0f);

		String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
		String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);

		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
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
