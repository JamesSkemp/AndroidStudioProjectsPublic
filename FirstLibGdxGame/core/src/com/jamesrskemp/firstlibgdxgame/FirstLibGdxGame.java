package com.jamesrskemp.firstlibgdxgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FirstLibGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	FPSLogger fpsLogger;
	OrthographicCamera camera;
	Viewport viewport;
	TextureRegion background;
	TextureRegion terrainBelow;
	TextureRegion terrainAbove;
	float terrainOffset;
	Animation plane;
	float planeAnimTime;
	Vector2 planeVelocity = new Vector2();
	Vector2 planePosition = new Vector2();
	Vector2 planeDefaultPosition = new Vector2();
	Vector2 gravity = new Vector2();
	// Friction.
	private static final Vector2 damping = new Vector2(0.99f, 0.99f);
	TextureAtlas atlas;
	Vector3 touchPosition = new Vector3();
	Vector2 tmpVector = new Vector2();
	private static final int TOUCH_IMPULSE = 500;
	TextureRegion tapIndicator;
	TextureRegion tap1;
	TextureRegion gameOver;
	float tapDrawTime;
	private static final float TAP_DRAW_TIME_MAX = 1.0f;
	GameState gameState = GameState.INIT;
	Vector2 scrollVelocity = new Vector2();

	static enum GameState {
		/**
		 * Initialize
		 */
		INIT,
		ACTION,
		GAME_OVER
	}

	@Override
	public void create () {
		fpsLogger = new FPSLogger();
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("ThrustCopter.pack"));
		camera = new OrthographicCamera();
		camera.position.set(400, 240, 0);
		// Show the clear color as bars as needed.
		viewport = new FitViewport(800, 480, camera);
		background = atlas.findRegion("background");
		terrainBelow = atlas.findRegion("groundGrass");
		terrainAbove = new TextureRegion(terrainBelow);
		terrainAbove.flip(true, true);
		plane = new Animation(0.05f,
				atlas.findRegion("planeRed1"),
				atlas.findRegion("planeRed2"),
				atlas.findRegion("planeRed3"),
				atlas.findRegion("planeRed2")
				);
		plane.setPlayMode(Animation.PlayMode.LOOP);
		tapIndicator = atlas.findRegion("tap2");
		tap1 = atlas.findRegion("tap1");
		gameOver = atlas.findRegion("gameover");

		resetScene();
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
		if (Gdx.input.justTouched()) {
			if (gameState == GameState.INIT) {
				gameState = GameState.ACTION;
				return;
			}

			if (gameState == GameState.GAME_OVER) {
				gameState = GameState.INIT;
				resetScene();
				return;
			}

			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			// Convert touch position based upon the camera.
			camera.unproject(touchPosition);
			tmpVector.set(planePosition.x, planePosition.y);
			tmpVector.sub(touchPosition.x, touchPosition.y).nor();
			planeVelocity.mulAdd(tmpVector,
					TOUCH_IMPULSE - MathUtils.clamp(Vector2.dst(touchPosition.x, touchPosition.y, planePosition.x, planePosition.y), 0, TOUCH_IMPULSE)
			);

			tapDrawTime = TAP_DRAW_TIME_MAX;
		}

		if (gameState == GameState.INIT || gameState == GameState.GAME_OVER) {
			return;
		}

		// deltaTime will be 1/fps
		float deltaTime = Gdx.graphics.getDeltaTime();

		tapDrawTime -= deltaTime;

		//terrainOffset -= 200 * deltaTime;
		planeAnimTime += deltaTime;
		planeVelocity.scl(damping);
		planeVelocity.add(gravity);
		planeVelocity.add(scrollVelocity);

		planePosition.mulAdd(planeVelocity, deltaTime);

		terrainOffset -= planePosition.x - planeDefaultPosition.x;
		// Keep the plane in the same place on the x-axis.
		planePosition.x = planeDefaultPosition.x;
		if (terrainOffset * -1 > terrainBelow.getRegionWidth()) {
			terrainOffset = 0;
		}

		if (terrainOffset > 0) {
			terrainOffset = -terrainBelow.getRegionWidth();
		}

		if (planePosition.y < terrainBelow.getRegionHeight() - 35 || planePosition.y + 73 > 480 - terrainBelow.getRegionHeight()) {
			if (gameState != GameState.GAME_OVER) {
				gameState = GameState.GAME_OVER;
			}
		}
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
		batch.draw(plane.getKeyFrame(planeAnimTime), planePosition.x, planePosition.y);
		// Tap indicator.
		if (tapDrawTime > 0) {
			// 29.5 is half the width and height of the image.
			batch.draw(tapIndicator, touchPosition.x - 29.5f, touchPosition.y - 29.5f);
		}
		// Tap to begin indicator.
		if (gameState == GameState.INIT) {
			batch.draw(tap1, planePosition.x, planePosition.y - 80);
		}
		if (gameState == GameState.GAME_OVER) {
			batch.draw(gameOver, 400-206, 240-80);
		}

		batch.end();
	}

	public void resetScene() {
		terrainOffset = 0;
		planeAnimTime = 0;
		planeVelocity.set(200, 60);
		gravity.set(0, -2);
		// Plane itself is 88x73.
		planeDefaultPosition.set(400-88/2, 240-73/2);
		planePosition.set(planeDefaultPosition.x, planeDefaultPosition.y);
		scrollVelocity.set(4, 0);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}