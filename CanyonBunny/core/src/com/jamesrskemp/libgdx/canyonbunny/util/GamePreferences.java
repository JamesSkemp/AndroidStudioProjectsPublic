package com.jamesrskemp.libgdx.canyonbunny.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by James on 3/14/2015.
 */
public class GamePreferences {
	public static final String TAG = GamePreferences.class.getName();

	public static final GamePreferences instance = new GamePreferences();

	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;
	public int charSkin;
	public boolean showFpsCounter;

	private Preferences prefs;

	// Singleton.
	private GamePreferences() {
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	}

	public void load() {
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0, 1);
		volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0, 1);
		charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0), 0, 2);
		showFpsCounter = prefs.getBoolean("showFpsCounter", false);
	}

	public void save() {
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("volSound", volSound);
		prefs.putFloat("volMusic", volMusic);
		prefs.putInteger("charSkin", charSkin);
		prefs.putBoolean("showFpsCounter", showFpsCounter);
		// Actually save the preferences to the file.
		prefs.flush();
	}

}
