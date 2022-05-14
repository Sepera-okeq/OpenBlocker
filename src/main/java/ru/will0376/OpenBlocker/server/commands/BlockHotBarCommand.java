package ru.will0376.OpenBlocker.server.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.cli.CommandLine;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;
import ru.will0376.OpenBlocker.common.utils.Base64;
import ru.will0376.OpenBlocker.common.utils.Flag;
import ru.will0376.OpenBlocker.common.utils.FlagData;

import java.util.Arrays;
import java.util.List;

public class BlockHotBarCommand extends CommandAbstract {
	public BlockHotBarCommand() {
		super("blockHotBar");
	}

	@Override
	public void add(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
		CommandLine parse = getLine(args);
		EntityPlayer player = (EntityPlayer) sender;
		for (int i = 0; i < 9; ++i) {
			ItemStack itemStack = player.inventory.getStackInSlot(i).copy();
			if (itemStack.isEmpty()) continue;
			itemStack.setCount(1);

			Blocked blockedByStack = BlockHelper.findBlockedByStack(itemStack);

			if (blockedByStack == null) blockedByStack = Blocked.builder()
					.stack(itemStack)
					.reason(parse.getOptionValue("reason", Main.config.getDefRes()))
					.build()
					.addStatus(Blocked.Status.Blocked);

			if (blockedByStack.getNBT() != null && !blockedByStack.getNBT().isEmpty() && !blockedByStack.getNBT().equals("null"))
				blockedByStack = (Blocked) blockedByStack.clone();

			if (!blockedByStack.getStatus().contains(Blocked.Status.Blocked))
				blockedByStack.getStatus().add(Blocked.Status.Blocked);

			if (parse.hasOption("temp"))
				blockedByStack.addNewFlag(Flag.Flags.Temp, true);
			if (parse.hasOption("allMeta"))
				blockedByStack.addNewFlag(Flag.Flags.AllMeta, true);
			if (parse.hasOption("disableBox"))
				blockedByStack.addNewFlag(Flag.Flags.DisableBox, true);
			if (parse.hasOption("tile"))
				blockedByStack.addNewFlag(Flag.Flags.Tile, true);
			if (parse.hasOption("interaction"))
				blockedByStack.addNewFlag(Flag.Flags.Interaction, true);
			if (parse.hasOption("useNbt")) {
				NBTTagCompound nbtTagCompound = itemStack.writeToNBT(new NBTTagCompound());
				blockedByStack.addData(FlagData.Const.NBTData, Base64.encode(nbtTagCompound.toString()));
			}

			BlockHelper.addNewBlocked(blockedByStack);
		}

		sender.sendMessage(new TextComponentString("Done!"));
		BlockHelper.save();
	}

	@Override
	public void remove(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
		EntityPlayer player = (EntityPlayer) sender;

		for (int i = 0; i < 9; ++i) {
			ItemStack itemStack = player.inventory.getStackInSlot(i);
			if (itemStack.isEmpty()) continue;
			Blocked blockedByStack = BlockHelper.findBlockedByStack(itemStack);

			if (blockedByStack != null) BlockHelper.removeStatus(blockedByStack, Blocked.Status.Blocked);
		}

		sender.sendMessage(new TextComponentString("Done!"));
		BlockHelper.save();
	}

	@Override
	public List<Argument> getArgList() {
		return Arrays.asList(Argument.builder()
				.name("allMeta")
				.desc("блокирует все метадаты")
				.build(), Argument.builder()
				.name("reason")
				.desc("причина блокировки")
				.hasArg(true)
				.build(), Argument.builder().name("temp").desc("помечает предмет временно заблокированным")
				.build(), Argument.builder()
				.name("disableBox")
				.desc("отключает выделение блока в мире")
				.build(), Argument.builder().name("useNbt").desc("использовать НБТ").build(), Argument.builder()
				.name("useTile")
				.desc("Тест")
				.build(), Argument.builder()
				.name("useItem")
				.desc("использовать предмет из строки")
				.hasArg(true)
				.build(), Argument.builder()
				.name("useItemMeta")
				.desc("использовать мету из строки")
				.hasArg(true)
				.build(), Argument.builder().name("interaction").desc("разрешает использовать блок в мире").build());
	}
}
