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

							if ( targetblock == blockFromName && (item.getMetadata() == Integer.parseInt(check[2].split("@")[0]) || Integer.parseInt(check[2].split("@")[0]) == -99))
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
		GL11.glColor4f(GuiColor.RValue, GuiColor.GValue, GuiColor.BValue, 1.75F);
		xes(aabb);
		pluses(aabb);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.0F);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}
	private static void pluses(AxisAlignedBB aabb){
		double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ;
		double maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
		GL11.glBegin(1);
	//top
		GL11.glVertex3d(minX+0.5, maxY+0.001, minZ);
		GL11.glVertex3d(minX+0.5, maxY+0.001, maxZ);

		GL11.glVertex3d(minX, maxY+0.001, minZ+0.5);
		GL11.glVertex3d(maxX, maxY+0.001, minZ+0.5);
	//end

	//north
		GL11.glVertex3d(minX, minY+0.5, minZ-0.001);
		GL11.glVertex3d(maxX, minY+0.5, minZ-0.001);

		GL11.glVertex3d(minX+0.5, minY, minZ-0.001);
		GL11.glVertex3d(minX+0.5, maxY, minZ-0.001);
	//end

	//east
		GL11.glVertex3d(maxX+0.001, minY+0.5, minZ);
		GL11.glVertex3d(maxX+0.001, minY+0.5, maxZ);

		GL11.glVertex3d(maxX+0.001, minY, minZ+0.5);
		GL11.glVertex3d(maxX+0.001, maxY, minZ+0.5);
	//end

	//south
		GL11.glVertex3d(minX, minY+0.5, maxZ+0.001);
		GL11.glVertex3d(maxX, minY+0.5, maxZ+0.001);

		GL11.glVertex3d(minX+0.5, minY, maxZ+0.001);
		GL11.glVertex3d(minX+0.5, maxY, maxZ+0.001);
	//end

	//west
		GL11.glVertex3d(minX-0.001, minY+0.5, minZ);
		GL11.glVertex3d(minX-0.001, minY+0.5, maxZ);

		GL11.glVertex3d(minX-0.001, minY, minZ+0.5);
		GL11.glVertex3d(minX-0.001, maxY, minZ+0.5);
	//end

	//bottom
		GL11.glVertex3d(minX, minY-0.001, minZ+0.5);
		GL11.glVertex3d(maxX, minY-0.001, minZ+0.5);

		GL11.glVertex3d(minX+0.5, minY, minZ);
		GL11.glVertex3d(minX+0.5, minY, maxZ);
	//end

		GL11.glEnd();
	}
	/*public static void box(AxisAlignedBB aabb) {
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
	}*/
	private static void xes(AxisAlignedBB aabb){
		double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ;
		double maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
		GL11.glBegin(1);
	//top
		GL11.glVertex3d(minX, maxY+0.001, minZ);
		GL11.glVertex3d(maxX, maxY+0.001, maxZ);

		GL11.glVertex3d(minX, maxY+0.001, maxZ);
		GL11.glVertex3d(maxX, maxY+0.001, minZ);
	//end

	//north
		GL11.glVertex3d(maxX, maxY, minZ-0.001);
		GL11.glVertex3d(minX, minY, minZ-0.001);

		GL11.glVertex3d(maxX, minY, minZ-0.001);
		GL11.glVertex3d(minX, maxY, minZ-0.001);
	//end north

	//east
		GL11.glVertex3d(maxX+0.001, maxY, maxZ);
		GL11.glVertex3d(maxX+0.001, minY, minZ);

		GL11.glVertex3d(maxX+0.001, maxY, minZ);
		GL11.glVertex3d(maxX+0.001, minY, maxZ);
	//end east

	//south
		GL11.glVertex3d(minX, maxY, maxZ+0.001);
		GL11.glVertex3d(maxX, minY, maxZ+0.001);

		GL11.glVertex3d(minX, minY, maxZ+0.001);
		GL11.glVertex3d(maxX, maxY, maxZ+0.001);
	//end

	//west
		GL11.glVertex3d(minX-0.001, maxY, minZ);
		GL11.glVertex3d(minX-0.001, minY, maxZ);

		GL11.glVertex3d(minX-0.001, minY, minZ);
		GL11.glVertex3d(minX-0.001, maxY, maxZ);
	//end

	//bottom
		GL11.glVertex3d(minX, minY-0.001, minZ);
		GL11.glVertex3d(maxX, minY-0.001, maxZ);

		GL11.glVertex3d(minX, minY-0.001, maxZ);
		GL11.glVertex3d(maxX, minY-0.001, minZ);
	//end

		GL11.glEnd();
	}

}
