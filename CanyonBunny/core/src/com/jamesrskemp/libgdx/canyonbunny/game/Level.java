package com.jamesrskemp.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.jamesrskemp.libgdx.canyonbunny.game.objects.AbstractGameObject;
import com.jamesrskemp.libgdx.canyonbunny.game.objects.Clouds;
import com.jamesrskemp.libgdx.canyonbunny.game.objects.Mountains;
import com.jamesrskemp.libgdx.canyonbunny.game.objects.Rock;
import com.jamesrskemp.libgdx.canyonbunny.game.objects.WaterOverlay;

/**
 * Created by James on 3/11/2015.
 */
public class Level {
	public static final String TAG = Level.class.getName();

	public enum BLOCK_TYPE {
		// Black
		EMPTY(0, 0, 0),
		// Green
		ROCK(0, 255, 0),
		// White
		PLAYER_SPAWNPOINT(255, 255, 255),
		// Purple
		ITEM_FEATHER(255, 0, 255),
		// Yellow
		ITEM_GOLD_COIN(255, 255, 0);

		private int color;

		private BLOCK_TYPE(int r, int g, int b) {
			// Expect full opacity for game objects.
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}

		public boolean sameColor(int color) {
			return this.color == color;
		}

		public int getColor() {
			return color;
		}
	}

	// Objects
	public Array<Rock> rocks;
	// Decorations
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;

	public Level(String filename) {
		init(filename);
	}

	private void init(String filename) {
		// Objects
		rocks = new Array<Rock>();

		// Load level data.
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		// Scan pixels from top-left to bottom-right.
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				// Height grows from bottom to top.
				float baseHeight = pixmap.getHeight() - pixelY;
				// Get color of current pixel as 32-bit RGBA value.
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				// Find and create the block, if found.
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					// Do nothing.
				} else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
					if (lastPixel != currentPixel) {
						obj = new Rock();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
						rocks.add((Rock)obj);
					} else {
						rocks.get(rocks.size - 1).increaseLength(1);
					}
				} else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {

				} else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {

				} else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {

				} else {
					// Red color channel.
					int r = 0xff & (currentPixel >>> 24);
					// Green color channel.
					int g = 0xff & (currentPixel >>> 16);
					// Blue color channel.
					int b = 0xff & (currentPixel >>> 8);
					// Alpha channel.
					int a = 0xff & currentPixel;
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY
							+ ">: r<" + r + "> g<" + g + "> b<" + b + "> a" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}

		// Decorations
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);

		// Free memory.
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

	public void render(SpriteBatch batch) {
		// Draw mountains.
		mountains.render(batch);
		// Draw rocks.
		for (Rock rock : rocks) {
			rock.render(batch);
		}
		// Draw water.
		waterOverlay.render(batch);
		// Draw clouds.
		clouds.render(batch);
	}
}
