package ru.will0376.OpenBlocker;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyUtils {
	public static final KeyBinding key = new KeyBinding("Gui Blocked Item", 48, "OpenBlocker");

	public static void preInit() {
		ClientRegistry.registerKeyBinding(key);
	}
}
