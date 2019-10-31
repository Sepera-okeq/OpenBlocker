package ru.will0376.OpenBlocker.common;

import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Blocker implements IMessageHandler<Blocker, IMessage>, IMessage {
	String text;

	public Blocker() {
	}

	public Blocker(String text) {
		this.text = text;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.text = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.text);
	}

	@Override
	public IMessage onMessage(Blocker message, MessageContext ctx) {
		try {
			JsonParser parser = new JsonParser();
			JsonHelper.client = parser.parse(message.text).getAsJsonObject();
		} catch (Exception e) {
			net.minecraft.client.Minecraft.getMinecraft().player.sendChatMessage(e.getMessage());
		}
		return null;
	}
}
