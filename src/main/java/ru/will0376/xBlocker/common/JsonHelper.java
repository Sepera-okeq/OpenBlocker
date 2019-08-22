package ru.will0376.xBlocker.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonHelper {
	public static JsonObject client;
	public static JsonObject server;
	public static void add(JsonObject jo,String arrayname,boolean isClient){
		if(!isClient)
			server.getAsJsonArray(arrayname).add(jo);
		else
			client.getAsJsonArray(arrayname).add(jo);
	}
	public static void remove(String arrayname,String blockname,boolean isClient){
		if(!isClient){
			for(JsonElement jo : server.getAsJsonArray(arrayname)){
			}
		}
	}
}
