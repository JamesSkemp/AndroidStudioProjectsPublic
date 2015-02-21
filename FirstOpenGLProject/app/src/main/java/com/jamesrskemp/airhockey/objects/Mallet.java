package com.jamesrskemp.airhockey.objects;

import com.jamesrskemp.airhockey.Constants;
import com.jamesrskemp.airhockey.data.VertexArray;
import com.jamesrskemp.airhockey.programs.ColorShaderProgram;

import static android.opengl.GLES20.*;

/**
 * Created by James on 2/21/2015.
 */
public class Mallet {
	// Two components per vertex (X, Y).
	private static final int POSITION_COMPONENT_COUNT = 2;
	// Three components per color (R, G, B).
	private static final int COLOR_COMPONENT_COUNT = 3;
	// Stride tells OpenGL where to get the next set of vertices
	private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

	private static final float[] VERTEX_DATA = {
			// X, Y, R, G, B
			0f, -0.4f, 0f, 0f, 1f,
			0f, 0.4f, 1f, 0f, 0f
	};

	private final VertexArray vertexArray;

	public Mallet() {
		vertexArray = new VertexArray(VERTEX_DATA);
	}

	public void bindData(ColorShaderProgram colorProgram) {
		vertexArray.setVertexAttribPointer(
				0,
				colorProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT,
				STRIDE
		);

		vertexArray.setVertexAttribPointer(
				POSITION_COMPONENT_COUNT,
				colorProgram.getColorAttributeLocation(),
				COLOR_COMPONENT_COUNT,
				STRIDE
		);
	}

	public void draw() {
		glDrawArrays(GL_POINTS, 0, 2);
	}
}
