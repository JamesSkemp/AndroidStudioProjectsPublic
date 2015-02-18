package com.jamesrskemp.firstopenglproject;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jamesrskemp.firstopenglproject.util.LoggerConfig;
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

	// Fragment uniform variable name.
	private static final String U_COLOR = "u_Color";
	// Uniform's location in a particular program.
	private int uColorLocation;

	private static final String A_POSITION = "a_Position";
	private int aPositionLocation;

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
				-0.5f, -0.5f,
				0.5f, 0.5f,
				-0.5f, 0.5f,

				// Triangle 2
				-0.5f, -0.5f,
				0.5f, -0.5f,
				0.5f, 0.5f,

				// Line 1 (center line)
				-0.5f, 0f,
				0.5f, 0f,

				// Mallets
				0f, -0.25f,
				0f, 0.25f
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
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
		String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);

		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

		if (LoggerConfig.ON) {
			// Will validate the program.
			// If the if statement changes, update the method to check the LoggerConfig.ON status.
			ShaderHelper.validateProgram(program);
		}

		// Use the program when drawing to the screen.
		glUseProgram(program);
		// Get the color location so we can update it later.
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		// Store the location of the position attribute.
		// We could set this if we wanted to, otherwise it's automatically handled.
		aPositionLocation = glGetAttribLocation(program, A_POSITION);

		// Position our buffer pointer at the beginning of the data.
		vertexData.position(0);
		// Find the data in the vertextData buffer.
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
		// Data is linked to the attribute, so now we enable it.
		glEnableVertexAttribArray(aPositionLocation);
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

		// Uniforms don't have defaults, so start with a white table.
		glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
		// Draw triangles, starting at the beginning, and read in six vertices (2 sets of 3, which means 2 triangles).
		glDrawArrays(GL_TRIANGLES, 0, 6);

		// Red
		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		// Draw a line, with two vertices, six in.
		glDrawArrays(GL_LINES, 6, 2);

		// Blue mallet
		glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
		glDrawArrays(GL_POINTS, 8, 1);
		// Red mallet
		glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
		glDrawArrays(GL_POINTS, 9, 1);
	}
}
