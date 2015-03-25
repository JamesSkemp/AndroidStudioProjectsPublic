package com.jamesrskemp.libgdxToolsApp.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tiledmappacker.TiledMapPacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.jamesrskemp.libgdxToolsApp.LibGDXToolsApp;

import java.io.File;
import java.io.IOException;

public class DesktopLauncher {
	public static final String TAG = DesktopLauncher.class.getName();

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// None of this works.
		// http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=18377 suggests this is no longer supported.
		Gdx.app.log(TAG, new File("../").getAbsolutePath());

		Gdx.app.log(TAG, Gdx.files.external("../../../../../").file().getAbsolutePath());

		File sourceDirectory = Gdx.files.internal("originals").file();
		File packedDirectory = Gdx.files.internal("packed").file();
		TiledMapPacker tiledMapPacker = new TiledMapPacker();

		Gdx.app.log(TAG, sourceDirectory.getAbsolutePath());
		Gdx.app.log(TAG, packedDirectory.getAbsolutePath());

		try {
			tiledMapPacker.processMaps(sourceDirectory, packedDirectory, new TexturePacker.Settings());
		} catch (IOException e) {
			Gdx.app.log(TAG, e.getMessage());
			// do nothing
		}

		new LwjglApplication(new LibGDXToolsApp(), config);
	}
}
