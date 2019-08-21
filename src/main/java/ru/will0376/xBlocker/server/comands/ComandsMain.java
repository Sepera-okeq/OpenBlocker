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
			case "set": new CommandAdd().add(server, sender, args); break;
			case "tempblock": new CommandTemp().add(server, sender, args); break;
			case "mincost": new CommandMincost().execute(server, sender, args);break; //add & remove
			case "enchant": new CommandEnchant().execute(server, sender, args);break; //add & remove
			case "limit": new CommandLimit().add(server, sender, args); break;
			case "craft": new CommandCraft().execute(server, sender, args); break; //add & remove


			case "remove":
			case "delete": new CommandAdd().remove(server, sender, args);break;
			case "deletetempblock":
			case "removetempblock":  new CommandTemp().remove(server, sender, args); break;
			case "removelimit": new CommandLimit().remove(server, sender, args); break;


			case "perms": break;
			case "reload": break;
			default: sender.sendMessage(new TextComponentString(usage));
		}
	}
}
