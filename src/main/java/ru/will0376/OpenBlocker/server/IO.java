package ru.will0376.OpenBlocker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.Blocked;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@GradleSideOnly(GradleSide.SERVER)
@Log4j2
public class IO {
	public static File path;
	public static File fileJson;
	public static Type type = new TypeToken<List<Blocked>>() {
	}.getType();

	public static void write(List<Blocked> blockedList) {
		try {
			if (path == null || fileJson == null) {
				log.error("Path to cfg folder = null");
				return;
			}
			if (!checkFile()) {
				log.error("IO error!");
				return;
			}

			try (Writer writer = new FileWriter(fileJson)) {
				Main.gson.toJson(blockedList, writer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Blocked> read() {
		try {
			if (path == null || fileJson == null) {
				log.error("Path to cfg folder = null");
				return null;
			}
			if (!checkFile()) {
				log.error("IO error!");
				return null;
			}
			if (fileEmpty()) {
				writeNewJson();
				return new ArrayList<>();
			}
			List<Blocked> blockeds = Main.gson.fromJson(new FileReader(fileJson), type);
			return blockeds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void writeNewJson() throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = new FileWriter(fileJson);
		gson.toJson(new JsonObject(), fw);
		fw.flush();
		fw.close();
		log.info("Created new file: {}", fileJson.getAbsolutePath());
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
}
