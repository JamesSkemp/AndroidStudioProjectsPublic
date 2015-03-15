package com.jamesrskemp.libgdx.canyonbunny.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by James on 3/14/2015.
 */
public enum CharacterSkin {
	WHITE("White", 1, 1, 1),
	GRAY("Gray", 0.7f, 0.7f, 0.7f),
	BROWN("Brown", 0.7f, 0.5f, 0.3f);

	private String name;
	private Color color = new Color();

	private CharacterSkin(String name, float r, float g, float b) {
		this.name = name;
		color.set(r, g, b, 1);
	}

	@Override
	public String toString() {
		return name;
	}

	public Color getColor() {
		return color;
	}
}
