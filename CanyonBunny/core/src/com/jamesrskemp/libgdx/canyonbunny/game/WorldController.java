package com.jamesrskemp.libgdx.canyonbunny.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.jamesrskemp.libgdx.canyonbunny.game.objects.BunnyHead;
import com.jamesrskemp.libgdx.canyonbunny.game.objects.Feather;
import com.jamesrskemp.libgdx.canyonbunny.game.objects.GoldCoin;
import com.jamesrskemp.libgdx.canyonbunny.game.objects.Rock;
import com.jamesrskemp.libgdx.canyonbunny.util.CameraHelper;
import com.jamesrskemp.libgdx.canyonbunny.util.Constants;

/**
 * Created by James on 3/10/2015.
 */
public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();

	public CameraHelper cameraHelper;

	public Level level;
	public int lives;
	public int score;

	// Rectangles for collision detection.
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();

	public WorldController() {
		init();
	}

	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		initLevel();
	}

	private void initLevel() {
		score = 0;
		level = new Level(Constants.LEVEL_01);
	}

	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
	}

	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
			return;
		}

		// Camera moving controls.
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			camMoveSpeed *= camMoveSpeedAccelerationFactor;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			moveCamera(-camMoveSpeed, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			moveCamera(camMoveSpeed, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			moveCamera(0, camMoveSpeed);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			moveCamera(0, -camMoveSpeed);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
			cameraHelper.setPosition(0, 0);
		}

		// Camera zoom controls.
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
			cameraHelper.addZoom(camZoomSpeed);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
			cameraHelper.addZoom(-camZoomSpeed);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) {
			cameraHelper.setZoom(1);
		}
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.R) {
			// Reset game world.
			init();
			Gdx.app.debug(TAG, "Game world reset");
		}
		return false;
	}

	private void testCollisions() {
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,
				level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

		for (Rock rock : level.rocks) {
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if (!r1.overlaps(r2)) {
				continue;
			}
			onCollisionBunnyHeadWithRock(rock);
			// Don't break, as we need to test all rocks.
		}

		for (GoldCoin goldCoin : level.goldCoins) {
			if (goldCoin.collected) {
				continue;
			}
			r2.set(goldCoin.position.x, goldCoin.position.y, goldCoin.bounds.width, goldCoin.bounds.height);
			if (!r1.overlaps(r2)) {
				continue;
			}
			onCollisionBunnyWithGoldCoin(goldCoin);
			break;
		}

		for (Feather feather : level.feathers) {
			if (feather.collected) {
				continue;
			}
			r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2)) {
				continue;
			}
			onCollisionBunnyWithFeather(feather);
			break;
		}
	}

	private void onCollisionBunnyHeadWithRock(Rock rock) {
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f) {
			boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2);
			if (hitRightEdge) {
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
			}
			return;
		}

		switch (bunnyHead.jumpState) {
			case GROUNDED:
				break;
			case FALLING:
			case JUMP_FALLING:
				bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
				bunnyHead.jumpState = BunnyHead.JUMP_STATE.GROUNDED;
				break;
			case JUMP_RISING:
				bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
				break;
		}
	}

	private void onCollisionBunnyWithGoldCoin(GoldCoin goldCoin) {
		goldCoin.collected = true;
		score += goldCoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected.");
	}

	private void onCollisionBunnyWithFeather(Feather feather) {
		feather.collected = true;
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected.");
	}
}
