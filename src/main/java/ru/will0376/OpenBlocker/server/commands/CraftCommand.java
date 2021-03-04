package ru.will0376.OpenBlocker.server.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.cli.CommandLine;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;
import ru.will0376.OpenBlocker.common.utils.FlagData;

import java.util.Arrays;
import java.util.List;

@GradleSideOnly(GradleSide.SERVER)

public class CraftCommand extends CommandAbstract {
	public CraftCommand() {
		super("craft");
	}

	@Override
	public void add(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
		CommandLine parse = getLine(args);

		EntityPlayer player = (EntityPlayer) sender;
		ItemStack itemStack = player.getHeldItemMainhand().copy();
		if (itemStack.isEmpty()) {
			player.sendMessage(new TextComponentString("Take item/block into hand"));
			return;
		}
		itemStack.setCount(1);

		Blocked blockedByStack = BlockHelper.findBlockedByStack(itemStack);

		if (blockedByStack == null) {
			blockedByStack = Blocked.builder()
					.stack(itemStack)
					.reason(parse.hasOption("reason") ? parse.getOptionValue("reason") : Main.config.getDefRes())
					.build();

			if (blockedByStack.getNbt() != null && !blockedByStack.getNbt().isEmpty() && !blockedByStack.getNbt()
					.equals("null")) blockedByStack = (Blocked) blockedByStack.clone();

			BlockHelper.addNewBlocked(blockedByStack);
			if (parse.hasOption("allMeta")) blockedByStack.addNewFlag(FlagData.Flag.AllMeta, true);
		}

		BlockHelper.addStatus(blockedByStack, Blocked.Status.Craft);
		sender.sendMessage(new TextComponentString("Done!"));
		BlockHelper.save();
	}

	@Override
	public void remove(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack itemStack = player.getHeldItemMainhand();
		if (itemStack.isEmpty()) {
			player.sendMessage(new TextComponentString("Take item/block into hand"));
			return;
		}

		Blocked blockedByStack = BlockHelper.findBlockedByStack(itemStack);
		if (blockedByStack != null) {
			BlockHelper.removeStatus(blockedByStack, Blocked.Status.Craft);
		}

		sender.sendMessage(new TextComponentString("Done!"));
		BlockHelper.save();
	}

	@Override
	public List<Argument> getArgMap() {
		return Arrays.asList(Argument.builder().name("allMeta").desc("блокирует все метадаты").build(), Argument.builder()
				.name("reason")
				.desc("причина блокировки")
				.hasArg(true)
				.build(), Argument.builder().name("useNbt").desc("использовать НБТ").build(), Argument.builder()
				.name("useTile")
				.desc("Тест")
				.build());

	}
}
