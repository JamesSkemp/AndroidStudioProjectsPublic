package com.jamesrskemp.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.jamesrskemp.libgdx.canyonbunny.game.Assets;

/**
 * Created by James on 3/11/2015.
 */
public class Mountains extends AbstractGameObject {
	private TextureRegion regMountainLeft;
	private TextureRegion regMountainRight;

	private int length;

	public Mountains(int length) {
		this.length = length;
		init();
	}

	private void init() {
		dimension.set(10, 2);

		regMountainLeft = Assets.instance.levelDecoration.mountainLeft;
		regMountainRight = Assets.instance.levelDecoration.mountainRight;

		// Shift mountain and extend length.
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
	}

	private void drawMountain(SpriteBatch batch, float offsetX, float offsetY, float tintColor) {
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;

		// Mountains span the entire level.
		int mountainLength = 0;
		mountainLength += MathUtils.ceil(length / (2 * dimension.x));
		mountainLength += MathUtils.ceil(0.5f + offsetX);
		for (int i = 0; i < mountainLength; i++) {
			// Left mountain.
			reg = regMountainLeft;
			batch.draw(reg.getTexture(),
					origin.x + xRel, position.y + origin.y + yRel,
					origin.x, origin.y,
					dimension.x, dimension.y,
					scale.x, scale.y,
					rotation,
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
			xRel += dimension.x;

			// Right mountain.
			reg = regMountainRight;
			batch.draw(reg.getTexture(),
					origin.x + xRel, position.y + origin.y + yRel,
					origin.x, origin.y,
					dimension.x, dimension.y,
					scale.x, scale.y,
					rotation,
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
			xRel += dimension.x;
		}
		// Reset color to white.
		batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void render(SpriteBatch batch) {
		// Dark gray mountains.
		drawMountain(batch, 0.5f, 0.5f, 0.5f);
		// Gray mountains.
		drawMountain(batch, 0.25f, 0.25f, 0.7f);
		// Light gray mountains.
		drawMountain(batch, 0f, 0f, 0.9f);
	}
}
