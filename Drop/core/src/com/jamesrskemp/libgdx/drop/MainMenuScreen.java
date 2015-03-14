package com.jamesrskemp.libgdx.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by James on 3/13/2015.
 */
public class MainMenuScreen implements Screen {
	final DropGame game;

	OrthographicCamera camera;

	public MainMenuScreen(final DropGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Good practice to update the camera once per frame.
		camera.update();

		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, "Welcome to Drop!", 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin.", 100, 100);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			dispose();
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}
}
