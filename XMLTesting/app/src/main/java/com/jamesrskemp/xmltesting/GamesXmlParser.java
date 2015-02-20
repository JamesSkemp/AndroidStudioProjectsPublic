package com.jamesrskemp.xmltesting;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

/**
 * Created by James on 11/23/2014.
 */
public class GamesXmlParser {
	private static final String ns = null;

	public static class Game {
		public String title;
		public Long id;
		public Boolean addOn;
		public Boolean electronic;
		public Boolean beat;
		public Boolean used;
		public String systemConsole;
		public String systemVersion;
		public String purchaseDate;
		public String purchasePrice;
		public String purchasePlace;
		public String sellDate;
		public String sellPrice;
		public Boolean own;
		public String notes;

		public Game() {
		}
	}

	public List<Game> parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private List<Game> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<Game> entries = new ArrayList<Game>();

		parser.require(XmlPullParser.START_TAG, ns, "feed");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("entry")) {
				entries.add(readEntry(parser));
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
	// off
	// to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
	private Game readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "entry");
		Game game = new Game();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("title")) {
				game.title = readStringElement(parser, "title");
			/*} else if (name.equals("id")) {
				game.id = Long.parseLong(readStringElement(parser, "id"));*/
			}else if (name.equals("console")) {
				game.systemConsole = readStringElement(parser, "console");
			} else if (name.equals("version")) {
				game.systemVersion = readStringElement(parser, "version");
			} else if (name.equals("own")) {
				game.own = Boolean.parseBoolean(readStringElement(parser, "own"));
			} else if (name.equals("notes")) {
				game.own = Boolean.parseBoolean(readStringElement(parser, "noes"));
			} else {
				skip(parser);
			}
			/*
			game.title;
Long id;
Boolean addOn;
Boolean electronic;
Boolean beat;
Boolean used;
game.systemConsole;
game.systemVersion;
Date purchaseDate;
Currency purchasePrice;
game.purchasePlace;
Date sellDate;
Currency sellPrice;
Boolean own;
game.notes;
			 */
		}
		return game;
	}









	// Processes summary tags in the feed.
	private String readStringElement(XmlPullParser parser, String elementName) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, elementName);
		String elementValue = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, elementName);
		return elementValue;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	// Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
	// if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
	// finds the matching END_TAG (as indicated by the value of "depth" being 0).
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}
}
