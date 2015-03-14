package com.jamesrskemp.libgdx.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.jamesrskemp.libgdx.canyonbunny.game.Assets;
import com.jamesrskemp.libgdx.canyonbunny.game.screens.MenuScreen;

public class CanyonBunny extends Game {
	@Override
	public void create () {
		// TODO update log level as needed
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Start game at menu screen.
		setScreen(new MenuScreen(this));
	}
}
