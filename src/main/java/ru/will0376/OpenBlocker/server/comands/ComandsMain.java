package ru.will0376.OpenBlocker.server.comands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.ItemHelper;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.ArrayList;
import java.util.List;

@GradleSideOnly(GradleSide.SERVER)
public class ComandsMain extends CommandBase {
	private String usage = "/ob <module>\n" +
			"Helps:\n" +
			"-> add-help\n" +
			"-> mincost-help\n" +
			"-> enchant-help\n" +
			"-> limit-help\n" +
			"-> craft-help\n" +
			"\n" +
			"Other modules:\n" +
			"-> perms\n" +
			"-> reload\n" +
			"-> debug";

	public static String stringArrToString(String[] arr) {
		StringBuilder sb = new StringBuilder();
		for (String obj : arr)
			sb.append(obj).append(" ");
		return sb.substring(0, sb.length() - 1);

	}

	@Override
	public String getName() {
		return "ob";
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
		al.add("xb");
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
				break;
			case "mincost":
				if (Main.config.isEnabledMinCost())
					new CommandMincost().execute(server, sender, args);
				else sender.sendMessage(new TextComponentString(ChatForm.prefix_warring + "Module disabled"));

				break; //add & remove
			case "mincost-help":
				if (Main.config.isEnabledMinCost())
					new CommandMincost().help(sender);
				else sender.sendMessage(new TextComponentString(ChatForm.prefix_warring + "Module disabled"));

				break;
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
			case "getnbt":
				try {
					NBTTagCompound tag = new NBTTagCompound();
					Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).writeToNBT(tag);
					sender.sendMessage(new TextComponentString(tag.toString()));
				} catch (NullPointerException ignored) {
				}
				break;
			case "getmaxmeta": {
				Item i = Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).copy().getItem();
				sender.sendMessage(new TextComponentString(ItemHelper.getCountAllSubItems(i) + ""));
			}
			break;
			case "perms":
				sender.sendMessage(new TextComponentString(ChatForm.prefix + "WIP!"));
				break;
			case "reload":
				JsonHelper.init();
				JsonHelper.resendToClient();
				sender.sendMessage(new TextComponentString(ChatForm.prefix + "Reloaded!"));
				break;
			case "debug":
				Main.debug = !Main.debug;
				sender.sendMessage(new TextComponentString(ChatForm.prefix + "Done. Debug = " + Main.debug));
				break;
			default:
				sender.sendMessage(new TextComponentString(usage));
		}
	}
}
