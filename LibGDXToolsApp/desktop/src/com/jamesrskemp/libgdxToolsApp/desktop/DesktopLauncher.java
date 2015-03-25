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

		new LwjglApplication(new LibGDXToolsApp(), config);
	}
}
