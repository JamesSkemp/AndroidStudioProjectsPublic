package com.jamesrskemp.firstlibgdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;

/**
 * Created by James on 3/5/2015.
 */
public class BaseScene extends ScreenAdapter {
	protected FirstLibGdxGame game;
	private boolean keyHandled;

	public BaseScene(FirstLibGdxGame firstLibGdxGame) {
		game = firstLibGdxGame;
		keyHandled = false;
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			if (keyHandled) {
				return;
			}
			handleBackPress();
			keyHandled = true;
		} else {
			keyHandled = false;
		}
	}

	protected void handleBackPress() {
	}
}
