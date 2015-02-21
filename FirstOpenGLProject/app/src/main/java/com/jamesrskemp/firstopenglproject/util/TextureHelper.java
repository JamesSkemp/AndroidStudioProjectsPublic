package com.jamesrskemp.firstopenglproject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

/**
 * Created by James on 2/21/2015.
 */
public class TextureHelper {
	private static final String TAG = "TextureHelper";

	/**
	 * Loads a resource into OpenGL as a texture object.
	 * @param context Android context.
	 * @param resourceId Resource to load. Should have dimensions of power of two (128, 256, 512), but not necessarily square.
	 * @return Id of the loaded OpenGL texture object.
	 */
	public static int loadTexture(Context context, int resourceId) {
		final int[] textureObjectIds = new int[1];
		// Generate a texture object.
		glGenTextures(1, textureObjectIds, 0);

		if (textureObjectIds[0] == 0) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Could not generate a new OpenGL texture object.");
			}
			return 0;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		// Get the original image data.
		options.inScaled = false;

		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

		if (bitmap == null) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
			}

			glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}
		// Treat the texture object as a 2D texture.
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

		// Use trilinear filtering for minification.
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		// Use bilinear filtering for magnification.
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		// Copies bitmap data to loaded texture object.
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

		// Release the bitmap.
		bitmap.recycle();

		// Generate mipmaps, used for minification.
		glGenerateMipmap(GL_TEXTURE_2D);

		// Unbind from the texture we've been working with.
		glBindTexture(GL_TEXTURE_2D, 0);

		return textureObjectIds[0];
	}
}
