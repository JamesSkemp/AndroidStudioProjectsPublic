package com.jamesrskemp.firstopenglproject;

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

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * Created by James on 2/19/2015.
 */
public class AirHockey3Renderer implements GLSurfaceView.Renderer {
	// Two components per vertex (X, Y).
	private static final int POSITION_COMPONENT_COUNT = 2;
	// Three components per color (R, G, B).
	private static final int COLOR_COMPONENT_COUNT = 3;
	// float is 32 bits and byte has 8 bits of precision
	private static final int BYTES_PER_FLOAT = 4;
	// stride tells OpenGL where to get the next set of vertices
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

	// Will store data in native memory.
	private final FloatBuffer vertexData;

	private final Context context;

	// Store the linked program (shaders).
	private int program;

	// Fragment attribute variable name.
	private static final String A_COLOR = "a_Color";
	// Attribute location in a particular program.
	private int aColorLocation;

	private static final String A_POSITION = "a_Position";
	private int aPositionLocation;
	// Vertex shader uniform name.
	private static final String U_MATRIX = "u_Matrix";
	// Hold our 4x4 matrix data.
	private final float[] projectionMatrix = new float[16];
	// Location of the matrix.
	private int uMatrixLocation;

	public AirHockey3Renderer(Context context) {
		this.context = context;

		float[] tableVerticesWithTriangles = {
				// Define in counter-clockwise order (winding order)
				// Triangle Fan
				// Start in the center, move to the bottom left corner, then bottom right, then back to center
				// Then keep going around clockwise from the center (first vertex).
				// X, Y, R, G, B
				0, 0, 1f, 1f, 1f,
				-0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
				0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
				0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
				-0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
				-0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

				// Line 1 (center line)
				-0.5f, 0f, 1f, 0f, 0f,
				0.5f, 0f, 1f, 0f, 0f,

				// Mallets
				0f, -0.4f, 0f, 0f, 1f,
				0f, 0.4f, 1f, 0f, 0f
		};

		vertexData = ByteBuffer
				// allocate native memory that won't be garbage collected
				.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		// copy data from Dalvik's memory to native memory
		vertexData.put(tableVerticesWithTriangles);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Red, Green, Blue, alpha
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_color_matrix_vertex_shader);
		String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_color_fragment_shader);

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
		aColorLocation = glGetAttribLocation(program, A_COLOR);
		// Store the location of the position attribute.
		// We could set this if we wanted to, otherwise it's automatically handled.
		aPositionLocation = glGetAttribLocation(program, A_POSITION);
		// Store the location of the matrix uniform.
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

		// Position our buffer pointer at the beginning of the data.
		vertexData.position(0);
		// Find the data in the vertextData buffer.
		glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
		// Data is linked to the attribute, so now we enable it.
		glEnableVertexAttribArray(aPositionLocation);

		// Position our buffer pointer after the first position data.
		vertexData.position(POSITION_COMPONENT_COUNT);
		// Find the data in the vertexData buffer.
		glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
		// Data is linked to the attribute, so now we enable it.
		glEnableVertexAttribArray(aColorLocation);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Set the viewport to fill the entire surface.
		glViewport(0, 0, width, height);

		final float aspectRatio = width > height ?
				(float)width / (float)height :
				(float)height / (float)width;

		if (width > height) {
			// Landscape, so expand the width to the aspect ratio.
			orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
		} else {
			// Portrait, so expand the height to the aspect ratio.
			orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Clear the rendering surface.
		glClear(GL_COLOR_BUFFER_BIT);
		// Send the matrix to the shader.
		glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

		// Draw triangles, starting at the beginning, and read in six vertices (2 sets of 3, which means 2 triangles).
		glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

		// Draw a line, with two vertices, six in.
		glDrawArrays(GL_LINES, 6, 2);

		// Blue mallet
		glDrawArrays(GL_POINTS, 8, 1);
		// Red mallet
		glDrawArrays(GL_POINTS, 9, 1);
	}
}
