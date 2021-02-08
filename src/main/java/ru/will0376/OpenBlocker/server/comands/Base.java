package ru.will0376.OpenBlocker.server.comands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.common.utils.ChatForm;

@GradleSideOnly(GradleSide.SERVER)
public interface Base {
	String usage = ChatForm.prefix + "/ob ";

	void help(ICommandSender sender);

	void add(MinecraftServer server, ICommandSender sender, String[] args);

	void remove(MinecraftServer server, ICommandSender sender, String[] args);

}
