package ru.will0376.OpenBlocker.server.comands;

import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.math.NumberUtils;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.JsonHelper;

public class CommandLimit implements Base {
	String addusage = Base.usage + "limit <limit> <meta>*";
	String removeusage = Base.usage + "removelimit <meta>*\n" +
			"<meta>* - Put 'all' if you want to get the meter out of the block ";

	public void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(addusage + "\n" + removeusage));
	}

	public void add(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		int meta = is.getMetadata();
		boolean all = false;
		if (is.isEmpty()) {
			player.sendMessage(new TextComponentString("Hand is empty!"));
			return;
		}
		if (args.length <= 1) {
			help(sender);
			return;
		}

		if (args.length > 2 && NumberUtils.isNumber(args[2]))
			meta = Integer.parseInt(args[2]);

		if (contains(args, "all")) {
			all = true;
			meta = 0;
		}

		JsonObject jo = new JsonObject();
		if (all)
			jo.addProperty("boolBlockAllMeta", true);
		jo.addProperty("limit", args[1]);
		JsonHelper.addServer(jo, JsonHelper.LIMIT, is.getItem().getRegistryName().toString() + ":" + meta);
		player.sendMessage(new TextComponentString(ChatForm.prefix + String.format("The limit for %s block is set to %s", is.getItem().getRegistryName().toString() + ":" + meta, args[1])));
	}

	public void remove(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		int meta = is.getMetadata();
		if (is.isEmpty()) {
			player.sendMessage(new TextComponentString("Hand is empty!"));
			return;
		}
		if (args.length > 1 && NumberUtils.isNumber(args[1]))
			meta = Integer.parseInt(args[1]);

		if (contains(args, "all"))
			meta = 0;
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
