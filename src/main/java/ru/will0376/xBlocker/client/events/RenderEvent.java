package ru.will0376.xBlocker.client.events;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.client.gui.GuiColor;

public class RenderEvent {
	Minecraft mc = Minecraft.getMinecraft();

	public Item getItem(World w, BlockPos pos) {
		return Item.getItemFromBlock(w.getBlockState(pos).getBlock());
	}

	public ItemStack getPickBlock(World world, BlockPos pos) {
		try {
			Item item = this.getItem(world, pos);
			if (item == null) {
				return ItemStack.EMPTY;
			} else {
				Block block = ((Block) (item instanceof ItemBlock ? Block.getBlockFromItem(item) : this));
				return new ItemStack(item, 1, block.getMetaFromState(world.getBlockState(pos)));
			}
		}catch (Exception e){return ItemStack.EMPTY;}
	}

	@SubscribeEvent
	public void selectHighlight(DrawBlockHighlightEvent event) {
		try {
			if(event.getTarget() != null)
				if (event.getPlayer().getEntityWorld().getBlockState(event.getTarget().getBlockPos()).getBlock() != Blocks.AIR)
					if (Main.getInstance().listItemsClient.size() != 0 && !Main.getInstance().listItemsClient.isEmpty())
						for (int i = 0; i < Main.getInstance().listItemsClient.size(); ++i) {
							String[] check = Main.getInstance().listItemsClient.get(i).split(":");
							Block blockFromName = Block.getBlockFromName(check[0] + ":" + check[1]);
							ItemStack item = getPickBlock(event.getPlayer().getEntityWorld(),event.getTarget().getBlockPos());
							Block targetblock = event.getPlayer().getEntityWorld().getBlockState(event.getTarget().getBlockPos()).getBlock();

							if ( targetblock == blockFromName && item.getMetadata() == Integer.parseInt(check[2].split("@")[0]))
								this.render(event);
						}

/*				if (Main.getInstance().listItemsClient.size() != 0 && !Main.getInstance().listItemsClient.isEmpty()) {
					for (int i = 0; i < Main.getInstance().listItemsClient.size(); ++i) {
						String all = Main.getInstance().listItemsClient.get(i);
						if (all != null && all.contains(":")) {
							String[] check = all.split(":");
							String name = check[0] + ":" + check[1];
							int meta = Integer.parseInt(check[2].split("@")[0]);
							Block blockFromName = Block.getBlockFromName(name);
							Block targetblock = event.getPlayer().getEntityWorld().getBlockState(new BlockPos(event.getTarget().getBlockPos().getX(), event.getTarget().getBlockPos().getY(), event.getTarget().getBlockPos().getZ())).getBlock();
							ItemStack item = this.getPickBlock(event.getPlayer().getEntityWorld(),event.getTarget().getBlockPos());
							if (targetblock != Blocks.AIR && targetblock == blockFromName && item != null && item.getMetadata() == meta) {
								this.render(event);
							}

						}
					}
				}*/
		}
		catch (Exception e) {
		}

	}

	public void render(DrawBlockHighlightEvent event) {
		double ax = (double) event.getTarget().getBlockPos().getX();
		double ay = (double) event.getTarget().getBlockPos().getY();
		double az = (double) event.getTarget().getBlockPos().getZ();
		double bx = (double) (event.getTarget().getBlockPos().getX() + 1);
		double by = (double) (event.getTarget().getBlockPos().getY() + 1);
		double bz = (double) (event.getTarget().getBlockPos().getZ() + 1);
		AxisAlignedBB aabb = new AxisAlignedBB(ax, ay, az, bx, by, bz).expand(0.01, 0.01, 0.01);
		double x_fix = -(this.mc.player.lastTickPosX + (this.mc.player.posX - this.mc.player.lastTickPosX) * (double) event.getPartialTicks());
		double y_fix = -(this.mc.player.lastTickPosY + (this.mc.player.posY - this.mc.player.lastTickPosY) * (double) event.getPartialTicks());
		double z_fix = -(this.mc.player.lastTickPosZ + (this.mc.player.posZ - this.mc.player.lastTickPosZ) * (double) event.getPartialTicks());
		GL11.glPushMatrix();
		GL11.glTranslated(x_fix, y_fix, z_fix);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glColor4f(GuiColor.RValue, GuiColor.GValue, GuiColor.BValue, GuiColor.AValue);
	//	box(aabb);
		GL11.glColor4f(GuiColor.RValue, GuiColor.GValue, GuiColor.BValue, 1.75F);
		lines(aabb);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.0F);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}

	public static void box(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		int[] tmp = {(int) aabb.maxX, (int) aabb.maxY, (int) aabb.minZ};
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		bufferBuilder.addVertexData(tmp);
		tessellator.draw();
	}

	public static void lines(AxisAlignedBB aabb) {
		Double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ;
		Double maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
		GL11.glBegin(1);				// max   min
											//  x	  n
/*		System.out.println("minX: " + aabb.minX);
		System.out.println("maxX: " + aabb.maxX);
		System.out.println("minY: " + aabb.minY);
		System.out.println("maxY: " + aabb.maxY);
		System.out.println("minZ: " + aabb.minZ);
		System.out.println("maxZ: " + aabb.maxZ);

		[19:15:16] [Client thread/INFO] [STDOUT]: [ru.will0376.xBlocker.client.events.RenderEvent:lines:142]: minX: 31.0
		[19:15:16] [Client thread/INFO] [STDOUT]: [ru.will0376.xBlocker.client.events.RenderEvent:lines:143]: maxX: 32.0
		[19:15:16] [Client thread/INFO] [STDOUT]: [ru.will0376.xBlocker.client.events.RenderEvent:lines:144]: minY: 101.0
		[19:15:16] [Client thread/INFO] [STDOUT]: [ru.will0376.xBlocker.client.events.RenderEvent:lines:145]: maxY: 102.0
		[19:15:16] [Client thread/INFO] [STDOUT]: [ru.will0376.xBlocker.client.events.RenderEvent:lines:146]: minZ: 102.0
		[19:15:17] [Client thread/INFO] [STDOUT]: [ru.will0376.xBlocker.client.events.RenderEvent:lines:147]: maxZ: 103.0
		*/
		//top
		GL11.glVertex3d(minX, maxY, minZ); //nxn
		GL11.glVertex3d(maxX, maxY, minZ); //xxn

		GL11.glVertex3d(maxX, maxY, minZ); //xxn
		GL11.glVertex3d(maxX, maxY, maxZ); //xxx

		GL11.glVertex3d(maxX, maxY, maxZ); //xxx
		GL11.glVertex3d(minX, maxY, maxZ); //nxx

		GL11.glVertex3d(minX, maxY, maxZ); //nxx
		GL11.glVertex3d(minX, maxY, minZ); //nxn

		GL11.glVertex3d(minX, maxY, minZ); //nxn
		GL11.glVertex3d(maxX , maxY, maxZ); //xxx

		GL11.glVertex3d(minX, maxY, maxZ); //nxx
		GL11.glVertex3d(maxX, maxY, minZ); //xxn
		//end top
		//top lines
		GL11.glVertex3d(minX, maxY, minZ); //
		GL11.glVertex3d(minX, minY, minZ); //
		//end top lines
/*

		GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ); //nnn
		GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ); //xnn

		GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ); //xnn
		GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ); //xnx

		GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ); //xnx
		GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ); //nnx

		GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ); //nnx
		GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ); //nnn

		GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ); //nnn
		GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ); //nxn
		GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ); //nxn
		//GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ); //xnx

		GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ); //xxx

		GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ); //xxn
		GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ); //xxx

		*//*GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ); //nnx
		GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ); //nxx*/
		GL11.glEnd();
	}
}
