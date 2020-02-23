package ru.will0376.OpenBlocker.client.events;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.will0376.OpenBlocker.client.ItemsBlocks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
					if (check(is, false))
						render(e);
				}
		} catch (Exception ignore) {
		}
	}

	@SubscribeEvent
	public static void dravToolTips(ItemTooltipEvent e) {
		try {
			ItemStack is = e.getItemStack();
			if (check(is, true)) {
				e.getToolTip().addAll(getIB(is).getLore());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static boolean check(ItemStack is, boolean checkNonBlocks) {
		AtomicBoolean ret = new AtomicBoolean(false);
		ItemsBlocks.ib.forEach(l -> {
			if (l.is.isItemEqual(is) && !ret.get() && l.blocked)
				ret.set(true);
			else if (checkNonBlocks && l.is.isItemEqual(is) && !ret.get())
				ret.set(true);
		});
		return ret.get();
	}

	private static ItemsBlocks getIB(ItemStack is) {
		AtomicReference<ItemsBlocks> ret = new AtomicReference();
		ItemsBlocks.ib.forEach(l -> {
			if (l.is.isItemEqual(is) && ret.get() == null)
				ret.set(l);
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
		AxisAlignedBB aabb = new AxisAlignedBB(ax, ay, az, bx, by, bz).expand(0.02, 0.02, 0.02);
		double x_fix = -(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double) event.getPartialTicks());
		double y_fix = -(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double) event.getPartialTicks());
		double z_fix = -(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double) event.getPartialTicks());
		GlStateManager.pushMatrix();
		GlStateManager.translate(x_fix, y_fix, z_fix);
		GlStateManager.disableTexture2D();
		GlStateManager.color(1, 0, 0, 1.75F);
		xes(aabb);
		pluses(aabb);
		GlStateManager.color(1, 1, 1, 0);
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

	private static void pluses(AxisAlignedBB aabb) {
		float minX = (float) aabb.minX;
		float minY = (float) aabb.minY;
		float minZ = (float) aabb.minZ;

		float maxX = (float) aabb.maxX;
		float maxY = (float) aabb.maxY;
		float maxZ = (float) aabb.maxZ;
		GlStateManager.glBegin(1);
		//top
		GlStateManager.glVertex3f(minX + 0.5f, maxY + 0.001f, minZ);
		GlStateManager.glVertex3f(minX + 0.5f, maxY + 0.001f, maxZ);

		GlStateManager.glVertex3f(minX, maxY + 0.001f, minZ + 0.5f);
		GlStateManager.glVertex3f(maxX, maxY + 0.001f, minZ + 0.5f);
		//end

		//north
		GlStateManager.glVertex3f(minX, minY + 0.5f, minZ - 0.001f);
		GlStateManager.glVertex3f(maxX, minY + 0.5f, minZ - 0.001f);

		GlStateManager.glVertex3f(minX + 0.5f, minY, minZ - 0.001f);
		GlStateManager.glVertex3f(minX + 0.5f, maxY, minZ - 0.001f);
		//end

		//east
		GlStateManager.glVertex3f(maxX + 0.001f, minY + 0.5f, minZ);
		GlStateManager.glVertex3f(maxX + 0.001f, minY + 0.5f, maxZ);

		GlStateManager.glVertex3f(maxX + 0.001f, minY, minZ + 0.5f);
		GlStateManager.glVertex3f(maxX + 0.001f, maxY, minZ + 0.5f);
		//end

		//south
		GlStateManager.glVertex3f(minX, minY + 0.5f, maxZ + 0.001f);
		GlStateManager.glVertex3f(maxX, minY + 0.5f, maxZ + 0.001f);

		GlStateManager.glVertex3f(minX + 0.5f, minY, maxZ + 0.001f);
		GlStateManager.glVertex3f(minX + 0.5f, maxY, maxZ + 0.001f);
		//end

		//west
		GlStateManager.glVertex3f(minX - 0.001f, minY + 0.5f, minZ);
		GlStateManager.glVertex3f(minX - 0.001f, minY + 0.5f, maxZ);

		GlStateManager.glVertex3f(minX - 0.001f, minY, minZ + 0.5f);
		GlStateManager.glVertex3f(minX - 0.001f, maxY, minZ + 0.5f);
		//end

		//bottom
		GlStateManager.glVertex3f(minX, minY - 0.001f, minZ + 0.5f);
		GlStateManager.glVertex3f(maxX, minY - 0.001f, minZ + 0.5f);

		GlStateManager.glVertex3f(minX + 0.5f, minY, minZ);
		GlStateManager.glVertex3f(minX + 0.5f, minY, maxZ);
		//end
		GlStateManager.glEnd();
	}

	private static void xes(AxisAlignedBB aabb) {
		float minX = (float) aabb.minX;
		float minY = (float) aabb.minY;
		float minZ = (float) aabb.minZ;

		float maxX = (float) aabb.maxX;
		float maxY = (float) aabb.maxY;
		float maxZ = (float) aabb.maxZ;
		GlStateManager.glBegin(1);
		//top
		GlStateManager.glVertex3f(minX, maxY + 0.001f, minZ);
		GlStateManager.glVertex3f(maxX, maxY + 0.001f, maxZ);

		GlStateManager.glVertex3f(minX, maxY + 0.001f, maxZ);
		GlStateManager.glVertex3f(maxX, maxY + 0.001f, minZ);
		//end

		//north
		GlStateManager.glVertex3f(maxX, maxY, minZ - 0.001f);
		GlStateManager.glVertex3f(minX, minY, minZ - 0.001f);

		GlStateManager.glVertex3f(maxX, minY, minZ - 0.001f);
		GlStateManager.glVertex3f(minX, maxY, minZ - 0.001f);
		//end north

		//east
		GlStateManager.glVertex3f(maxX + 0.001f, maxY, maxZ);
		GlStateManager.glVertex3f(maxX + 0.001f, minY, minZ);

		GlStateManager.glVertex3f(maxX + 0.001f, maxY, minZ);
		GlStateManager.glVertex3f(maxX + 0.001f, minY, maxZ);
		//end east

		//south
		GlStateManager.glVertex3f(minX, maxY, maxZ + 0.001f);
		GlStateManager.glVertex3f(maxX, minY, maxZ + 0.001f);

		GlStateManager.glVertex3f(minX, minY, maxZ + 0.001f);
		GlStateManager.glVertex3f(maxX, maxY, maxZ + 0.001f);
		//end

		//west
		GlStateManager.glVertex3f(minX - 0.001f, maxY, minZ);
		GlStateManager.glVertex3f(minX - 0.001f, minY, maxZ);

		GlStateManager.glVertex3f(minX - 0.001f, minY, minZ);
		GlStateManager.glVertex3f(minX - 0.001f, maxY, maxZ);
		//end

		//bottom
		GlStateManager.glVertex3f(minX, minY - 0.001f, minZ);
		GlStateManager.glVertex3f(maxX, minY - 0.001f, maxZ);

		GlStateManager.glVertex3f(minX, minY - 0.001f, maxZ);
		GlStateManager.glVertex3f(maxX, minY - 0.001f, minZ);
		//end

		GlStateManager.glEnd();
	}
}
