package ru.will0376.OpenBlocker.common.utils;

import com.google.gson.*;

import java.lang.reflect.Type;

public class FlagDataAdapter implements JsonDeserializer<FlagData>, JsonSerializer<FlagData> {

	@Override
	public FlagData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject asJsonObject = json.getAsJsonObject();
		JsonElement flag = asJsonObject.get("flag");
		JsonElement data = asJsonObject.get("data");
		try {
			return FlagData.Flag.createNewByFlag(FlagData.Flag.valueOf(flag.getAsString()), data.getAsString());
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JsonElement serialize(FlagData src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jo = new JsonObject();
		jo.addProperty("flag", src.getFlag().name());
		jo.addProperty("data", String.valueOf(src.getData()));
		return jo;
	}
}
