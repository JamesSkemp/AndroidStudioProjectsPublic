package com.jamesrskemp.xmltesting;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 11/24/2014.
 */
public class GamesJsonParser {
	public List readJsonStream(InputStream in) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			return readGamesArray(reader);
		} finally {
			reader.close();
		}
	}

	public List readGamesArray(JsonReader reader) throws IOException {
		List games = new ArrayList();

		reader.beginObject();
		reader.nextName();
		reader.beginArray();
		while(reader.hasNext()) {
			games.add(readGame(reader));
		}
		reader.endArray();
		reader.endObject();
		return games;
	}

	public VideoGame readGame(JsonReader reader) throws IOException {
		VideoGame game = new VideoGame();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("Id")) {
				game.id = Long.parseLong(reader.nextString());
			} else if (name.equals("Title")) {
				game.title = reader.nextString();
			} else if (name.equals("System")) {
				game.system = reader.nextString();
			} else if (name.equals("Notes")) {
				game.notes = reader.nextString();
			} else if (name.equals("Own")) {
				game.own = reader.nextString();
			} else if (name.equals("Date")) {
				game.purchaseDate = reader.nextString();
			} else if (name.equals("Price")) {
				game.purchasePrice = reader.nextString();
			} else if (name.equals("Place")) {
				game.purchasePlace = reader.nextString();
			} else if (name.equals("SellDate")) {
				game.sellDate = reader.nextString();
			} else if (name.equals("SellPrice")) {
				game.sellPrice = reader.nextString();
			} else if (name.equals("SellPlace")) {
				game.sellPlace = reader.nextString();
			} else if (name.equals("AddOn")) {
				game.addOn = reader.nextString();
			} else if (name.equals("Electronic")) {
				game.electronic = reader.nextString();
			} else if (name.equals("Beat")) {
				game.beat = reader.nextBoolean();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return game;
	}
}

