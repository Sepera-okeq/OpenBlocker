package ru.will0376.OpenBlocker.server.comands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.CraftManager;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.HashMap;

@GradleSideOnly(GradleSide.SERVER)
public class CommandCraft {
	String usage = Base.usage + "craft <args>\n" +
			"   Arguments:\n" +
			"   -text:reason(multi-string)\n" +
			"   -allmeta(bool)\n\n" +
			"   e.x: /ob craft text:Test reason; allmeta\n" +
			"   (delimiter ';')";

	public static String[] getArgs() {
		return new String[]{"text", "allmeta"};
	}

	/**
	 * argumets: text,allmeta;
	 */
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		if (is.isEmpty()) {
			player.sendMessage(new TextComponentString("Take item/block into hand"));
			return;
		}
		int meta = is.getMetadata();

		HashMap<String, String> parsed = new CommandParser().commandParser(ComandsMain.stringArrToString(args).replace("craft ", ""));

		String text = parsed.getOrDefault("text", Main.config.getDefRes());
		boolean allmeta = Boolean.parseBoolean(parsed.getOrDefault("allmeta", "false"));
		if (allmeta) meta = 0;
		if (JsonHelper.contains(JsonHelper.CRAFT, is.getItem().getRegistryName().toString() + ":" + meta)) {
			CraftManager.removeCraftingRecipe(is);
			JsonHelper.removeFromServer(JsonHelper.CRAFT, is.getItem().getRegistryName().toString() + ":" + meta);
			player.sendMessage(new TextComponentString(ChatForm.prefix + "Item/Block available"));
		} else {
			JsonObject jo = new JsonObject();
			if (allmeta)
				jo.addProperty("boolBlockAllMeta", true);

			if (is.getTagCompound() != null && !is.getTagCompound().isEmpty()) {
				NBTTagCompound nbtTagCompound = is.getTagCompound();
				JsonArray ja = new JsonArray();
				for (String tgs : nbtTagCompound.getKeySet()) {
					ja.add(nbtTagCompound.getTag(tgs).toString().replace("\"", ""));
				}
				jo.add("nbts", ja);
			}

			jo.addProperty("reason", text);
			CraftManager.bringBack(is);
			JsonHelper.addServer(jo, JsonHelper.CRAFT, is.getItem().getRegistryName().toString() + ":" + meta);
			player.sendMessage(new TextComponentString(ChatForm.prefix + "Item/Block Blocked!"));
		}
	}

	public void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(usage));
	}

	private boolean contains(String[] args, String text) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase(text))
				return true;
		}
		return false;
	}
}
