package ru.will0376.xBlocker.common;

import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;

public class Hookloader extends HookLoader {
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{PrimaryClassTransformer.class.getName()};
	}

	@Override
	protected void registerHooks() {
		registerHookContainer("ru.will0376.xBlocker.common.hooks.hooks");
		registerHookContainer("ru.will0376.xBlocker.common.hooks.ItemsHooks");
	}
}
