package ru.will0376.OpenBlocker.server.comands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;
import ru.will0376.OpenBlocker.common.utils.FlagData;

import java.util.HashMap;

@GradleSideOnly(GradleSide.SERVER)
public class CommandLimit implements Base {
	String addusage = Base.usage + "limit <args>\n" + "   Arguments:\n" + "   -allmeta(bool)\n" + "   -l(int)\n\n" + "   e.x: /ob limit l:5";
	String removeusage = Base.usage + "removelimit";

	public static String[] getArgs(int mode) {
		if (mode == 0) //add
			return new String[]{"l"};
		return new String[]{""};
	}

	public void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(addusage + "\n" + removeusage));
	}

	/**
	 * argumets: l
	 */
	public void add(MinecraftServer server, ICommandSender sender, String[] args) {
		try {
			EntityPlayer player = (EntityPlayer) sender;
			ItemStack is = player.getHeldItemMainhand();
			if (is.isEmpty()) {
				player.sendMessage(new TextComponentString("Hand is empty!"));
				return;
			}
			HashMap<String, String> parsed = new CommandParser().commandParser(ComandsMain.stringArrToString(args)
					.replace("limit ", ""));
			String limit = parsed.getOrDefault("l", "-1");

			Blocked blockedByStack = BlockHelper.findBlockedByStack(is);

			if (blockedByStack == null)
				blockedByStack = Blocked.builder().stack(is).reason(Main.config.getDefRes()).build();

			if (!blockedByStack.containsFlag(FlagData.Flag.Limit))
				blockedByStack.addNewFlag(FlagData.Flag.Limit, limit);

			if (!blockedByStack.getStatus().contains(Blocked.Status.Limit))
				BlockHelper.addStatus(blockedByStack, Blocked.Status.Limit);

			sender.sendMessage(new TextComponentString("Done!"));
		} catch (Exception ex) {
			ex.printStackTrace();}
	}

	public void remove(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		if (is.isEmpty()) {
			player.sendMessage(new TextComponentString("Hand is empty!"));
			return;
		}
		Blocked blockedByStack = BlockHelper.findBlockedByStack(is);
		if (blockedByStack != null) BlockHelper.removeStatus(blockedByStack, Blocked.Status.Limit);

		BlockHelper.save();

	}

	private boolean contains(String[] args, String text) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase(text))
				return true;
		}
		return false;
	}
}
