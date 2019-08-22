package ru.will0376.xBlocker.server;

import com.google.gson.JsonObject;
import ru.will0376.xBlocker.Main;

import java.io.File;

public class IO {
	public static File path;
	public void write(JsonObject jo) {
		if(path == null){
			Main.Logger.error("Path to cfg folder = null");
			return;
		}
	}

	public JsonObject read() {
		if(path == null){
			Main.Logger.error("Path to cfg folder = null");
			return null;
		}
		return null;
	}
}
