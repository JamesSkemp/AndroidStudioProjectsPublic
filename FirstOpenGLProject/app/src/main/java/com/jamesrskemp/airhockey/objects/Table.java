package com.jamesrskemp.airhockey.objects;

import com.jamesrskemp.airhockey.Constants;
import com.jamesrskemp.airhockey.data.VertexArray;
import com.jamesrskemp.airhockey.programs.TextureShaderProgram;

import static android.opengl.GLES20.*;

/**
 * Created by James on 2/21/2015.
 */
public class Table {
	// Two components per vertex (X, Y).
	private static final int POSITION_COMPONENT_COUNT = 2;
	// Two components per texture (S, T).
	private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
	// Stride tells OpenGL where to get the next set of vertices
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

	private static final float[] VERTEX_DATA = {
			// Triangle Fan
			// Start in the center, move to the bottom left corner, then bottom right, then back to center
			// Then keep going around clockwise from the center (first vertex).
			// X, Y, S, T
			0f, 0f, 0.5f, 0.5f,
			-0.5f, -0.8f, 0f, 0.9f,
			0.5f, -0.8f, 1f, 0.9f,
			0.5f, 0.8f, 1f, 0.1f,
			-0.5f, 0.8f, 0f, 0.1f,
			-0.5f, -0.8f, 0f, 0.9f,
	};

	private final VertexArray vertexArray;

	public Table() {
		vertexArray = new VertexArray(VERTEX_DATA);
	}

	public void bindData(TextureShaderProgram textureProgram) {
		vertexArray.setVertexAttribPointer(
				0,
				textureProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE
		);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				textureProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDINATES_COMPONENT_COUNT,
				STRIDE
		);
	}

	public void draw() {
		glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
	}
}
