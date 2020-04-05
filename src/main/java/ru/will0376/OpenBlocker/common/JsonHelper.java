package ru.will0376.OpenBlocker.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.server.IO;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonHelper {
	public static final String BLOCKER = "blocker";
	public static final String LIMIT = "limit";
	public static final String MINCOST = "mincost";
	public static final String ENCHANT = "enchant";
	public static final String CRAFT = "craft";
	public static JsonObject client = new JsonObject();
	public static JsonObject server = new JsonObject();
	public static boolean isServer = Main.server;

	public static void addClient(JsonObject jo, String objectname, String subname) {
		client.getAsJsonObject(objectname).add(subname, jo);
	}

	public static JsonObject getClient(String objectname, String name) {
		return client.getAsJsonObject(objectname).getAsJsonObject(name);
	}

	public static JsonObject getServer(String objectname, String name) {
		return server.getAsJsonObject(objectname).getAsJsonObject(name);
	}

	public static void addServer(JsonObject jo, String objectname, String subname) {
		try {
			if (containsItemServer(objectname, subname.split(":")[0] + ":" + subname.split(":")[1], Integer.parseInt(subname.split(":")[2])))
				jo.getAsJsonArray("nbts").forEach(e -> server.getAsJsonObject(objectname).getAsJsonObject(subname).getAsJsonArray("nbts").add(e));
			else
				server.getAsJsonObject(objectname).add(subname, jo);
		} catch (Exception ignore) {
		}

		IO.write(server);
		resendToClient();
	}

	public static void removeFromServer(String objectname, String blockname) {
		server.getAsJsonObject(objectname).remove(blockname);
		IO.write(server);
		resendToClient();
	}

	public static boolean contains(String objectname, String name) {
		try {
			if (isServer) return server.getAsJsonObject(objectname).get(name) != null;
			else return client.getAsJsonObject(objectname).get(name) != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		//if (client.getAsJsonObject(objectname).get(name) != null) return true;
		//return server.getAsJsonObject(objectname).get(name) != null;
	}

	public static void resendToClient() {
		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(JsonHelper::sendToPlayer);
	}

	public static void sendToPlayer(EntityPlayerMP p) {
		Main.network.sendTo(new Blocker(server.toString()), p);
	}

	public static void init() {
		JsonObject jo = IO.read();
		if (jo != null)
			server = jo;
		else
			Main.Logger.error(ChatForm.prefix_error + "[JsonHelper_Init] FileReader Error!");
	}

	public static boolean containsItemServer(String objectname, String itemname, int meta) {
		JsonObject jo = (JsonObject) server.getAsJsonObject(objectname).get(itemname + ":0");
		if (jo != null && jo.has("boolBlockAllMeta"))
			return true;
		return server.getAsJsonObject(objectname).get(itemname + ":" + meta) != null;
	}

	public static boolean containsItemClient(String objectname, String itemname, int meta) {
		JsonElement je = client.getAsJsonObject(objectname).get(itemname + ":0");
		if (je == null) {
			System.err.println("[JsonHelper_Init] Hey, JsonElement == null =\\");
			return false;
		}
		JsonObject jo = (JsonObject) je;
		if (jo != null && jo.has("boolBlockAllMeta"))
			return true;
		return client.getAsJsonObject(objectname).get(itemname + ":" + meta) != null;

	}

	public static boolean containsItemServer(String objectname, ItemStack is) {
		return containsItemServer(objectname, is.getItem().getRegistryName().toString(), is.getMetadata());
	}

	public static ArrayList<String> findAllNBT(String name, int meta) {
		ArrayList<String> list = new ArrayList<>();
		finditem(name, meta).forEach(l -> {
			if (l.has("nbts"))
				l.get("nbts").getAsJsonArray().forEach(st -> list.add(st.getAsString()));
		});
		return list;
	}

	public static ArrayList<JsonObject> finditem(String name, int meta) {
		JsonObject jotemp;
		if (!isServer) jotemp = client;
		else jotemp = server;
		ArrayList<JsonObject> jolist = new ArrayList<>();
		AtomicInteger metaai = new AtomicInteger(meta);
		jotemp.entrySet().forEach(l -> {
			if (checkAllMetas(l.getKey(), name)) metaai.set(0);
			if (contains(l.getKey(), name + ":" + metaai.get())) {
				jolist.add(l.getValue().getAsJsonObject().getAsJsonObject(name + ":" + metaai.get()));
			}
		});
		return jolist;
	}

	public static boolean checkAllMetas(String objectname, String is) {
		if (isServer) {
			JsonObject jo = (JsonObject) server.getAsJsonObject(objectname).get(is + ":0");
			return jo != null && jo.has("boolBlockAllMeta");
		} else {
			JsonObject jo = (JsonObject) client.getAsJsonObject(objectname).get(is + ":0");
			return jo != null && jo.has("boolBlockAllMeta");
		}
	}

	public static boolean checkAllMetas(String objectname, ItemStack is) {
		return checkAllMetas(objectname, is.getItem().getRegistryName().toString());
	}

	public static boolean containsEnchantServer(ItemStack is) {
		NBTTagList nbts = (NBTTagList) is.getTagCompound().getTag("StoredEnchantments");
		if (nbts != null) {
			for (NBTBase tgs : nbts) {
				NBTTagCompound tmp = (NBTTagCompound) tgs;
				int id = tmp.getShort("id");
				int lvl = tmp.getShort("lvl");
				return containsItemServer(ENCHANT, String.valueOf(id), lvl);
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
			if (is.getTagCompound() != null)
				is.getTagCompound().getKeySet().forEach(s -> {
					if (!ab.get()) {
						NBTTagCompound nbtt = new NBTTagCompound();
						try {
							nbtt = JsonToNBT.getTagFromJson(B64.decode(nbt.getAsString()));
						} catch (NBTException e) {
							e.printStackTrace();
						}
						if (nbtt.hasKey("tag") && nbtt.getCompoundTag("tag").hasKey(s)) {
							if (is.serializeNBT().hasKey("tag") && is.serializeNBT().getCompoundTag("tag").hasKey(s) &&
									is.serializeNBT().getCompoundTag("tag").getCompoundTag(s).equals(nbtt.getCompoundTag("tag").getCompoundTag(s)))
								ab.set(true);
						}
					}
				});
		});
		return ab.get();
	}
}
