package ru.will0376.OpenBlocker.server.comands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.B64;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.HashMap;

@GradleSideOnly(GradleSide.SERVER)
public class CommandAdd implements Base {
	String usageadd = Base.usage + "add <args>\n" +
			"   Arguments:\n" +
			"   -text:reason(multi-string)\n" +
			"   -temp(bool)\n" +
			"   -allmeta(bool)\n\n" +
			"   e.x: /ob add text:Test reason; temp; allmeta\n" +
			"   (delimiter ';')";
	String usageremove = Base.usage + "remove(or delete) <agrs>\n" +
			"   Arguments:\n" +
			"   -allmeta(bool)";

	public void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(usageadd + "\n" + usageremove));
	}

	/**
	 * argumets: text,allmeta,temp;
	 */
	public void add(MinecraftServer server, ICommandSender sender, String[] args) {
		try {
			EntityPlayer player = (EntityPlayer) sender;
			if (player.getHeldItemMainhand().isEmpty()) {
				sender.sendMessage(new TextComponentString(ChatForm.prefix_warring + "Take a subject in a hand"));
				return;
			}

			ItemStack itemStack = player.getHeldItemMainhand();
			int meta = itemStack.getMetadata();

			HashMap<String, String> parsed = new CommandParser().commandParser(ComandsMain.stringArrToString(args).replace("add ", ""));

			String text = parsed.getOrDefault("text", Main.config.getDefRes());
			boolean temp = Boolean.parseBoolean(parsed.getOrDefault("temp", "false"));
			boolean allmeta = Boolean.parseBoolean(parsed.getOrDefault("allmeta", "false"));

			JsonObject jo = new JsonObject();
			if (allmeta) {
				meta = 0;
				jo.addProperty("boolBlockAllMeta", true);
			}
			if (temp)
				jo.addProperty("boolTemp", true);
			if (itemStack.getTagCompound() != null && !itemStack.getTagCompound().isEmpty()) {
				JsonArray ja = new JsonArray();
				ja.add(B64.encode(itemStack.serializeNBT().toString()));
				jo.add("nbts", ja);
			}

			jo.addProperty("reason", text.trim());
			JsonHelper.addServer(jo, JsonHelper.BLOCKER, itemStack.getItem().getRegistryName().toString() + ":" + meta);
			sender.sendMessage(new TextComponentString(ChatForm.prefix + String.format("ItemStack: %s successfully added!", itemStack.getItem().getRegistryName().toString() + ":" + meta)));
		} catch (Exception e) {
			sender.sendMessage(new TextComponentString(ChatForm.prefix_error + e.getMessage()));
		}
	}

	/**
	 * argumets: allmeta;
	 */
	public void remove(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		if (player.getHeldItemMainhand().isEmpty()) {
			sender.sendMessage(new TextComponentString(ChatForm.prefix_warring + "Take a subject in a hand"));
			return;
		}

		ItemStack itemStack = player.getHeldItemMainhand();
		int meta = itemStack.getMetadata();
		if (contains(args, "allmeta")) meta = 0;

		JsonHelper.removeFromServer(JsonHelper.BLOCKER, itemStack.getItem().getRegistryName().toString() + ":" + meta);
		sender.sendMessage(new TextComponentString(ChatForm.prefix + String.format("ItemStack: %s successfully deleted!", itemStack.getItem().getRegistryName().toString() + ":" + meta)));
	}

	private boolean contains(String[] args, String text) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase(text))
				return true;
		}
		return false;
	}
}
