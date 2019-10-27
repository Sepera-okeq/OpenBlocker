package ru.will0376.OpenBlocker.server.comands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class ComandsMain extends CommandBase {
	private String usage = "/xb <module>\n" +
			"Helps:\n" +
			"-> add-help\n" +
			"-> mincost-help\n" +
			"-> enchant-help\n" +
			"-> limit-help\n" +
			"-> craft-help\n" +
			"\n" +
			"Other modules:\n" +
			"-> perms\n" +
			"-> reload";

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
		al.add("OpenBlocker");
		al.add("xbl");
		return al;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString(usage));
			return;
		}
		switch (args[0].toLowerCase()) {

			case "add":
			case "set":
				new CommandAdd().add(server, sender, args);
				break;
			case "add-help":
				new CommandAdd().help(sender);

			case "mincost":
				new CommandMincost().execute(server, sender, args);
				break; //add & remove
			case "mincost-help":
				new CommandMincost().help(sender);

			case "enchant":
				new CommandEnchant().execute(server, sender, args);
				break; //add & remove
			case "enchant-help":
				new CommandEnchant().help(sender);
				break;

			case "limit":
				new CommandLimit().add(server, sender, args);
				break;
			case "limit-help":
				new CommandLimit().help(sender);
				break;

			case "craft":
				new CommandCraft().execute(server, sender, args);
				break; //add & remove
			case "craft-help":
				new CommandCraft().help(sender);
				break;

			case "remove":
			case "delete":
				new CommandAdd().remove(server, sender, args);
				break;
			case "removelimit":
				new CommandLimit().remove(server, sender, args);
				break;
			///

			case "perms":
				break;
			case "reload":
				JsonHelper.init();
				break;
			default:
				sender.sendMessage(new TextComponentString(usage));
		}
	}
}
