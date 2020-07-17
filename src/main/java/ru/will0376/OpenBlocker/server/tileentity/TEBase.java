package ru.will0376.OpenBlocker.server.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

@GradleSideOnly(GradleSide.SERVER)
public abstract class TEBase {
	public static boolean isTileEntity(World world, BlockPos pos) {
		TileEntity entity = world.getTileEntity(pos);
		return entity != null;
	}

	public abstract boolean handler(EntityPlayer player, String inputReg, World world, BlockPos pos);
}
