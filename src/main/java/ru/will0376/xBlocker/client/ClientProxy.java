package ru.will0376.xBlocker.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import ru.will0376.xBlocker.KeyUtils;
import ru.will0376.xBlocker.client.events.GuiEvent;
import ru.will0376.xBlocker.client.events.LokiEventClient;
import ru.will0376.xBlocker.client.events.RenderEvent;
import ru.will0376.xBlocker.common.CommonProxy;

public class ClientProxy extends CommonProxy {
	@Override
	public void events(FMLInitializationEvent event) {
		KeyUtils.preInit();
		MinecraftForge.EVENT_BUS.register(new LokiEventClient());
		MinecraftForge.EVENT_BUS.register(new GuiEvent());
		MinecraftForge.EVENT_BUS.register(new RenderEvent());
	}
}
