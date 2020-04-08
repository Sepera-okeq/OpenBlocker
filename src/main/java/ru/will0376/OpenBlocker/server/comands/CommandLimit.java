package ru.will0376.OpenBlocker.server.comands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.HashMap;

@GradleSideOnly(GradleSide.SERVER)
public class CommandLimit implements Base {
	String addusage = Base.usage + "limit <args>\n" +
			"   Arguments:\n" +
			"   -allmeta(bool)\n" +
			"   -l(int)\n\n" +
			"   e.x: /ob limit l:5; allmeta\n" +
			"   (delimiter ';')";
	String removeusage = Base.usage + "removelimit";

	public void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(addusage + "\n" + removeusage));
	}

	/**
	 * argumets: allmeta,l
	 */
	public void add(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		if (is.isEmpty()) {
			player.sendMessage(new TextComponentString("Hand is empty!"));
			return;
		}
		int meta = is.getMetadata();
		HashMap<String, String> parsed = new CommandParser().commandParser(ComandsMain.stringArrToString(args).replace("limit ", ""));
		String limit = parsed.getOrDefault("l", "-1");
		boolean allmeta = Boolean.parseBoolean(parsed.getOrDefault("allmeta", "false"));

		JsonObject jo = new JsonObject();
		if (allmeta) {
			jo.addProperty("boolBlockAllMeta", true);
			meta = 0;
		}
		if (limit.equals("-1")) {
			sender.sendMessage(new TextComponentString("Hey, you forgot to enter a limit!"));
			return;
		}
		jo.addProperty("limit", limit);

		if (is.getTagCompound() != null && !is.getTagCompound().isEmpty()) {
			NBTTagCompound nbtTagCompound = is.getTagCompound();
			JsonArray ja = new JsonArray();
			for (String tgs : nbtTagCompound.getKeySet()) {
				ja.add(nbtTagCompound.getTag(tgs).toString().replace("\"", ""));
			}
			jo.add("nbts", ja);
		}

		JsonHelper.addServer(jo, JsonHelper.LIMIT, is.getItem().getRegistryName().toString() + ":" + meta);
		player.sendMessage(new TextComponentString(ChatForm.prefix + String.format("The limit for %s block is set to %s", is.getItem().getRegistryName().toString() + ":" + meta, args[1])));
	}

	public void remove(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		if (is.isEmpty()) {
			player.sendMessage(new TextComponentString("Hand is empty!"));
			return;
		}
		int meta = is.getMetadata();
		JsonHelper.removeFromServer(JsonHelper.LIMIT, is.getItem().getRegistryName().toString() + ":" + meta);
		player.sendMessage(new TextComponentString(ChatForm.prefix + String.format("The limit for %s block removed", is.getItem().getRegistryName().toString() + ":" + meta)));
	}

	private boolean contains(String[] args, String text) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase(text))
				return true;
		}
		return false;
	}
}
