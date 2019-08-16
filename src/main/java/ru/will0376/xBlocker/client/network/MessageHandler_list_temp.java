package ru.will0376.xBlocker.client.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.will0376.xBlocker.Main;

public class MessageHandler_list_temp implements IMessageHandler<MessageHandler_list_temp, IMessage>, IMessage {
	String text;

	public MessageHandler_list_temp() {
	}

	public MessageHandler_list_temp(String text) {
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
	public IMessage onMessage(MessageHandler_list_temp message, MessageContext ctx) {
		if (Main.debug) System.out.println("templist: " + message.text);
		try {
			handleClientSide(Integer.valueOf(message.text.split(";")[0]), message.text.split(";")[1], Integer.valueOf(message.text.split(";")[2]), message.text.split(";")[3]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void handleClientSide(int act, String name, int meta, String res) {
		/***
		 * act = 0 - remove from list;
		 * act = 1 - add to list;
		 * act = 2 - clear list;
		 * act = 3 - remove recipe from jei
		 */
		switch (act) {
			case 0:
				Main.getInstance().listTempClient.remove(name + ":" + meta + "@" + res);
				break;

			case 1:
				Main.getInstance().listTempClient.add(name + ":" + meta + "@" + res);
				break;

			case 2:
				Main.getInstance().listTempClient.clear();
				break;
		}
	}

}

