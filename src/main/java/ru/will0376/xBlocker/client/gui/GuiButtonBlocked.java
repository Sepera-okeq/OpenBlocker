package ru.will0376.xBlocker.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.will0376.xBlocker.Main;

public class GuiButtonBlocked extends GuiButton {
	protected static final ResourceLocation buttonTextures = new ResourceLocation(Main.MODID, "textures/gui/widgets.png");

	public GuiButtonBlocked(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_, String p_i1020_4_) {
		this(p_i1020_1_, p_i1020_2_, p_i1020_3_, 200, 20, p_i1020_4_);
	}

	public GuiButtonBlocked(int id, int x, int y, int w, int h, String s) {
		super(id, x, y, w, h, s);
		super.width = 200;
		super.height = 20;
		super.enabled = true;
		super.visible = true;
		super.id = id;
		super.x = x;
		super.y = y;
		super.width = w;
		super.height = h;
		super.displayString = s;
	}

	public void drawButton(Minecraft mc, int p_146112_2_, int p_146112_3_) {
		if (super.visible) {
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(buttonTextures);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			super.hovered = p_146112_2_ >= super.x && p_146112_3_ >= super.y && p_146112_2_ < super.x + super.width && p_146112_3_ < super.y + super.height;
			int k = this.getHoverState(super.hovered);
			GL11.glEnable(3042);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(770, 771);
			this.drawTexturedModalRect(super.x, super.y, 0, 46 + k * 20, super.width / 2, super.height);
			this.drawTexturedModalRect(super.x + super.width / 2, super.y, 200 - super.width / 2, 46 + k * 20, super.width / 2, super.height);
			this.mouseDragged(mc, p_146112_2_, p_146112_3_);
			int l = 14737632;
			if (this.packedFGColour != 0) {
				l = this.packedFGColour;
			} else if (!super.enabled) {
				l = 10526880;
			} else if (super.hovered) {
				l = 16777120;
			}

			this.drawCenteredString(fontrenderer, super.displayString, super.x + super.width / 2, super.y + (super.height - 7) / 2, l);
		}

	}
}
