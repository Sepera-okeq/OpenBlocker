package ru.will0376.OpenBlocker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;

import java.io.*;

@GradleSideOnly(GradleSide.SERVER)
public class IO {
	public static File path;
	public static File fileJson;

	public static void write(JsonObject jo) {
		try {
			if (path == null || fileJson == null) {
				Main.Logger.error("Path to cfg folder = null");
				return;
			}
			if (!checkFile()) {
				Main.Logger.error("IO error!");
				return;
			}
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter fw = new FileWriter(fileJson);
			gson.toJson(jo, fw);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JsonObject read() {
		try {
			if (path == null || fileJson == null) {
				Main.Logger.error("Path to cfg folder = null");
				return null;
			}
			if (!checkFile()) {
				Main.Logger.error("IO error!");
				return null;
			}
			if (fileEmpty() || !fileIsJson()) {
				writeNewJson();
			}

			JsonParser jp = new JsonParser();
			return jp.parse(new JsonReader(new FileReader(fileJson))).getAsJsonObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void writeNewJson() throws IOException {
		JsonObject jo = new JsonObject();
		jo.add("blocker", new JsonObject());
		jo.add("limit", new JsonObject());
		jo.add("mincost", new JsonObject());
		jo.add("enchant", new JsonObject());
		jo.add("craft", new JsonObject());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = new FileWriter(fileJson);
		gson.toJson(jo, fw);
		fw.flush();
		fw.close();
	}

	private static boolean checkFile() {
		try {
			if (!path.exists())
				path.mkdir();
			if (!fileJson.exists())
				fileJson.createNewFile();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean fileEmpty() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileJson));
			return br.readLine() == null;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	private static boolean fileIsJson() {
		try {
			JsonParser jp = new JsonParser();
			jp.parse(new JsonReader(new FileReader(fileJson))).getAsJsonObject();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
