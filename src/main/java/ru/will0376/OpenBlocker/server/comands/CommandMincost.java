package ru.will0376.OpenBlocker.server.comands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.math.NumberUtils;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.JsonHelper;

@GradleSideOnly(GradleSide.SERVER)
public class CommandMincost {
	String usage = Base.usage + "mincost <cost> <meta>*\n" +
			"<meta> - Put 'all' if you want to get the meter out of the block\n" +
			"if enchant already blocked - it will be deleted";

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		int meta = is.getMetadata();
		boolean all = false;
		if (args.length < 2) {
			help(sender);
			return;
		}

		if (args.length > 2 && NumberUtils.isNumber(args[2]))
			meta = Integer.parseInt(args[2]);

		if (contains(args, "all")) {
			all = true;
			meta = 0;
		}

		if (JsonHelper.contains(JsonHelper.MINCOST, is.getItem().getRegistryName().toString() + ":" + meta)) {
			JsonHelper.removeFromServer(JsonHelper.MINCOST, is.getItem().getRegistryName().toString() + ":" + meta);
			sender.sendMessage(new TextComponentString(ChatForm.prefix + String.format("ItemStack: %s successfully removed!", is.getItem().getRegistryName().toString() + ":" + meta)));
		} else {
			JsonObject jo = new JsonObject();
			if (all)
				jo.addProperty("boolBlockAllMeta", true);
			if (is.getTagCompound() != null && !is.getTagCompound().isEmpty()) {
				NBTTagCompound nbtTagCompound = is.getTagCompound();
				JsonArray ja = new JsonArray();
				for (String tgs : nbtTagCompound.getKeySet())
					ja.add(nbtTagCompound.getTag(tgs).toString().replace("\"", ""));

				jo.add("nbts", ja);
			}
			jo.addProperty("cost", args[1]);
			JsonHelper.addServer(jo, JsonHelper.MINCOST, is.getItem().getRegistryName().toString() + ":" + meta);
			sender.sendMessage(new TextComponentString(ChatForm.prefix + String.format("ItemStack: %s with minCost %s, successfully added!", is.getItem().getRegistryName().toString() + ":" + meta, args[1])));
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
