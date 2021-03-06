package ru.will0376.OpenBlocker.common.net;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;

@Builder(toBuilder = true)
@AllArgsConstructor
public class ToClientBlocked implements IMessage, IMessageHandler<ToClientBlocked, IMessage> {
	Blocked blocked;
	Action action;

	public ToClientBlocked() {
	}

	@Override
	public void toBytes(ByteBuf byteBuf) {
		byteBuf.writeInt(action.ordinal());
		if (action == Action.AddBlock || action == Action.RemoveBlock || action == Action.UpdateBlock)
			ByteBufUtils.writeUTF8String(byteBuf, blocked.toStr());
	}

	@Override
	public void fromBytes(ByteBuf byteBuf) {
		int i = byteBuf.readInt();
		action = Action.values()[i];

		if (action != Action.ClearList) blocked = Blocked.fromJson(ByteBufUtils.readUTF8String(byteBuf));
	}

	@Override
	public IMessage onMessage(ToClientBlocked toClientBlocked, MessageContext messageContext) {
		switch (toClientBlocked.action) {

			case ClearList:
				BlockHelper.BlockHelperClient.blockedListClient.clear();

				break;

			case AddBlock:
				BlockHelper.addNewBlocked(toClientBlocked.blocked);
				break;

			case RemoveBlock:
				BlockHelper.removeBlocked(toClientBlocked.blocked);
				break;

			case UpdateBlock:
				BlockHelper.removeBlocked(toClientBlocked.blocked);
				BlockHelper.addNewBlocked(toClientBlocked.blocked);
				break;
		}
		return null;
	}

	public enum Action {
		AddBlock,
		RemoveBlock,
		ClearList,
		UpdateBlock
	}
}