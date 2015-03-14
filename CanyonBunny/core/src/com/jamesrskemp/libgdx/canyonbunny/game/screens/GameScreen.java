package com.jamesrskemp.libgdx.canyonbunny.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.jamesrskemp.libgdx.canyonbunny.game.WorldController;
import com.jamesrskemp.libgdx.canyonbunny.game.WorldRenderer;

/**
 * Created by James on 3/14/2015.
 */
public class GameScreen extends AbstractGameScreen {
	private static final String TAG = GameScreen.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;

	private boolean paused;

	public GameScreen(Game game) {
		super(game);
	}

	@Override
	public void render(float deltaTime) {
		if (!paused) {
			// Only update the world if we're not paused.
			worldController.update(deltaTime);
		}
		// Cornflower blue.
		Gdx.gl.glClearColor(0x64 / 255f, 0x95 / 255f, 0xed / 255f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		super.resume();
		// This is only called on Android.
		paused = false;
	}
}
