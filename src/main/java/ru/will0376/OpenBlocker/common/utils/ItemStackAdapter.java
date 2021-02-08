package ru.will0376.OpenBlocker.common.utils;

import com.google.gson.*;
import lombok.extern.log4j.Log4j2;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;

@Log4j2
public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject asJsonObject = json.getAsJsonObject();
		Item item = Item.getByNameOrId(asJsonObject.get("itemName").getAsString());
		if (item == Items.AIR) log.error("Item == air");
		return new ItemStack(item, 1, asJsonObject.get("metaData").getAsInt());
	}

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("itemName", src.getItem().getRegistryName().toString());
		jsonObject.addProperty("metaData", src.getMetadata());
		return jsonObject;
	}
}
