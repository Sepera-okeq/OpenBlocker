package ru.will0376.OpenBlocker;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyUtils {
	public static final KeyBinding key = new KeyBinding("Gui Blocked Item", Keyboard.KEY_B, "OpenBlocker");

	public static void preInit() {
		ClientRegistry.registerKeyBinding(key);
	}
}
