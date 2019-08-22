package ru.will0376.xBlocker.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.server.IO;

public class JsonHelper {
	public static JsonObject client;
	public static JsonObject server;

	public static void addClient(JsonObject jo,String objectname){
		client.getAsJsonArray(objectname).add(jo);
	}

	public static void addServer(JsonObject jo,String objectname){
			server.getAsJsonArray(objectname).add(jo);
			IO.write(server);
	}

	public static void removeServer(String objectname,String blockname){
			server.getAsJsonObject().getAsJsonObject(objectname).remove(blockname);
			IO.write(server);
	}

	public static void init(){
	JsonObject jo = IO.read();
		if(jo != null)
			server = jo;
		else
			Main.Logger.error("FileReader Error!");
	}
}
