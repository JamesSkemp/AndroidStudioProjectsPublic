package com.jamesrskemp.firstlibgdxgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FirstLibGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	FPSLogger fpsLogger;
	OrthographicCamera camera;
	Texture background;
	TextureRegion terrainBelow;
	TextureRegion terrainAbove;
	float terrainOffset;
	Animation plane;
	float planeAnimTime;

	@Override
	public void create () {
		batch = new SpriteBatch();
		fpsLogger = new FPSLogger();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		background = new Texture("background.png");
		terrainBelow = new TextureRegion(new Texture("groundGrass.png"));
		terrainAbove = new TextureRegion(terrainBelow);
		terrainAbove.flip(true, true);
		terrainOffset = 0;
		plane = new Animation(0.05f,
				new TextureRegion(new Texture("planeRed1.png")),
				new TextureRegion(new Texture("planeRed2.png")),
				new TextureRegion(new Texture("planeRed3.png")),
				new TextureRegion(new Texture("planeRed2.png"))
				);
		plane.setPlayMode(Animation.PlayMode.LOOP);
		planeAnimTime = 0;
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
		// deltaTime will be 1/fps
		float deltaTime = Gdx.graphics.getDeltaTime();
		//terrainOffset -= 200 * deltaTime;
		planeAnimTime += deltaTime;
	}

	public void drawScene() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// The background is not on top of anything else, so disable blending temporarily.
		batch.disableBlending();
		batch.draw(background, 0, 0);
		// Re-enable blending as our next items will have some transparency.
		batch.enableBlending();
		// Draw our ceiling and ground.
		batch.draw(terrainBelow, terrainOffset, 0);
		batch.draw(terrainBelow, terrainOffset + terrainBelow.getRegionWidth(), 0);
		batch.draw(terrainAbove, terrainOffset, 480 - terrainAbove.getRegionHeight());
		batch.draw(terrainAbove, terrainOffset + terrainAbove.getRegionWidth(), 480 - terrainAbove.getRegionHeight());
		// Draw our plane.
		batch.draw(plane.getKeyFrame(planeAnimTime), 350, 200);
		batch.end();
	}
}
