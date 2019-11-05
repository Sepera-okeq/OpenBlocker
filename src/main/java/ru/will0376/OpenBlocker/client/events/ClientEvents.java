package ru.will0376.OpenBlocker.client.events;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import ru.will0376.OpenBlocker.client.ItemsBlocks;

import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber

public class ClientEvents {

	public static ItemStack getPickBlock(World world, BlockPos pos) {
		try {
			Item item = Item.getItemFromBlock(world.getBlockState(pos).getBlock());
			if (item == null)
				return ItemStack.EMPTY;
			else
				return new ItemStack(item, 1, Block.getBlockFromItem(item).getMetaFromState(world.getBlockState(pos)));
		} catch (Exception e) {
			return ItemStack.EMPTY;
		}
	}


	@SubscribeEvent
	public static void drawBlokced(DrawBlockHighlightEvent e) {
		try {
			if (e.getTarget() != null)
				if (e.getPlayer().getEntityWorld().getBlockState(e.getTarget().getBlockPos()).getBlock() != Blocks.AIR) {
					ItemStack is = getPickBlock(e.getPlayer().getEntityWorld(), e.getTarget().getBlockPos());
					if (check(is))
						render(e);
				}
		} catch (Exception ignore) {
		}
	}

	private static boolean check(ItemStack is) {
		AtomicBoolean ret = new AtomicBoolean(false);
		ItemsBlocks.ib.forEach(l -> {
			if (l.is.isItemEqual(is) && !ret.get())
				ret.set(true);
		});
		return ret.get();
	}

	public static void render(DrawBlockHighlightEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		double ax = event.getTarget().getBlockPos().getX();
		double ay = event.getTarget().getBlockPos().getY();
		double az = event.getTarget().getBlockPos().getZ();
		double bx = (event.getTarget().getBlockPos().getX() + 1);
		double by = (event.getTarget().getBlockPos().getY() + 1);
		double bz = (event.getTarget().getBlockPos().getZ() + 1);
		AxisAlignedBB aabb = new AxisAlignedBB(ax, ay, az, bx, by, bz).expand(0.01, 0.01, 0.01);
		double x_fix = -(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double) event.getPartialTicks());
		double y_fix = -(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double) event.getPartialTicks());
		double z_fix = -(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double) event.getPartialTicks());
		GL11.glPushMatrix();
		GL11.glTranslated(x_fix, y_fix, z_fix);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glColor4f(1, 0, 0, 1.75F);
		xes(aabb);
		pluses(aabb);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.0F);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	private static void pluses(AxisAlignedBB aabb) {
		double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ;
		double maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
		GL11.glBegin(1);
		//top
		GL11.glVertex3d(minX + 0.5, maxY + 0.001, minZ);
		GL11.glVertex3d(minX + 0.5, maxY + 0.001, maxZ);

		GL11.glVertex3d(minX, maxY + 0.001, minZ + 0.5);
		GL11.glVertex3d(maxX, maxY + 0.001, minZ + 0.5);
		//end

		//north
		GL11.glVertex3d(minX, minY + 0.5, minZ - 0.001);
		GL11.glVertex3d(maxX, minY + 0.5, minZ - 0.001);

		GL11.glVertex3d(minX + 0.5, minY, minZ - 0.001);
		GL11.glVertex3d(minX + 0.5, maxY, minZ - 0.001);
		//end

		//east
		GL11.glVertex3d(maxX + 0.001, minY + 0.5, minZ);
		GL11.glVertex3d(maxX + 0.001, minY + 0.5, maxZ);

		GL11.glVertex3d(maxX + 0.001, minY, minZ + 0.5);
		GL11.glVertex3d(maxX + 0.001, maxY, minZ + 0.5);
		//end

		//south
		GL11.glVertex3d(minX, minY + 0.5, maxZ + 0.001);
		GL11.glVertex3d(maxX, minY + 0.5, maxZ + 0.001);

		GL11.glVertex3d(minX + 0.5, minY, maxZ + 0.001);
		GL11.glVertex3d(minX + 0.5, maxY, maxZ + 0.001);
		//end

		//west
		GL11.glVertex3d(minX - 0.001, minY + 0.5, minZ);
		GL11.glVertex3d(minX - 0.001, minY + 0.5, maxZ);

		GL11.glVertex3d(minX - 0.001, minY, minZ + 0.5);
		GL11.glVertex3d(minX - 0.001, maxY, minZ + 0.5);
		//end

		//bottom
		GL11.glVertex3d(minX, minY - 0.001, minZ + 0.5);
		GL11.glVertex3d(maxX, minY - 0.001, minZ + 0.5);

		GL11.glVertex3d(minX + 0.5, minY, minZ);
		GL11.glVertex3d(minX + 0.5, minY, maxZ);
		//end

		GL11.glEnd();
	}

	private static void xes(AxisAlignedBB aabb) {
		double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ;
		double maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
		GL11.glBegin(1);
		//top
		GL11.glVertex3d(minX, maxY + 0.001, minZ);
		GL11.glVertex3d(maxX, maxY + 0.001, maxZ);

		GL11.glVertex3d(minX, maxY + 0.001, maxZ);
		GL11.glVertex3d(maxX, maxY + 0.001, minZ);
		//end

		//north
		GL11.glVertex3d(maxX, maxY, minZ - 0.001);
		GL11.glVertex3d(minX, minY, minZ - 0.001);

		GL11.glVertex3d(maxX, minY, minZ - 0.001);
		GL11.glVertex3d(minX, maxY, minZ - 0.001);
		//end north

		//east
		GL11.glVertex3d(maxX + 0.001, maxY, maxZ);
		GL11.glVertex3d(maxX + 0.001, minY, minZ);

		GL11.glVertex3d(maxX + 0.001, maxY, minZ);
		GL11.glVertex3d(maxX + 0.001, minY, maxZ);
		//end east

		//south
		GL11.glVertex3d(minX, maxY, maxZ + 0.001);
		GL11.glVertex3d(maxX, minY, maxZ + 0.001);

		GL11.glVertex3d(minX, minY, maxZ + 0.001);
		GL11.glVertex3d(maxX, maxY, maxZ + 0.001);
		//end

		//west
		GL11.glVertex3d(minX - 0.001, maxY, minZ);
		GL11.glVertex3d(minX - 0.001, minY, maxZ);

		GL11.glVertex3d(minX - 0.001, minY, minZ);
		GL11.glVertex3d(minX - 0.001, maxY, maxZ);
		//end

		//bottom
		GL11.glVertex3d(minX, minY - 0.001, minZ);
		GL11.glVertex3d(maxX, minY - 0.001, maxZ);

		GL11.glVertex3d(minX, minY - 0.001, maxZ);
		GL11.glVertex3d(maxX, minY - 0.001, minZ);
		//end

		GL11.glEnd();
	}


}
