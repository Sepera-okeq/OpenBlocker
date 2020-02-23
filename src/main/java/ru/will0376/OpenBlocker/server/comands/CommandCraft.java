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
public class CommandCraft {
	String usage = Base.usage + "craft <meta>* <reason>\n" +
			"if item already blocked - it will be deleted\n" +
			"<meta> - Put 'all' if you want to get the meter out of the block \n" +
			"ex: /ob craft all lololo";

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		boolean all = false;
		int meta = is.getMetadata();
		if (is.isEmpty()) {
			player.sendMessage(new TextComponentString("Take item/block into hand"));
			return;
		}
		if (args.length > 1 && NumberUtils.isNumber(args[1]))
			meta = Integer.parseInt(args[1]);
		if (contains(args, "all")) {
			meta = 0;
			all = true;
		}
		if (JsonHelper.contains(JsonHelper.CRAFT, is.getItem().getRegistryName().toString() + ":" + meta)) {
			JsonHelper.removeFromServer(JsonHelper.CRAFT, is.getItem().getRegistryName().toString() + ":" + meta);
			player.sendMessage(new TextComponentString(ChatForm.prefix + "Item/Block the block will be available for JEI immediately after rebooting the client!"));
		} else {
			JsonObject jo = new JsonObject();
			if (all)
				jo.addProperty("boolBlockAllMeta", true);
			String reason = "";
			for (int i = 1; i < args.length; i++) {
				if (args[1].equals(meta + "") || (all && i == 1))
					continue;
				reason += args[i] + " ";

			}
			if (is.getTagCompound() != null && !is.getTagCompound().isEmpty()) {
				NBTTagCompound nbtTagCompound = is.getTagCompound();
				JsonArray ja = new JsonArray();
				for (String tgs : nbtTagCompound.getKeySet()) {
					ja.add(nbtTagCompound.getTag(tgs).toString().replace("\"", ""));
				}
				jo.add("nbts", ja);
			}
			jo.addProperty("reason", reason);
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
