package ru.will0376.OpenBlocker.common;

import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.will0376.OpenBlocker.client.ItemsBlocks;

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
			ItemsBlocks.ib.clear();
			JsonHelper.client.entrySet().forEach(l ->
					l.getValue().getAsJsonObject().entrySet().forEach(t ->
							new ItemsBlocks(t.getKey())));
			CraftManager.removedRecipe.forEach(rem -> {
				if (!ItemsBlocks.containStack(rem.getIs())) CraftManager.bringBack(rem.getIs());
			});
			CraftManager.removedRecipe.removeIf(CraftPOJO::getDelete);
		} catch (Exception e) {
			e.printStackTrace();
			net.minecraft.client.Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatForm.prefix_error_client + "Error loading json from server!"));
		}
		return null;
	}
}
