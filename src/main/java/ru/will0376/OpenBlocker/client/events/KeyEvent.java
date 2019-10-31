package ru.will0376.OpenBlocker.client.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import ru.will0376.OpenBlocker.KeyUtils;
import ru.will0376.OpenBlocker.client.GuiBlocker;

@Mod.EventBusSubscriber
public class KeyEvent {
	@SubscribeEvent
	public static void actionOpenGui(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START && event.side == Side.CLIENT) {
			if (KeyUtils.key.isPressed())
				Minecraft.getMinecraft().displayGuiScreen(new GuiBlocker());
		}
	}
}
