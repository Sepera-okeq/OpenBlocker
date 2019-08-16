package ru.will0376.xBlocker.client.events;


import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import ru.will0376.xBlocker.KeyUtils;
import ru.will0376.xBlocker.client.gui.GuiColor;
import ru.will0376.xBlocker.client.gui.GuiListItemBlocked;


public class GuiEvent {
	private Minecraft mc = Minecraft.getMinecraft();
	boolean k_reload;
	boolean k_reload_last = false;
	boolean k_mode;
	boolean k_mode_last = false;
	boolean started = false;

	@SubscribeEvent
	public void actionOpenGui(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START && event.side == Side.CLIENT) {
			if (KeyUtils.key.isPressed()) {
				this.k_reload = true;
				if (!this.k_reload_last && this.k_reload) {
					this.started = true;
					if (Keyboard.isKeyDown(15)) {
						this.mc.displayGuiScreen(new GuiColor());
					} else {
						this.mc.displayGuiScreen(new GuiListItemBlocked());
					}
				}

				this.k_reload_last = this.k_reload;
			} else {
				this.k_reload = false;
				this.k_reload_last = this.k_reload;
			}
		}

	}
}
