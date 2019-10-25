package ru.will0376.xBlocker.server.comands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import ru.will0376.xBlocker.common.ChatForm;

public interface Base {
	String usage = ChatForm.prefix+"/xb ";
	void help(ICommandSender sender);
	 void add(MinecraftServer server, ICommandSender sender, String[] args);

	void remove(MinecraftServer server, ICommandSender sender, String[] args);

}
