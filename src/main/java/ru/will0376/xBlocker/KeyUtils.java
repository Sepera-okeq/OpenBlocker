package ru.will0376.xBlocker;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyUtils {
	public static final KeyBinding key = new KeyBinding("Gui Blocked Item", 48, "xBlocker");

	public static void preInit() {
		ClientRegistry.registerKeyBinding(key);
	}
}
