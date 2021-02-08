package ru.will0376.OpenBlocker.server.comands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

@GradleSideOnly(GradleSide.SERVER)
@Deprecated
public class CommandMincost {
	String usage = Base.usage + "mincost <args>\n" +
			"   Arguments:\n" +
			"   -allmeta(bool)\n" +
			"   -cost(int)\n\n" +
			"   e.x: /ob mincost cost:10; allmeta\n" +
			"   (delimiter ';')";

	public static String[] getArgs() {
		return new String[]{"cost", "allmeta"};
	}

	/**
	 * argumets: cost, allmeta
	 */
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
/*
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		if (is.isEmpty()) {
			player.sendMessage(new TextComponentString("Hand is empty!"));
			return;
		}
		int meta = is.getMetadata();

		HashMap<String, String> parsed = new CommandParser().commandParser(ComandsMain.stringArrToString(args).replace("mincost ", ""));
		String cost = parsed.getOrDefault("cost", "-1");
		boolean allmeta = Boolean.parseBoolean(parsed.getOrDefault("allmeta", "false"));
		if (allmeta) meta = 0;

		if (JsonHelper.contains(JsonHelper.MINCOST, is.getItem().getRegistryName().toString() + ":" + meta)) {
			JsonHelper.removeFromServer(JsonHelper.MINCOST, is.getItem().getRegistryName().toString() + ":" + meta);
			sender.sendMessage(new TextComponentString(ChatForm.prefix + String.format("ItemStack: %s successfully removed!", is.getItem().getRegistryName().toString() + ":" + meta)));
		} else {
			JsonObject jo = new JsonObject();
			if (allmeta)
				jo.addProperty("boolBlockAllMeta", true);
			if (is.getTagCompound() != null && !is.getTagCompound().isEmpty()) {
				NBTTagCompound nbtTagCompound = is.getTagCompound();
				JsonArray ja = new JsonArray();
				for (String tgs : nbtTagCompound.getKeySet())
					ja.add(nbtTagCompound.getTag(tgs).toString().replace("\"", ""));

				jo.add("nbts", ja);
			}
			jo.addProperty("cost", cost);
			JsonHelper.addServer(jo, JsonHelper.MINCOST, is.getItem().getRegistryName().toString() + ":" + meta);
			sender.sendMessage(new TextComponentString(ChatForm.prefix + String.format("ItemStack: %s with minCost %s, successfully added!", is.getItem().getRegistryName().toString() + ":" + meta, args[1])));
		}
*/
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
