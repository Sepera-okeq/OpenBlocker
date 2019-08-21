package ru.will0376.xBlocker.server.comands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface Base {
	void add(MinecraftServer server, ICommandSender sender, String[] args);

	void remove(MinecraftServer server, ICommandSender sender, String[] args);

}
