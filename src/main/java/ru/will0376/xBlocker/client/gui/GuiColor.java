package ru.will0376.xBlocker.client.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import ru.will0376.xBlocker.client.gui.renders.RenderUtils;

public class GuiColor extends GuiScreen {
	private int field_146445_a;
	private Minecraft minecraft = Minecraft.getMinecraft();
	public static Float RValue = 1.0F;
	public static Float GValue = 0.0F;
	public static Float BValue = 0.0F;
	public static Float AValue = 1.0F;

	public void initGui() {
		this.field_146445_a = 0;
		super.buttonList.clear();
		super.buttonList.add(new GuiSliderAdv(0, 10, 10, "Красный: ", RValue));
		super.buttonList.add(new GuiSliderAdv(1, 10, 34, "Зеленый: ", GValue));
		super.buttonList.add(new GuiSliderAdv(2, 10, 58, "Синий: ", BValue));
		super.buttonList.add(new GuiSliderAdv(3, 10, 82, "Прозрачность: ", 1.0F - AValue));
	}

	protected void mouseClickMove(int par1, int par2, int par3, long par4) {
		super.mouseClickMove(par1, par2, par3, par4);
		RValue = ((GuiSliderAdv) super.buttonList.get(0)).sliderValue;
		GValue = ((GuiSliderAdv) super.buttonList.get(1)).sliderValue;
		BValue = ((GuiSliderAdv) super.buttonList.get(2)).sliderValue;
		AValue = 1.0F - ((GuiSliderAdv) super.buttonList.get(3)).sliderValue;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution scaledResolution = new ScaledResolution(this.minecraft);
		int scaledWidth = scaledResolution.getScaledWidth();
		int scaledHeight = scaledResolution.getScaledHeight();
		RenderUtils.renderColor(0, 0, scaledWidth, scaledHeight, RValue, GValue, BValue, AValue);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
