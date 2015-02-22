package com.jamesrskemp.airhockey.objects;

import android.util.FloatMath;

import com.jamesrskemp.firstopenglproject.util.Geometry;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.*;

/**
 * Created by James on 2/22/2015.
 */
public class ObjectBuilder {
	private static final int FLOATS_PER_VERTEX = 3;
	private final float[] vertexData;
	private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();

	private int offset = 0;

	static interface DrawCommand {
		void draw();
	}

	static class GeneratedData {
		final float[] vertexData;
		final List<DrawCommand> drawList;

		GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
			this.vertexData = vertexData;
			this.drawList = drawList;
		}
	}

	private ObjectBuilder(int sizeInVertices) {
		vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
	}

	private static int sizeOfCircleInVertices(int numPoints) {
		return 1 + (numPoints + 1);
	}

	private static int sizeOfOpenCylinderInVertices(int numPoints) {
		return (numPoints + 1) * 2;
	}

	private void appendCircle(Geometry.Circle circle, int numPoints) {
		final int startVertex = offset / FLOATS_PER_VERTEX;
		final int numVertices = sizeOfCircleInVertices(numPoints);

		// Center point of fan
		vertexData[offset++] = circle.center.x;
		vertexData[offset++] = circle.center.y;
		vertexData[offset++] = circle.center.z;

		// Fan around the center point.
		for (int i = 0; i <= numPoints; i++) {
			float angleInRadians = ((float)i / (float) numPoints) * ((float) Math.PI * 2f);

			vertexData[offset++] = circle.center.x + circle.radius * FloatMath.cos(angleInRadians);
			vertexData[offset++] = circle.center.y;
			vertexData[offset++] = circle.center.z + circle.radius * FloatMath.sin(angleInRadians);
		}

		drawList.add(new DrawCommand() {
			@Override
			public void draw() {
				glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
			}
		});
	}

	private void appendOpenCylinder(Geometry.Cylinder cylinder, int numPoints) {
		final int startVertex = offset / FLOATS_PER_VERTEX;
		final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
		final float yStart = cylinder.center.y - (cylinder.height / 2f);
		final float yEnd = cylinder.center.y + (cylinder.height / 2f);

		for (int i = 0; i <= numPoints; i++) {
			float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);

			float xPosition = cylinder.center.x + cylinder.radius * FloatMath.cos(angleInRadians);
			float zPosition = cylinder.center.z + cylinder.radius * FloatMath.sin(angleInRadians);

			vertexData[offset++] = xPosition;
			vertexData[offset++] = yStart;
			vertexData[offset++] = zPosition;

			vertexData[offset++] = xPosition;
			vertexData[offset++] = yEnd;
			vertexData[offset++] = zPosition;
		}

		drawList.add(new DrawCommand() {
			@Override
			public void draw() {
				glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
			}
		});
	}

	private GeneratedData build() {
		return new GeneratedData(vertexData, drawList);
	}

	static GeneratedData createPuck(Geometry.Cylinder puck, int numPoints) {
		int size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints);

		ObjectBuilder builder = new ObjectBuilder(size);

		Geometry.Circle puckTop = new Geometry.Circle(puck.center.translateY(puck.height / 2f), puck.radius);

		builder.appendCircle(puckTop, numPoints);
		builder.appendOpenCylinder(puck, numPoints);

		return builder.build();
	}
}
