package ru.will0376.xBlocker.server.comands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class ComandsMain extends CommandBase {
	private String usage = "/xb <module>";
	@Override
	public String getName() {
		return "xb";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public List<String> getAliases() {
		ArrayList<String> al = new ArrayList<>();
		al.add("xblocker");
		al.add("xbl");
		return al;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0){
			sender.sendMessage(new TextComponentString(usage));
			return;
		}
		switch (args[0].toLowerCase()){
			case "add":
			case "set": break;
			case "tempblock": break;
			case "mincost": break;
			case "enchant":break; //add & remove
			case "limit": break;
			case "craft": break;


			case "remove":
			case "delete": break;
			case "deletetempblock":
			case "removetempblock": break;
			case "removelimit": break;


			case "perms": break;
			case "reload": break;
			default: sender.sendMessage(new TextComponentString(usage));
		}
	}
}
