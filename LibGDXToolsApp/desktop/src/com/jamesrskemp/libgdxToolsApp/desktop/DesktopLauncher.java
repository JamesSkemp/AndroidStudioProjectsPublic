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
	public static void main (String[] arg) {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.useIndexes = false;

		//TexturePacker.process(settings, "raw", "packed", "tiledexampletiles.atlas");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		new LwjglApplication(new LibGDXToolsApp(), config);
	}
}
