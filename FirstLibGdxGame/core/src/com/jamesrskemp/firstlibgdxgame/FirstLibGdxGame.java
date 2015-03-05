package com.jamesrskemp.firstlibgdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FirstLibGdxGame extends Game {
	FPSLogger fpsLogger;
	OrthographicCamera camera;
	Viewport viewport;

	SpriteBatch batch;
	TextureAtlas atlas;

	AssetManager manager = new AssetManager();

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
		manager.load("sounds/journey.mp3", Music.class);
		manager.load("sounds/alarm.ogg", Sound.class);
		manager.load("sounds/crash.ogg", Sound.class);
		manager.load("sounds/pop.ogg", Sound.class);
		manager.load("sounds/star.ogg", Sound.class);
		manager.load("sounds/fuel.ogg", Sound.class);
		manager.load("sounds/shield.ogg", Sound.class);
		manager.load("ThrustCopter.pack", TextureAtlas.class);
		manager.load("life.png", Texture.class);
		manager.load("fonts/impact-40.fnt", BitmapFont.class);
		manager.load("Explosion", ParticleEffect.class);
		manager.load("Smoke", ParticleEffect.class);
		manager.finishLoading();

		batch = new SpriteBatch();
		atlas = manager.get("ThrustCopter.pack", TextureAtlas.class);

		setScreen(new ThrustCopterScene(this));
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
	}
}
