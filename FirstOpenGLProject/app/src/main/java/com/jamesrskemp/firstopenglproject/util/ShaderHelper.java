package com.jamesrskemp.firstopenglproject.util;

import android.util.Log;

import static android.opengl.GLES20.*;

/**
 * Created by James on 2/17/2015.
 */
public class ShaderHelper {
	private static final String TAG = "ShaderHelper";

	public static int compileVertexShader(String shaderCode) {
		return compileShader(GL_VERTEX_SHADER, shaderCode);
	}

	public static int compileFragmentShader(String shaderCode) {
		return compileShader(GL_FRAGMENT_SHADER, shaderCode);
	}

	private static int compileShader(int type, String shaderCode) {
		// Reference id of the shader object we're creating.
		final int shaderObjectId = glCreateShader(type);

		if (shaderObjectId == 0) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Could not create new shader.");
			}

			return 0;
		}

		// Associate the shader source code with the shader object.
		glShaderSource(shaderObjectId, shaderCode);

		// Compile the shader / source code.
		glCompileShader(shaderObjectId);

		final int[] compileStatus = new int[1];
		// Read the compile status and store it to the 0th element of the array.
		glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

		if (LoggerConfig.ON) {
			// Human readable compile status message.
			Log.v(TAG, "Results of compiling source: " + "\n" + shaderCode + "\n:" + glGetShaderInfoLog(shaderObjectId));
		}

		if (compileStatus[0] == 0) {
			// Delete the shader object if compiling failed.
			glDeleteShader(shaderObjectId);

			if (LoggerConfig.ON) {
				Log.w(TAG, "Compilation of shader failed.");
			}

			return 0;
		}

		return shaderObjectId;
	}

	public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
		final int programObjectId = glCreateProgram();

		if (programObjectId == 0) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Could not create new program.");
			}

			return 0;
		}

		glAttachShader(programObjectId, vertexShaderId);
		glAttachShader(programObjectId, fragmentShaderId);
		// Join the shaders together.
		glLinkProgram(programObjectId);

		final int[] linkStatus = new int[1];
		glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

		if (LoggerConfig.ON) {
			Log.v(TAG, "Results of linking program:\n" + glGetProgramInfoLog(programObjectId));
		}

		if (linkStatus[0] == 0) {
			// Delete the object if it failed.
			glDeleteProgram(programObjectId);
			if (LoggerConfig.ON) {
				Log.w(TAG, "Linking of program failed.");
			}

			return 0;
		}

		return programObjectId;
	}
}
