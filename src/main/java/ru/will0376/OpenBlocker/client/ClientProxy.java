package ru.will0376.OpenBlocker.client;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.will0376.OpenBlocker.KeyUtils;
import ru.will0376.OpenBlocker.common.CommonProxy;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		KeyUtils.preInit();

	}

}
