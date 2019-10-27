package ru.will0376.xBlocker.server.comands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.math.NumberUtils;
import ru.will0376.xBlocker.common.ChatForm;
import ru.will0376.xBlocker.common.JsonHelper;


public class CommandAdd implements Base{
	String usageadd = Base.usage+"add (or set) <reason> <meta>* <temp?(empty or true)> \n" +
			"ex: /xb add test add 1";
	String usageremove = Base.usage+"remove(or delete) <meta>\n" +
			"<meta> - Put 'all' if you want to get the meter out of the block";

	public void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(usageadd+"\n"+usageremove));
	}
	@SuppressWarnings("deprecated")
	public void add(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		boolean allMeta = false;
		boolean temp = false;
		ItemStack itemStack = player.getHeldItemMainhand();
		String reason = "";
		int meta = itemStack.getMetadata();
		int end = args.length - 1;
		if(args.length < 2) { sender.sendMessage(new TextComponentString(usageadd)); return;}
		if(player.getHeldItemMainhand().isEmpty()){ sender.sendMessage(new TextComponentString(ChatForm.prefix_warring+"Take a subject in a hand")); return;}
		if(args[args.length - 1].equalsIgnoreCase("true") || args[args.length -1].equalsIgnoreCase("екгу")) {
			temp = true;
			end = args.length -2;
		}

				if(temp) {
					if (args[args.length - 2].equalsIgnoreCase("all")) {
						allMeta = true;
						meta = 0;
					}
					else if (NumberUtils.isNumber(args[args.length - 2]) && !args[args.length - 2].equalsIgnoreCase("all"))
						meta = Integer.parseInt(args[args.length-2]);
				}
		else {
			if (args[args.length - 1].equalsIgnoreCase("all")) {
				allMeta = true;
				meta = 0;
			}
			else if (NumberUtils.isNumber(args[args.length - 1]) && !args[args.length -1].equalsIgnoreCase("0"))
				meta = Integer.parseInt(args[args.length - 1]);
		}

		for(int i = 1;i<end;i++)
			reason += args[i]+" ";

		JsonObject jo = new JsonObject();
		if(allMeta)
			jo.addProperty("boolBlockAllMeta",true);
		if(temp)
			jo.addProperty("boolTemp",true);


		if (itemStack.getTagCompound() != null && !itemStack.getTagCompound().isEmpty()) {
			NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
			JsonArray ja = new JsonArray();
			for (String tgs : nbtTagCompound.getKeySet()) {
				ja.add(nbtTagCompound.getTag(tgs).toString().replace("\"", ""));
			}
			jo.add("nbts", ja);
		}

		jo.addProperty("reason",reason.trim());
		JsonHelper.addServer(jo,JsonHelper.BLOCKER,itemStack.getItem().getRegistryName().toString()+":"+meta);
		sender.sendMessage(new TextComponentString(ChatForm.prefix+String.format("ItemStack: %s successfully added!",itemStack.getItem().getRegistryName().toString()+":"+meta)));
	}
	@SuppressWarnings("deprecated")
	public void remove(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack itemStack = player.getHeldItemMainhand();
		int meta = itemStack.getMetadata();
		if(args.length >= 3 || (args.length > 1  &&args[1].equalsIgnoreCase("-h"))) {
			sender.sendMessage(new TextComponentString(usageremove));
			return;
		}
		if(contains(args,"all")) {
			meta = 0;
		}
		else if(NumberUtils.isNumber(args[args.length - 1])){
			meta = Integer.parseInt(args[args.length - 1]);
		}
		JsonHelper.removeFromServer(JsonHelper.BLOCKER,itemStack.getItem().getRegistryName().toString()+":"+meta);
		sender.sendMessage(new TextComponentString(ChatForm.prefix+String.format("ItemStack: %s successfully deleted!",itemStack.getItem().getRegistryName().toString()+":"+meta)));
	}
	private boolean contains(String[] args,String text) {
		for (int i = 0; i < args.length; i++) {
			if(args[i].equalsIgnoreCase(text))
				return true;
		}
		return false;
	}
}
