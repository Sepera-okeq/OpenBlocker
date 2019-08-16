package ru.will0376.xBlocker.server.utils;

import net.minecraft.entity.player.EntityPlayer;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;

@GradleSideOnly(GradleSide.SERVER)
public class PexUtils {
	public static boolean hasPex(String permission, EntityPlayer player) {
		if (player.canUseCommand(4, "xblocker.getAll.perm")) {
			return true;
		}
		return player.canUseCommand(4, permission);
	}
}
