package com.jamesrskemp.firstlibgdxgame;

import com.badlogic.gdx.Game;

public class FirstLibGdxGame extends Game {
	public FirstLibGdxGame() {
	}

	@Override
	public void create () {
		setScreen(new ThrustCopterScene());
	}

	@Override
	public void render() {
		super.render();
	}
}
