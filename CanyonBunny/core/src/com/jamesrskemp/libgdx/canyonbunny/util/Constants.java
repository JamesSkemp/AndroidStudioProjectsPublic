package com.jamesrskemp.libgdx.canyonbunny.util;

/**
 * Created by James on 3/10/2015.
 */
public class Constants {
	/**
	 * Visible game world is 5 meters wide.
	 */
	public static final float VIEWPORT_WIDTH = 5.0f;

	/**
	 * Visible game world is 5 meters tall.
	 */
	public static final float VIEWPORT_HEIGHT = 5.0f;

	/**
	 * User interface width.
	 */
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	/**
	 * User interface height.
	 */
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

	/**
	 * Location of texture atlas description file for the game.
	 */
	public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack";

	/**
	 * Used to create the main menu screen.
	 */
	public static final String SKIN_CANYONBUNNY_UI = "ui/canyonbunny-ui.json";
	/**
	 * Used to create the main menu screen.
	 */
	public static final String TEXTURE_ATLAS_UI = "ui/canyonbunny-ui.pack";

	/**
	 * Standard ligGDX example skin.
	 */
	public static final String SKIN_LIBGDX_UI = "ui/uiskin.json";
	/**
	 * Standard ligGDX example skin texture atlas.
	 */
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "ui/uiskin.atlas";

	/**
	 * Name of the preferences file.
	 */
	public static final String PREFERENCES = "canyonbunny.prefs";

	/**
	 * Location of the first level map.
	 */
	public static final String LEVEL_01 = "levels/level-01.png";

	/**
	 * Number of lives at the start of the game.
	 */
	public static final int LIVES_START = 3;

	/**
	 * Duration of feather power-up in seconds.
	 */
	public static final float ITEM_FEATHER_POWERUP_DURATION = 9;

	/**
	 * Delay until game over message.
	 */
	public static final float TIME_DELAY_GAME_OVER = 3;
}
