package com.jamesrskemp.libgdx.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jamesrskemp.libgdx.canyonbunny.game.Assets;
import com.jamesrskemp.libgdx.canyonbunny.game.WorldController;
import com.jamesrskemp.libgdx.canyonbunny.game.WorldRenderer;

public class CanyonBunny extends ApplicationAdapter {
	private static final String TAG = CanyonBunny.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;

	private boolean paused;

	@Override
	public void create () {
		// TODO update log level as needed
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Initialize controller and renderer.
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		// Game is active on start.
		paused = false;
	}

	@Override
	public void render () {
		// Don't update the world if the game is paused.
		if (!paused) {
			// Update the game world by the time that has passed since last rendered frame.
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		// Sets the clear screen color.
		Gdx.gl.glClearColor(100/255f, 149/255f, 237/255f, 1);
		// Clear the screen.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render the game world to the screen.
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		Assets.instance.init(new AssetManager());
		paused = false;
	}

	@Override
	public void dispose() {
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
}
