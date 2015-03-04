package com.jamesrskemp.firstlibgdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by James on 3/3/2015.
 */
public class ThrustCopterScene extends ScreenAdapter {
	SpriteBatch batch;
	FPSLogger fpsLogger;
	OrthographicCamera camera;
	Viewport viewport;
	TextureRegion background;
	TextureRegion terrainBelow;
	TextureRegion terrainAbove;
	TextureRegion pillarUp;
	TextureRegion pillarDown;
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
	Array<Vector2> pillars = new Array<Vector2>();
	Vector2 lastPillarPosition = new Vector2();
	float deltaPosition;
	Rectangle planeRect = new Rectangle();
	Rectangle obstacleRect = new Rectangle();
	// Meteors
	Array<TextureAtlas.AtlasRegion> meteorTextures = new Array<TextureAtlas.AtlasRegion>();
	TextureRegion selectedMeteorTexture;
	boolean meteorInScene;
	private static final int METEOR_SPEED = 60;
	Vector2 meteorPosition = new Vector2();
	Vector2 meteorVelocity = new Vector2();
	float nextMeteorIn;

	Music music;
	Sound tapSound;
	Sound crashSound;
	Sound spawnSound;

	static enum GameState {
		/**
		 * Initialize
		 */
		INIT,
		ACTION,
		GAME_OVER
	}

	public ThrustCopterScene() {
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
		pillarUp = atlas.findRegion("rockGrassUp");
		pillarDown = atlas.findRegion("rockGrassDown");
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
		// Meteors
		meteorTextures.add(atlas.findRegion("meteorBrown_med1"));
		meteorTextures.add(atlas.findRegion("meteorBrown_med2"));
		meteorTextures.add(atlas.findRegion("meteorBrown_small1"));
		meteorTextures.add(atlas.findRegion("meteorBrown_small2"));
		meteorTextures.add(atlas.findRegion("meteorBrown_tiny1"));
		meteorTextures.add(atlas.findRegion("meteorBrown_tiny2"));

		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/journey.mp3"));
		music.setLooping(true);
		music.play();

		tapSound = Gdx.audio.newSound(Gdx.files.internal("sounds/pop.ogg"));
		crashSound = Gdx.audio.newSound(Gdx.files.internal("sounds/crash.ogg"));
		spawnSound = Gdx.audio.newSound(Gdx.files.internal("sounds/alarm.ogg"));

		resetScene();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		fpsLogger.log();
		updateScene();
		drawScene();
	}

	@Override
	public void dispose() {
		batch.dispose();
		tapSound.dispose();
		crashSound.dispose();
		spawnSound.dispose();
		music.dispose();
		pillars.clear();
		meteorTextures.clear();
		atlas.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	private void resetScene() {
		terrainOffset = 0;
		planeAnimTime = 0;
		planeVelocity.set(200, 60);
		gravity.set(0, -2);
		// Plane itself is 88x73.
		planeDefaultPosition.set(400-88/2, 240-73/2);
		planePosition.set(planeDefaultPosition.x, planeDefaultPosition.y);
		scrollVelocity.set(4, 0);
		pillars.clear();
		lastPillarPosition = new Vector2();

		meteorInScene = false;
		nextMeteorIn = (float)Math.random() * 5;
	}

	private void drawScene() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// The background is not on top of anything else, so disable blending temporarily.
		batch.disableBlending();
		batch.draw(background, 0, 0);
		// Re-enable blending as our next items will have some transparency.
		batch.enableBlending();
		// Draw our pillar(s).
		for (Vector2 vec : pillars) {
			if (vec.y == 1) {
				batch.draw(pillarUp, vec.x, 0);
			} else {
				batch.draw(pillarDown, vec.x, 480 - pillarDown.getRegionHeight());
			}
		}
		if (meteorInScene) {
			batch.draw(selectedMeteorTexture, meteorPosition.x, meteorPosition.y);
		}
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

	private void updateScene() {
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

			tapSound.play();

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

		//terrainOffset -= 200 * deltaTime;
		planeAnimTime += deltaTime;
		planeVelocity.scl(damping);
		planeVelocity.add(gravity);
		planeVelocity.add(scrollVelocity);

		planePosition.mulAdd(planeVelocity, deltaTime);

		terrainOffset -= planePosition.x - planeDefaultPosition.x;
		deltaPosition = planePosition.x - planeDefaultPosition.x;
		// Keep the plane in the same place on the x-axis.
		planePosition.x = planeDefaultPosition.x;
		if (terrainOffset * -1 > terrainBelow.getRegionWidth()) {
			terrainOffset = 0;
		}

		if (terrainOffset > 0) {
			terrainOffset = -terrainBelow.getRegionWidth();
		}

		planeRect.set(planePosition.x + 16, planePosition.y, 50, 73);
		for (Vector2 vec : pillars) {
			vec.x -= deltaPosition;
			if (vec.x + pillarUp.getRegionWidth() < -10) {
				pillars.removeValue(vec, false);
			}
			if (vec.y == 1) {
				obstacleRect.set(vec.x + 10, 0, pillarUp.getRegionWidth() - 20, pillarUp.getRegionHeight() - 10);
			} else {
				obstacleRect.set(vec.x + 10, 480 - pillarDown.getRegionHeight() + 10, pillarUp.getRegionWidth() - 20, pillarUp.getRegionHeight());
			}

			if (planeRect.overlaps(obstacleRect)) {
				if (gameState != GameState.GAME_OVER) {
					crashSound.play();
					gameState = GameState.GAME_OVER;
				}
			}
		}

		if (meteorInScene) {
			obstacleRect.set(meteorPosition.x + 2, meteorPosition.y + 2, selectedMeteorTexture.getRegionWidth() - 4, selectedMeteorTexture.getRegionHeight() - 4);

			if (planeRect.overlaps(obstacleRect)) {
				if (gameState != GameState.GAME_OVER) {
					crashSound.play();
					gameState = GameState.GAME_OVER;
				}
			}
		}

		if (lastPillarPosition.x < 400) {
			addPillar();
		}

		if (meteorInScene) {
			meteorPosition.mulAdd(meteorVelocity, deltaTime);
			meteorPosition.x -= deltaPosition;
			if (meteorPosition.x < -10) {
				meteorInScene = false;
			}
		}

		nextMeteorIn -= deltaTime;
		if (nextMeteorIn <= 0) {
			launchMeteor();
		}

		if (planePosition.y < terrainBelow.getRegionHeight() - 35 || planePosition.y + 73 > 480 - terrainBelow.getRegionHeight()) {
			if (gameState != GameState.GAME_OVER) {
				crashSound.play();
				gameState = GameState.GAME_OVER;
			}
		}

		tapDrawTime -= deltaTime;
	}

	private void addPillar() {
		Vector2 pillarPosition = new Vector2();
		if (pillars.size == 0) {
			pillarPosition.x = (float)(800 + Math.random() * 600);
		} else {
			pillarPosition.x = lastPillarPosition.x + (float)(600 + Math.random() * 600);
		}
		if (MathUtils.randomBoolean()) {
			pillarPosition.y = 1;
		} else {
			pillarPosition.y = -1;
		}
		lastPillarPosition = pillarPosition;
		pillars.add(pillarPosition);
	}

	private void launchMeteor() {
		nextMeteorIn = 1.5f + (float)Math.random() * 5;
		if (meteorInScene) {
			return;
		}
		meteorInScene = true;

		int id = (int)(Math.random() * meteorTextures.size);
		selectedMeteorTexture = meteorTextures.get(id);

		meteorPosition.x = 810;
		meteorPosition.y = (float)(80 + Math.random() * 320);

		Vector2 destination = new Vector2();
		destination.x = -10;
		destination.y = (float)(80 + Math.random() * 320);
		destination.sub(meteorPosition).nor();
		meteorVelocity.mulAdd(destination, METEOR_SPEED);
		spawnSound.play();
	}
}
