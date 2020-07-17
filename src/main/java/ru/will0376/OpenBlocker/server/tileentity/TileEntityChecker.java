package ru.will0376.OpenBlocker.server.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class TileEntityChecker {
	public static boolean checkBlock(EntityPlayer player, String inputReg, World world, BlockPos pos) {
		if (inputReg.contains("ic2:te") && Loader.isModLoaded("ic2")) {
			return new IC2Checker().handler(player, inputReg, world, pos);
		}
		return false;
	}
}
