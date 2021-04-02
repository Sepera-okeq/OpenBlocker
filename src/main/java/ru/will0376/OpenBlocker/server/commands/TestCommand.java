package ru.will0376.OpenBlocker.server.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.util.List;

@GradleSideOnly(GradleSide.SERVER)

public class TestCommand extends CommandAbstract {
	public TestCommand() {
		super("test");
	}

	@Override
	public void add(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {

	}

	@Override
	public void remove(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {

	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {

	}

	@Override
	public List<Argument> getArgList() {
		return null;
	}
}
