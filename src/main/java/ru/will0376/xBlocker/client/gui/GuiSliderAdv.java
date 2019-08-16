package ru.will0376.xBlocker.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class GuiSliderAdv extends GuiButton {
	public Float sliderValue;
	public boolean dragging;
	private String name;

	public GuiSliderAdv(int par1, int par2, int par3, String par5Str, Float par6, int w, int h) {
		super(par1, par2, par3, w, h, par5Str);
		this.sliderValue = par6;
		this.name = par5Str;
	}

	public GuiSliderAdv(int par1, int par2, int par3, String par5Str, Float par6) {
		super(par1, par2, par3, 150, 20, par5Str);
		this.sliderValue = par6;
		this.name = par5Str;
	}

	public int getHoverState(boolean par1) {
		return 0;
	}

	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
		if (this.dragging) {
			this.sliderValue = (float) (par2 - (super.x + 4)) / (float) (super.width - 8);
			if (this.sliderValue < 0.0F) {
				this.sliderValue = 0.0F;
			}

			if (this.sliderValue > 1.0F) {
				this.sliderValue = 1.0F;
			}
		}

		super.displayString = this.name + " " + this.sliderValue * 100.0F + "%";
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(super.x + (int) (this.sliderValue * (float) (super.width - 8)), super.y, 0, 66, 4, 20);
		this.drawTexturedModalRect(super.x + (int) (this.sliderValue * (float) (super.width - 8)) + 4, super.y, 196, 66, 4, 20);
	}

	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		if (super.mousePressed(par1Minecraft, par2, par3)) {
			this.sliderValue = (float) (par2 - (super.x + 4)) / (float) (super.width - 8);
			if (this.sliderValue < 0.0F) {
				this.sliderValue = 0.0F;
			}

			if (this.sliderValue > 1.0F) {
				this.sliderValue = 1.0F;
			}

			super.displayString = this.name + " " + this.sliderValue * 100.0F + "%";
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}

	public void mouseReleased(int par1, int par2) {
		this.dragging = false;
	}
}
