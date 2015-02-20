package com.jamesrskemp.firstopenglproject.util;

/**
 * Created by James on 2/20/2015.
 */
public class MatrixHelper {
	/**
	 * frustumM() has a bug, and perspectiveM() is only supported by ICS+
	 * @param m Array to add the matrix data to. Must have at least 16 elements.
	 * @param yFovInDegrees Field of view in degrees. Must be less than 180.
	 * @param aspect Aspect ratio of the screen.
	 * @param n Distance to the near plane. Must be positive. Frustum starts at this point (top of pyramid).
	 * @param f Distance to the far plane. Must be greater than <code>n</code>. Frustum ends at this point (base of pyramid).
	 */
	public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f) {
		// The math requires radians.
		final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
		// Focal length of the camera.
		final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
		// OpenGL stores matrix data in column-major order.
		// Column 1
		m[0] = a / aspect;
		m[1] = 0f;
		m[2] = 0f;
		m[3] = 0f;

		// Column 2
		m[4] = 0f;
		m[5] = a;
		m[6] = 0f;
		m[7] = 0f;

		// Column 3
		m[8] = 0f;
		m[9] = 0f;
		m[10] = -((f + n) / (f - n));
		m[11] = -1f;

		// Column 4
		m[12] = 0f;
		m[13] = 0f;
		m[14] = -((2f * f * n) / (f - n));
		m[15] = 0f;
	}
}
