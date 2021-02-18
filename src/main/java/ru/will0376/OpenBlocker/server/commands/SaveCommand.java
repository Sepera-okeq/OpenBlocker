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

public class SaveCommand extends CommandAbstract {
	public SaveCommand() {
		super("save");
	}

	@Override
	public void add(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
	}

	@Override
	public void remove(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
		BlockHelper.save();
		sender.sendMessage(new TextComponentString(ChatForm.prefix + "Saved!"));
	}

	@Override
	public List<Argument> getArgMap() {
		return null;
	}
}
