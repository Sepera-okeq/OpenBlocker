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
public class CommandCraft {
	String usage = Base.usage + "craft <args>\n" +
			"   Arguments:\n" +
			"   -text:reason(multi-string)\n" +
			"   -allmeta(bool)\n\n" +
			"   e.x: /ob craft text:Test reason; allmeta\n" +
			"   (delimiter ';')";

	public static String[] getArgs() {
		return new String[]{"text", "allmeta"};
	}

	/**
	 * argumets: text,allmeta;
	 */
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		try {
			EntityPlayer player = (EntityPlayer) sender;
			ItemStack is = player.getHeldItemMainhand();
			if (is.isEmpty()) {
				player.sendMessage(new TextComponentString("Take item/block into hand"));
				return;
			}

			HashMap<String, String> parsed = new CommandParser().commandParser(ComandsMain.stringArrToString(args)
					.replace("craft ", ""));

			String text = parsed.getOrDefault("text", Main.config.getDefRes());
			boolean allmeta = Boolean.parseBoolean(parsed.getOrDefault("allmeta", "false"));
			Blocked blockedByStack = BlockHelper.findBlockedByStack(is);
			if (blockedByStack != null) {
				if (blockedByStack.getStatus().contains(Blocked.Status.Craft)) {
					BlockHelper.removeStatus(blockedByStack, Blocked.Status.Craft);
					return;
				}
			}
			if (blockedByStack == null) {
				blockedByStack = Blocked.builder().stack(is).reason(text).build().addStatus(Blocked.Status.Craft);
				if (allmeta) blockedByStack.addNewFlag(FlagData.Flag.AllMeta, true);
			}
			BlockHelper.addStatus(blockedByStack, Blocked.Status.Craft);
			sender.sendMessage(new TextComponentString("Done!"));
			BlockHelper.save();

		} catch (Exception ex) {
			ex.printStackTrace();
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
