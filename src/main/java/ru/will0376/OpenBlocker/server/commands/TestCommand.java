package ru.will0376.OpenBlocker.server.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.List;

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
	public List<Argument> getArgMap() {
		return null;
	}
}
