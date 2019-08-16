package ru.will0376.xBlocker.server.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.xBlocker.Main;


public class PluginMessage implements IMessage, IMessageHandler<PluginMessage, IMessage> {
	public String text;

	public PluginMessage() {
	}

	public PluginMessage(String text) {
		this.text = text;
	}

	public void fromBytes(ByteBuf buf) {
		this.text = ByteBufUtils.readUTF8String(buf);
	}

	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.text);
	}

	@Override
	public IMessage onMessage(PluginMessage message, MessageContext ctx) {
		if (Main.FOR_SERVER)
			return logic(message, ctx);
		return null;
	}

	@GradleSideOnly(GradleSide.SERVER)
	private IMessage logic(PluginMessage message, MessageContext ctx) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GradleSideOnly(GradleSide.SERVER)
	private EntityPlayerMP getEPMP(String nick) {
		for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
			if (player.getGameProfile().getName().equals(nick))
				return player;
		return null;
	}
}
