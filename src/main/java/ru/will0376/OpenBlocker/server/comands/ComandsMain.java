package ru.will0376.OpenBlocker.server.comands;

import com.sun.istack.internal.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.ItemHelper;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.ArrayList;
import java.util.List;

@GradleSideOnly(GradleSide.SERVER)
public class ComandsMain extends CommandBase {
	private final String usage = "/ob <module>\n" +
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

	public static ArrayList<String> getperm() {
		ArrayList<String> r = new ArrayList<>();
		r.add("ob.main <- commands");
		r.add("ob.debug.messages <- debug messages for player");
		return r;
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
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {

		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, "add", "add-help", "mincost", "mincost-help", "enchant", "enchant-help",
					"craft", "craft-help", "limit", "limit-help", "perms", "reload", "debug", "getnbt", "getmaxmeta");
		if (args.length == 2) {
			switch (args[0]) {
				case "add":
					return getListOfStringsMatchingLastWord(args, CommandAdd.getArgs(0));
				case "remove":
					return getListOfStringsMatchingLastWord(args, CommandAdd.getArgs(1));
				case "craft":
					return getListOfStringsMatchingLastWord(args, CommandCraft.getArgs());
				case "limit":
					return getListOfStringsMatchingLastWord(args, CommandLimit.getArgs(0));
				case "removelimit":
					return getListOfStringsMatchingLastWord(args, CommandLimit.getArgs(1));
				case "mincost":
					return getListOfStringsMatchingLastWord(args, CommandMincost.getArgs());
			}
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender.canUseCommand(4, "ob.main")) {
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
					sender.sendMessage(new TextComponentString(ChatForm.prefix + "Permissions:\n" + String.join(";\n", getperm())));
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
		} else sender.sendMessage(new TextComponentString(ChatForm.prefix_error + "No rights!"));
	}
}
