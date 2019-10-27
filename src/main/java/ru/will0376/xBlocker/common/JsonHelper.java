package ru.will0376.xBlocker.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.server.IO;

import java.util.concurrent.atomic.AtomicBoolean;

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
		try {
			if (containsItem(objectname, subname.split(":")[0] + ":" + subname.split(":")[1], Integer.parseInt(subname.split(":")[2])))
				jo.getAsJsonArray("nbts").forEach(e -> server.getAsJsonObject(objectname).getAsJsonObject(subname).getAsJsonArray("nbts").add(e));
			else
				server.getAsJsonObject(objectname).add(subname, jo);
		} catch (Exception ignore) {
		}

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

	public static boolean containsItem(String objectname, String itemname, int meta) {
		JsonObject jo = (JsonObject) server.getAsJsonObject(objectname).get(itemname + ":0");
		if (jo != null && jo.has("boolBlockAllMeta"))
			return true;
		return server.getAsJsonObject(objectname).get(itemname + ":" + meta) != null;
	}

	public static boolean containsItem(String objectname, ItemStack is) {
		return containsItem(objectname, is.getItem().getRegistryName().toString(), is.getMetadata());
	}

	public static boolean checkAllMetas(String objectname, ItemStack is) {
		JsonObject jo = (JsonObject) server.getAsJsonObject(objectname).get(is.getItem().getRegistryName().toString() + ":0");
		return jo != null && jo.has("boolBlockAllMeta");
	}

	public static boolean containsEnchant(ItemStack is) {
		NBTTagList nbts = (NBTTagList) is.getTagCompound().getTag("StoredEnchantments");
		if (nbts != null) {
			for (NBTBase tgs : nbts) {
				NBTTagCompound tmp = (NBTTagCompound) tgs;
				int id = tmp.getShort("id");
				int lvl = tmp.getShort("lvl");
				return containsItem(ENCHANT, String.valueOf(id), lvl);
			}
		}
		return false;
	}

	public static boolean checkNBT(String objectname, ItemStack is) {
		JsonObject jo;
		if (checkAllMetas(objectname, is))
			jo = (JsonObject) server.getAsJsonObject(objectname).get(is.getItem().getRegistryName().toString() + ":" + 0);
		else
			jo = (JsonObject) server.getAsJsonObject(objectname).get(is.getItem().getRegistryName().toString() + ":" + is.getMetadata());
		if (!jo.has("nbts")) return false;
		AtomicBoolean ab = new AtomicBoolean(false);
		JsonArray jsonArray = jo.getAsJsonArray("nbts");
		jsonArray.forEach(nbt -> {
			is.getTagCompound().getKeySet().forEach(s -> {
				if (!ab.get())
					ab.set(nbt.toString().replace("\"", "").contains(is.getTagCompound().getString(s)));
			});
		});
		return ab.get();
	}
}
