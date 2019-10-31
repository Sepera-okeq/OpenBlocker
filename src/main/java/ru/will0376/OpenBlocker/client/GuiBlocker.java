package ru.will0376.OpenBlocker.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiBlocker extends GuiScreen {
	private static int scrollMax = 130;
	private int scrollPos = 0; //up to 93
	private boolean isScrollPressed = false;
	private int pane = 1;

	public void initGui() {
		buttonList.clear();
		//buttonList.add();
	}

	public void actionPerformed(GuiButton g) {

	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawGUIBackground();
		drawScroll(mouseY);
		drawBlocks();
		GlStateManager.disableBlend();

		if (!isMouseOverArea(mouseX, mouseY, width / 2 + 110, height / 2 - 80 + scrollPos, 12, 108))
			isScrollPressed = false;
	}

	private void drawBlocks() {
		if (pane == 1) {
			GuiHelper.drawScalledString((int) (width / 2 - ("Block".length() * 1.5f)), height / 2 - 98, 1.5f, 1.5f, "Block", -1);
		}
	}

	private void drawGUIBackground() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiHelper.bindTexture("textures/gui/bg2.png");
		GuiHelper.cleanRenderCentered(width / 2, height / 2, 250, 200, 0);

	}

	private void drawScroll(int mouseY) {
		if (isScrollPressed) {
			scrollPos = mouseY - 7 - (height / 2 - 80);
			handleScrollPos();
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiHelper.bindTexture("textures/gui/scroll.png");
		drawTexturedModalRect(width / 2 + 110, height / 2 - 80 + scrollPos, 206, 0, 12, 15);
	}

	private boolean isMouseOverArea(int mouseX, int mouseY, int posX, int posY, int sizeX, int sizeY) {
		return (mouseX >= posX && mouseX < posX + sizeX && mouseY >= posY && mouseY < posY + sizeY);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		if (button != 0)
			return;
		isScrollPressed = isMouseOverArea(mouseX, mouseY, width / 2 + 110, height / 2 - 80 + scrollPos, 12, 108);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int button, long timeSinceLastClick) {
		if (!Mouse.isButtonDown(0))
			isScrollPressed = false;
	}

	private void handleScrollPos() {
		if (scrollPos < 0)
			scrollPos = 0;
		else if (scrollPos > scrollMax)
			scrollPos = scrollMax;
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int wheelState = Mouse.getEventDWheel();
		if (wheelState != 0) {
			scrollPos += wheelState > 0 ? -8 : 8;
			handleScrollPos();
		}
	}
}
