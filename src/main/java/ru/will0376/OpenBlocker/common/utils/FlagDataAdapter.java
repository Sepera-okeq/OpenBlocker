package ru.will0376.OpenBlocker.common.utils;

import com.google.gson.*;

import java.lang.reflect.Type;

public class FlagDataAdapter implements JsonDeserializer<FlagData<?>>, JsonSerializer<FlagData<?>> {
	private static final Gson gson = new Gson();

	@Override
	public FlagData<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return gson.fromJson(json, FlagData.class);
	}

	@Override
	public JsonElement serialize(FlagData src, Type typeOfSrc, JsonSerializationContext context) {
		return gson.toJsonTree(src, FlagData.class);
	}
}
