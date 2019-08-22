package ru.will0376.xBlocker.server.comands;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.math.NumberUtils;
import ru.will0376.xBlocker.common.ChatForm;
import ru.will0376.xBlocker.common.JsonHelper;

import java.text.Format;

public class CommandAdd implements Base{
	String usageadd = Base.usage+"add (or set) <reason> <meta>* (true)*\n" +
			"<meta> - Put 0 if you want to get the meter out of the block \n" +
			"*true - if you want to block all metadata*\n" +
			"ex: /xb add test add 1";
	String usageremove = Base.usage+"remove(or delete) <meta or 'true'>";
	public void add(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		boolean allMeta = false;
		int endLenght = args.length -1;
		ItemStack itemStack = player.getHeldItemMainhand();
		String reason = "";
		String meta;
		if(args.length < 3) { sender.sendMessage(new TextComponentString(usageadd)); return;}
		if(player.getHeldItemMainhand().isEmpty()){ sender.sendMessage(new TextComponentString(ChatForm.prefix_warring+"Take a subject in a hand")); return;}
		if(args[args.length -1].equalsIgnoreCase("true"))
			allMeta = true;
		if(allMeta){
			if(args[args.length - 2].equals("0"))
				meta = String.valueOf(itemStack.getMetadata());
			else
				meta = args[args.length - 2];
			endLenght = args.length - 2;
		}

		else
			if(args[args.length-1].equals("0"))
				meta = String.valueOf(itemStack.getMetadata());
			else
				meta = args[args.length - 1];

			for(int i = 1; i < endLenght;i++)
				reason += args[i]+" ";

		JsonObject sub = new JsonObject();
		sub.addProperty("name",itemStack.getDisplayName());
		sub.addProperty("boolTemp",false);
		sub.addProperty("boolBlockAllMeta", allMeta);
		sub.addProperty("reason", reason.trim());
		sub.addProperty("meta", meta);

		JsonHelper.addServer(sub,JsonHelper.BLOCKER,itemStack.getItem().getRegistryName().toString()+":"+meta);
		sender.sendMessage(new TextComponentString(ChatForm.prefix+"Done!"));
	}

	public void remove(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		boolean allMeta = false;
		String meta = "";
		ItemStack itemStack = player.getHeldItemMainhand();
		String name = itemStack.getItem().getRegistryName().toString();
		if(args.length == 2 &&(args[1].equalsIgnoreCase("help") || args[1].equals("?") || args[1].equals("-h"))) { sender.sendMessage(new TextComponentString(usageremove)); return;}
		if(args.length == 2 && NumberUtils.isParsable(args[1])) meta = args[1];
		else if(args.length == 2 && args[1].equals("true")) allMeta = true;
		else meta = String.valueOf(itemStack.getMetadata());
		if(allMeta) {
			/*for (int i = 0; i <= itemStack.getRarity() i++)
				//JsonHelper.removeFromServer(JsonHelper.BLOCKER, name + ":" + i);*/
			return;
		}
		JsonHelper.removeFromServer(JsonHelper.BLOCKER,name+":"+meta);
	}
	private int getAllMetaFromBlock(ItemStack is){
		/*if(is instanceof )*/
		return 0;
	}
}
