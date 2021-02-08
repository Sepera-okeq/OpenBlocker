package ru.will0376.OpenBlocker.server.comands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;
import ru.will0376.OpenBlocker.common.utils.B64;
import ru.will0376.OpenBlocker.common.utils.ChatForm;
import ru.will0376.OpenBlocker.common.utils.FlagData;

import java.util.HashMap;

@GradleSideOnly(GradleSide.SERVER)
public class CommandAdd implements Base {
	String usageadd = Base.usage + "add <args>\n" +
			"   Arguments:\n" +
			"   -text:reason(multi-string)\n" +
			"   -temp(bool)\n" +
			"   -allmeta(bool)\n" +
			"	-disableBox\n\n" +
			"   e.x: /ob add text:Test reason; temp; allmeta; disableBox\n" +
			"   (delimiter ';')";
	String usageremove = Base.usage + "remove(or delete) <agrs>\n" +
			"   Arguments:\n" +
			"   -allmeta(bool)";

	public static String[] getArgs(int mode) {
		if (mode == 0) //add
			return new String[]{"text", "allmeta", "temp", "disableBox"};
		else if (mode == 1) //remove
			return new String[]{"allmeta"};
		return new String[]{""};
	}

	public void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(usageadd + "\n" + usageremove));
	}

	/**
	 * argumets: text,allmeta,temp,disableBox,tile,nbt;
	 */
	public void add(MinecraftServer server, ICommandSender sender, String[] args) {
		try {
			EntityPlayer player = (EntityPlayer) sender;
			if (player.getHeldItemMainhand().isEmpty()) {
				sender.sendMessage(new TextComponentString(ChatForm.prefix_warring + "Take a subject in a hand"));
				return;
			}

			ItemStack itemStack = player.getHeldItemMainhand();

			HashMap<String, String> parsed = new CommandParser().commandParser(ComandsMain.stringArrToString(args)
					.replace("add ", ""));

			String text = parsed.getOrDefault("text", Main.config.getDefRes());
			boolean temp = Boolean.parseBoolean(parsed.getOrDefault("temp", "false"));
			boolean allmeta = Boolean.parseBoolean(parsed.getOrDefault("allmeta", "false"));
			boolean disableBox = Boolean.parseBoolean(parsed.getOrDefault("disableBox", "false"));
			boolean tile = Boolean.parseBoolean(parsed.getOrDefault("tile", "false"));
			boolean usenbt = Boolean.parseBoolean(parsed.getOrDefault("nbt", "false"));

			Blocked blockedByStack = BlockHelper.findBlockedByStack(itemStack);
			if (blockedByStack == null) blockedByStack = Blocked.builder()
					.stack(itemStack)
					.reason(text)
					.build()
					.addStatus(Blocked.Status.Blocked);

			if (!blockedByStack.getStatus().contains(Blocked.Status.Blocked))
				blockedByStack.getStatus().add(Blocked.Status.Blocked);

			if (temp) blockedByStack.addNewFlag(FlagData.Flag.Temp, true);
			if (allmeta) blockedByStack.addNewFlag(FlagData.Flag.AllMeta, true);
			if (disableBox) blockedByStack.addNewFlag(FlagData.Flag.DisableBox, true);
			if (tile) blockedByStack.addNewFlag(FlagData.Flag.Tile, true);
			if (usenbt) {
				NBTTagCompound nbtTagCompound = itemStack.writeToNBT(new NBTTagCompound());
				blockedByStack.setNbt(B64.encode(nbtTagCompound.toString()));
			}

			BlockHelper.addNewBlocked(blockedByStack);
			sender.sendMessage(new TextComponentString("Done!"));
			BlockHelper.save();
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(new TextComponentString(ChatForm.prefix_error + e.getMessage()));
		}
	}

	public void remove(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		if (player.getHeldItemMainhand().isEmpty()) {
			sender.sendMessage(new TextComponentString(ChatForm.prefix_warring + "Take a subject in a hand"));
			return;
		}

		ItemStack itemStack = player.getHeldItemMainhand();
		Blocked blockedByStack = BlockHelper.findBlockedByStack(itemStack);

		if (blockedByStack != null) BlockHelper.removeStatus(blockedByStack, Blocked.Status.Blocked);

		sender.sendMessage(new TextComponentString("Done!"));
		BlockHelper.save();

	}
}
