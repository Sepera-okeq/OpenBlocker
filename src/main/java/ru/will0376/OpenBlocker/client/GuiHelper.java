package ru.will0376.OpenBlocker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.will0376.OpenBlocker.Main;

public class GuiHelper {
	public static int xySize = 176;
	public static String username = Minecraft.getMinecraft().getSession().getUsername();

	public static void bindTexture(String path) {
		bindTexture(Main.MODID, path);
	}

	public static void bindTexture(String modId, String path) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(2896);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(modId + ":" + path));
	}

	public static void drawTopLine(int x, int y) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(0, y, 0).tex(0, 1).endVertex(); //нижний-левый
		bufferbuilder.pos(x, y, 0).tex(1, 1).endVertex();//нижний-правый
		bufferbuilder.pos(x, 0, 0).tex(1, 0).endVertex();//верхний-правый
		bufferbuilder.pos(0, 0, 0).tex(0, 0).endVertex();//верхний-левый
		tessellator.draw();
	}

	public static void drawTextured(int x, int y) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + (176 + 20), 0).tex(0, 1).endVertex(); //нижний-левый
		bufferbuilder.pos(x + 400, y + (176 + 20), 0).tex(1, 1).endVertex();//нижний-правый
		bufferbuilder.pos(x + 400, y - 20, 0).tex(1, 0).endVertex();//верхний-правый
		bufferbuilder.pos(x, y - 20, 0).tex(0, 0).endVertex();//верхний-левый
		tessellator.draw();
	}

	public static void cleanRenderCentered(double x, double y, double x2, double y2, int zLevel) {
		cleanRender(x - (x2 / 2), y - (y2 / 2), x2, y2, zLevel);
	}

	public static void cleanRender(double x, double y, double x2, double y2, int zLevel) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((x + 0), (y + y2), zLevel).tex(0, 1).endVertex();
		bufferbuilder.pos((x + x2), (y + y2), zLevel).tex(1, 1).endVertex();
		bufferbuilder.pos((x + x2), (y + 0), zLevel).tex(1, 0).endVertex();
		bufferbuilder.pos((x + 0), (y + 0), zLevel).tex(0, 0).endVertex();
		tessellator.draw();
	}


	public static void renderSkinHead(int x, int y) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0F);
		GL11.glScalef(4.0F, 4.0F, 0.0F);
		Gui.drawScaledCustomSizeModalRect(0, 0, 8.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
		GL11.glPopMatrix();
	}

	public static void renderBlocks(int x, int y, ItemStack it, float scalledX, float scalledY) {
		renderBlocks(x, y, it, scalledX, scalledY, 0);
	}

	public static void renderBlocks(int x, int y, ItemStack it, float scalledX, float scalledY, float Z) {
		if (scalledX == 0)
			scalledX = 1;
		if (scalledY == 0)
			scalledY = 1;
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, Z);
		GL11.glScalef(scalledX, scalledY, 0.0F);
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(it, 0, 0);
		GL11.glPopMatrix();
	}

	public static void drawScalledString(int x, int y, float scalledX, float scalledY, String string, int color) {
		if (color == -1)
			color = 16777215;
		if (scalledX != 1.0f || scalledY != 1.0f) {
			GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0.0F);
			GL11.glScalef(scalledX, scalledY, 0.0F);
			new Gui().drawString(Minecraft.getMinecraft().fontRenderer, string, 0, 0, color); //price
			GL11.glPopMatrix();
		} else new Gui().drawString(Minecraft.getMinecraft().fontRenderer, string, x, y, color);
	}
}
