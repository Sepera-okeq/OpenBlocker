package ru.will0376.OpenBlocker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import ru.will0376.OpenBlocker.Main;

public class GuiButtonSome extends GuiButton {
	public ResourceLocation img = null, imgSelected = null;
	int h = 0, w = 0;

	public GuiButtonSome(int buttonId, String path, String selectedPath, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		h = heightIn;
		w = widthIn;
		img = new ResourceLocation(Main.MODID + ":" + path);
		imgSelected = new ResourceLocation(Main.MODID + ":" + selectedPath);
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(img);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			if (this.hovered)
				mc.getTextureManager().bindTexture(imgSelected);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			draw();
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;

			if (packedFGColour != 0) {
				j = packedFGColour;
			} else if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
			}
			if (img == null)
				this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
		}
	}

	private void draw() {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + h, 0).tex(0, 1).endVertex();
		bufferbuilder.pos(x + w, y + h, 0).tex(1, 1).endVertex();
		bufferbuilder.pos(x + w, y, 0).tex(1, 0).endVertex();
		bufferbuilder.pos(x, y, 0).tex(0, 0).endVertex();
		tessellator.draw();
	}
}
