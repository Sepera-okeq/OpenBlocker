package ru.will0376.OpenBlocker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.will0376.OpenBlocker.Main;

public class RenderUtils {
	public static void renderItemEnable(int xCoord, int yCoord, int i, int j) {
		int w = i + 4;
		int h = j + 4;
		int x = xCoord - 2;
		int y = yCoord - 2;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		ResourceLocation skin = new ResourceLocation(Main.MODID, "textures/gui/wasted.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
		GL11.glEnable(3042);
		GL11.glColor4d(1.0D, 1.0D, 1.0D, Math.min(Math.min(0.5D, 1.0D), 1.0D));
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + h, 0).tex(0, 1).endVertex(); //нижний-левый
		bufferbuilder.pos(x + w, y + h, 0).tex(1, 1).endVertex();//нижний-правый
		bufferbuilder.pos(x + w, y, 0).tex(1, 0).endVertex();//верхний-правый
		bufferbuilder.pos(x, y, 0).tex(0, 0).endVertex();//верхний-левый
		tessellator.draw();
		GL11.glDisable(3042);

	}

	public static void renderColor(int xCoord, int yCoord, int i, int j, float r, float g, float b, float a) {
		int w = i + 4;
		int h = j + 4;
		int x = xCoord - 2;
		int y = yCoord - 2;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		ResourceLocation skin = new ResourceLocation(Main.MODID, "textures/gui/wasted.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
		GL11.glEnable(3042);
		GL11.glColor4d(r, g, b, a);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + h, 0).tex(0, 1).endVertex(); //нижний-левый
		bufferbuilder.pos(x + w, y + h, 0).tex(1, 1).endVertex();//нижний-правый
		bufferbuilder.pos(x + w, 0, 0).tex(1, 0).endVertex();//верхний-правый
		bufferbuilder.pos(x, y, 0).tex(0, 0).endVertex();//верхний-левый
		tessellator.draw();
		GL11.glDisable(3042);
	}

}
