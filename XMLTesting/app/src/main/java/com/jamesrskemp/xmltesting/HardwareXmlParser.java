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
public class HardwareXmlParser {
	private static final String ns = null;

	public static class Hardware{
		public String name;
		public Long id;
		public String systemConsole;
		public String systemVersion;
		public String purchaseDate;
		public String purchasePrice;
		public String purchasePlace;
		public String sellDate;
		public String sellPrice;
		public String sellPlace;
		public Boolean own;
		public String notes;

		public Hardware() {
		}
	}

	public List<Hardware> parse(InputStream in) throws XmlPullParserException, IOException {
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

	private List<Hardware> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<Hardware> entries = new ArrayList<Hardware>();

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
	private Hardware readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "entry");
		Hardware hardware = new Hardware();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("name")) {
				hardware.name = readStringElement(parser, "name");
			} else if (name.equals("own")) {
				hardware.own = Boolean.parseBoolean(readStringElement(parser, "own"));
			} else if (name.equals("notes")) {
				hardware.notes = readStringElement(parser, "notes");
			} else {
				skip(parser);
			}
		}
		return hardware;
	}

/*
hardware.name;
Long id;
hardware.systemConsole;
hardware.systemVersion;
Date purchaseDate;
Currency purchasePrice;
hardware.purchasePlace;
Date sellDate;
Currency sellPrice;
hardware.sellPlace;
Boolean own;
hardware.notes;

 */



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
