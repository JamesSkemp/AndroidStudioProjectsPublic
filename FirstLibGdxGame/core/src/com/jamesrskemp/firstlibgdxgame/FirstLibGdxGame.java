package com.jamesrskemp.firstlibgdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FirstLibGdxGame extends Game {
	FPSLogger fpsLogger;
	OrthographicCamera camera;
	Viewport viewport;

	SpriteBatch batch;
	TextureAtlas atlas;

	AssetManager manager = new AssetManager();

	BitmapFont font;

	boolean soundEnabled;
	float soundVolume;

	public static final int screenWidth = 800;
	public static final int screenHeight = 480;

	public FirstLibGdxGame() {
		fpsLogger = new FPSLogger();
		camera = new OrthographicCamera();
		camera.position.set(screenWidth / 2, screenHeight / 2, 0);
		// Show the clear color as bars as needed.
		viewport = new FitViewport(screenWidth, screenHeight, camera);
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		soundEnabled = true;
		soundVolume = 1;

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void render() {
		fpsLogger.log();
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose() {
		batch.dispose();
		atlas.dispose();
		manager.dispose();
		font.dispose();
	}
}
