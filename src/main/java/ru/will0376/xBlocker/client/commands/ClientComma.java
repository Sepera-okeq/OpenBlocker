package ru.will0376.xBlocker.client.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.will0376.xBlocker.Main;

public class ClientComma extends CommandBase {
	@Override
	public String getName() {
		return "xbC";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/xbC <submodule>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0){
			sender.sendMessage(new TextComponentString(getUsage(sender)));
			return;
		}
		switch (args[0].toLowerCase()){
			case "printallblocked":{
				StringBuilder stringBuilder = new StringBuilder();
				Main.getInstance().listItemsClient.forEach((block) -> stringBuilder.append(block+"\n"));
			sender.sendMessage(new TextComponentString("Blocked:\n"+stringBuilder.toString()));
			break;
			}
			case "printalllimited":{
				StringBuilder stringBuilder = new StringBuilder();
				Main.getInstance().listLimitedClient.forEach((limit) -> stringBuilder.append(limit+"\n"));
				sender.sendMessage(new TextComponentString("Limited:\n"+stringBuilder.toString()));
			break;
			}


		}
	}
}
