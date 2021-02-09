package ru.will0376.OpenBlocker.server.tileentity;

import ic2.core.ref.MetaTeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.server.ServerEvents;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@GradleSideOnly(GradleSide.SERVER)
public class IC2Checker extends TEBase {
	@Override
	public List<String> getCheckList() {
		return Arrays.asList("ic2:te");
	}

	@Override
	public boolean handler(EntityPlayer player, String inputReg, World world, BlockPos pos) {
		try {
			TileEntity entity = world.getTileEntity(pos);
			Block block = entity.getBlockType();
			IBlockState state = block.getDefaultState().getActualState(world, pos);

			AtomicReference<Comparable<MetaTeBlock>> ar = new AtomicReference<>();
			state.getProperties().forEach((key, value) -> {
				if (ar.get() == null && key.getName().equals("type")) {
					ar.set((Comparable<MetaTeBlock>) value);
				}
			});
			if (ar.get() != null) {
				MetaTeBlock meta = (MetaTeBlock) ar.get();
				boolean ret = ServerEvents.checkBlock(player, new ItemStack(block, 1, meta.teBlock.getId()), "serverevent.interaction", "PlayerInteractEvent.RightClickBlock");
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
