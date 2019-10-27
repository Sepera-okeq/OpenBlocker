package ru.will0376.OpenBlocker.server.comands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ru.will0376.OpenBlocker.common.ChatForm;

public interface Base {
	String usage = ChatForm.prefix + "/xb ";

	void help(ICommandSender sender);

	void add(MinecraftServer server, ICommandSender sender, String[] args);

	void remove(MinecraftServer server, ICommandSender sender, String[] args);

}
