package com.jamesrskemp.firstlibgdxgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FirstLibGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	FPSLogger fpsLogger;
	OrthographicCamera camera;
	Texture background;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		fpsLogger = new FPSLogger();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		background = new Texture("background.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		fpsLogger.log();
		updateScene();
		drawScene();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	public void updateScene() {
	}

	public void drawScene() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();
	}
}
