package com.jamesrskemp.xmltesting;

import java.util.Comparator;

/**
 * Created by James on 11/24/2014.
 */
public class VideoGameComparer implements Comparator<VideoGame> {
	@Override
	public int compare(VideoGame x, VideoGame y) {
		int value = x.title.compareTo(y.title);
		if (value == 0) {
			value = x.system.compareTo(y.system);
		}
		return value;
	}
}
