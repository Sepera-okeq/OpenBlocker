package ru.will0376.OpenBlocker.server.tileentity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class TileEntityChecker {
	public static boolean checkBlock(String inputReg, World world, BlockPos pos) {
		if (inputReg.contains("ic2") && Loader.isModLoaded("ic2")) {
			return new IC2Checker().handler(inputReg, world, pos);
		}
		return false;
	}
}
