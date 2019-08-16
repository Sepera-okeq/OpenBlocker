package ru.will0376.xBlocker.client.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.will0376.xBlocker.Main;

public class MessageHandler_list_ench implements IMessageHandler<MessageHandler_list_ench, IMessage>, IMessage {
	String text;

	public MessageHandler_list_ench() {
	}

	public MessageHandler_list_ench(String text) {
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
	public IMessage onMessage(MessageHandler_list_ench message, MessageContext ctx) {
		if (Main.debug) System.out.println("ench: " + message.text);
		try {
			handleClientSide(Integer.valueOf(message.text.split(";")[0]), message.text.split(";")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void handleClientSide(int act, String id) {
		/**
		 * act=0 - remove id
		 * act=1 - add id+res
		 * act=2 - clear list
		 *
		 */
		switch (act) {
			case 0:
				Main.getInstance().listEnchantClient.remove(id);
				break;
			case 1:
				Main.getInstance().listEnchantClient.add(id);
				break;
			case 2:
				Main.getInstance().listEnchantClient.clear();
				break;
		}
	}
}

