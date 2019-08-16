package ru.will0376.xBlocker.client.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.will0376.xBlocker.Main;

public class MessageHandler_list_limited implements IMessageHandler<MessageHandler_list_limited, IMessage>, IMessage {
	String text;

	public MessageHandler_list_limited() {
	}

	public MessageHandler_list_limited(String text) {
		this.text = text;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		text = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, text);
	}

	@Override
	public IMessage onMessage(MessageHandler_list_limited message, MessageContext ctx) {
		try {
			if (Main.debug) System.out.println("Limited: " + message.text);
			handleClientSide(Integer.valueOf(message.text.split(";")[0]), message.text.split(";")[1], Integer.valueOf(message.text.split(";")[2]), Integer.valueOf(message.text.split(";")[3]), message.text.split(";")[4]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void handleClientSide(int act, String name, int meta, int limit, String res) {
		switch (act) {
			case 0:
				Main.getInstance().listLimitedClient.remove(name + ":" + meta + ":" + limit + ":" + res);
				break;
			case 1:
				Main.getInstance().listLimitedClient.add(name + ":" + meta + ":" + limit + ":" + res);
				break;
			case 2:
				Main.getInstance().listLimitedClient.clear();
		}
	}
}

