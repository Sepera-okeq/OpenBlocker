package ru.will0376.OpenBlocker.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
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
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, Math.min(Math.min(0.5F, 1.0F), 1.0F));
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + h, 0).tex(0, 1).endVertex(); //нижний-левый
		bufferbuilder.pos(x + w, y + h, 0).tex(1, 1).endVertex();//нижний-правый
		bufferbuilder.pos(x + w, y, 0).tex(1, 0).endVertex();//верхний-правый
		bufferbuilder.pos(x, y, 0).tex(0, 0).endVertex();//верхний-левый
		tessellator.draw();
		GlStateManager.disableBlend();
	}
}
