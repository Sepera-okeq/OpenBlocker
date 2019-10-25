package ru.will0376.xBlocker.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.server.IO;

public class JsonHelper {
	public static JsonObject client;
	public static JsonObject server;
	public static final String BLOCKER = "blocker";
	public static final String LIMIT = "limit";
	public static final String MINCOST = "mincost";
	public static final String ENCHANT = "enchant";
	public static final String CRAFT = "craft";


	public static void addClient(JsonObject jo,String objectname,String subname){
		client.getAsJsonObject(objectname).add(subname,jo);
	}

	public static void addServer(JsonObject jo,String objectname,String subname){
			server.getAsJsonObject(objectname).add(subname,jo);
			IO.write(server);
	}

	public static void removeFromServer(String objectname,String blockname){
			server.getAsJsonObject(objectname).remove(blockname);
			IO.write(server);
	}

	public static boolean contains(String objectname,String name) {
		return server.getAsJsonObject(objectname).get(name) != null;
	}
	public static void init(){
	JsonObject jo = IO.read();
		if(jo != null)
			server = jo;
		else
			Main.Logger.error("FileReader Error!");
	}
}
