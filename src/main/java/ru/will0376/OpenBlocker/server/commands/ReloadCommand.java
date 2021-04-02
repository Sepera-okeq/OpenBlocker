package ru.will0376.OpenBlocker.server.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.utils.ChatForm;

import java.util.List;

@GradleSideOnly(GradleSide.SERVER)

public class ReloadCommand extends CommandAbstract {
	public ReloadCommand() {
		super("reload");
	}

	@Override
	public void add(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {

	}

	@Override
	public void remove(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {

	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
		BlockHelper.init();
		BlockHelper.sendAllBlocksToAll();
		sender.sendMessage(new TextComponentString(ChatForm.prefix + "Reloaded!"));
	}

	@Override
	public List<Argument> getArgList() {
		return null;
	}
}
