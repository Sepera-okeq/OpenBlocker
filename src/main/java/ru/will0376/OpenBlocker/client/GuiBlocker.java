package ru.will0376.OpenBlocker.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiBlocker extends GuiScreen {
	private static int scrollMax = 130;
	private static int scrolloffset = 1;
	private int scrollPos = 0; //up to 93
	private boolean isScrollPressed = false;
	private int pane = 0;
	private ArrayList<ItemsBlocks> list = new ArrayList<>();

	public void initGui() {
		list.clear();
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 60, height / 2 + 70, 20, 20, "<"));
		buttonList.add(new GuiButton(1, width / 2 + 50, height / 2 + 70, 20, 20, ">"));

		scrollPos = 0;
		if (ItemsBlocks.ib != null)
			switch (pane) {
				case 0:
					list = (ArrayList<ItemsBlocks>) ItemsBlocks.ib.clone();
					break;
				case 1:
					ItemsBlocks.ib.stream().filter(i -> i.blocked).forEach(list::add);
					break;
				case 2:
					ItemsBlocks.ib.stream().filter(i -> i.allmeta).forEach(list::add);
					break;
				case 3:
					ItemsBlocks.ib.stream().filter(i -> i.limitb).forEach(list::add);
					break;
				case 4:
					ItemsBlocks.ib.stream().filter(i -> i.mincostb).forEach(list::add);
					break;
				case 5:
					ItemsBlocks.ib.stream().filter(i -> i.craft).forEach(list::add);
					break;
			}
	}

	protected void actionPerformed(GuiButton g) {

		switch (g.id) {
			case 0: {
				if (pane != 0) {
					pane -= 1;
					mc.displayGuiScreen(this);
				}
				break;
			}
			case 1: {
				if (pane != 5) {
					pane += 1;
					mc.displayGuiScreen(this);
				}
				break;
			}
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawGUIBackground();
		drawScroll(mouseY);
		drawBlocks(mouseX, mouseY);
		GlStateManager.disableBlend();

		if (!isMouseOverArea(mouseX, mouseY, width / 2 + 110, height / 2 - 80 + scrollPos, 12, 108))
			isScrollPressed = false;
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawBlocks(int mouseX, int mouseY) {
		switch (pane) {
			case 0:
				GuiHelper.drawScalledString((int) (width / 2 - ("All".length() * 1.5f)), height / 2 - 98, 1.5f, 1.5f, "All", -1);
				break;

			case 1:
				GuiHelper.drawScalledString((int) (width / 2 - ("Block".length() * 1.5f)), height / 2 - 98, 1.5f, 1.5f, "Block", -1);
				break;

			case 2:
				GuiHelper.drawScalledString((int) (width / 2 - ("All Meta".length() * 1.5f)), height / 2 - 98, 1.5f, 1.5f, "All Meta", -1);
				break;

			case 3:
				GuiHelper.drawScalledString((int) (width / 2 - ("Limit".length() * 1.5f)), height / 2 - 98, 1.5f, 1.5f, "Limit", -1);
				break;

			case 4:
				GuiHelper.drawScalledString((int) (width / 2 - ("Min Cost".length() * 1.5f)), height / 2 - 98, 1.5f, 1.5f, "Min Cost", -1);
				break;

			case 5:
				GuiHelper.drawScalledString((int) (width / 2 - ("Craft".length() * 1.5f)), height / 2 - 98, 1.5f, 1.5f, "Craft", -1);
				break;
		}

		AtomicInteger offset = new AtomicInteger(0);
		RenderHelper.enableStandardItemLighting();
		scrollMax = (list.size() / 4) + 10;
		scrolloffset = (list.size() / 4) * 2;
		AtomicBoolean skip = new AtomicBoolean(false);
		AtomicBoolean skipRow = new AtomicBoolean(false);
		list.forEach(block -> {
			int index = list.indexOf(block);
			float numberlist = ((float) index / 4) - (index / 4);
			int temp = offset.get() / 4;
			if (list.size() < 15)
				isScrollPressed = false;
			if (((height / 2 - 80 - (scrollPos * scrolloffset) + (temp * 39)) > (height / 2 - 100)
					&& (height / 2 - 80 - (scrollPos * scrolloffset) + (temp * 39)) < height / 2 + 50)
					|| list.size() < 15) {
				if (!skip.getAndSet(false) && !skipRow.get()) {
					ItemStack is = block.is.copy();
					if (!block.nbt.isEmpty()) {
						is = new ItemStack(block.nbt);
					}
					GuiHelper.renderBlocks((int) (width / 2 - 100 + (numberlist * 230)), (height / 2 - 80 - (scrollPos * scrolloffset) + (temp * 39)), is, 1.8f, 1.8f, 0);
				}
				if (numberlist == 0.75)
					skipRow.set(false);
				if (isMouseOverArea(mouseX, mouseY, (int) (width / 2 - 100 + (numberlist * 230)), (height / 2 - 80 - (scrollPos * scrolloffset) + (temp * 39)), 25, 25)) {
					ArrayList<String> list = new ArrayList<>();
					list.add("-> " + block.is.getDisplayName());
					list.add("Full name: ");
					list.add(block.is.getItem().getRegistryName().toString() + ":" + block.is.getMetadata());
					if (block.blocked) list.add("Blocked: true");
					if (block.allmeta) list.add("Block all meta: true");
					if (block.limitb && block.limit != -1) list.add("Block limit on chunk: " + block.limit);
					if (block.mincostb) list.add("Minimum cost: " + block.mincost);
					if (block.craft) list.add("Block craft: true");

					if (!block.nbt.isEmpty()) {
						list.add("NBT: " + block.nbt);
					}
					int tmp = list.stream().mapToInt(String::length).filter(l -> l >= 0).max().orElse(0);
					if (tmp > 80) {
						skipRow.set(true);
					}
					renderTooltip((int) (width / 2 - 100 + (numberlist * 230)), (height / 2 - 80 - scrollPos + (temp * 39)), list);
					if (numberlist != 0.75)
						skip.set(true);
				}
			}
			offset.getAndIncrement();
		});
		RenderHelper.disableStandardItemLighting();
	}

	private void drawGUIBackground() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiHelper.bindTexture("textures/gui/bg2.png");
		GuiHelper.cleanRenderCentered(width / 2, height / 2, 250, 200, 0);
		GuiHelper.bindTexture("textures/gui/bg2inside.png");
		GuiHelper.cleanRenderCentered(width / 2, height / 2 - 8, 226, 160, 0);

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
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
/*		if (button != 0)
			return;*/
		if (button == 0)
			isScrollPressed = isMouseOverArea(mouseX, mouseY, width / 2 + 110, height / 2 - 80 + scrollPos, 12, 108);
		super.mouseClicked(mouseX, mouseY, button);
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

	/*@Override
	protected void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
		net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(textLines, x, y, width, height, -1, font);
		if (false && !textLines.isEmpty()) {
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int i = 0;

			for (String s : textLines) {
				int j = this.fontRenderer.getStringWidth(s);

				if (j > i) {
					i = j;
				}
			}

			int l1 = x + 12;
			int i2 = y - 12;
			int k = 8;

			if (textLines.size() > 1) {
				k += 2 + (textLines.size() - 1) * 10;
			}

			if (l1 + i > this.width) {
				l1 -= 28 + i;
			}

			if (i2 + k + 6 > this.height) {
				i2 = this.height - k - 6;
			}

			this.zLevel = 400.0F;
			this.itemRender.zLevel = 299.0F;
			int l = -267386864;
			this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
			this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
			this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
			this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
			this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
			int i1 = 1347420415;
			int j1 = 1344798847;
			this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
			this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
			this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
			this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);

			for (int k1 = 0; k1 < textLines.size(); ++k1) {
				String s1 = textLines.get(k1);
				this.fontRenderer.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

				if (k1 == 0) {
					i2 += 2;
				}

				i2 += 10;
			}

			this.zLevel = 400.0F;
			this.itemRender.zLevel = 0.0F;
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
		}
	}*/
	private void renderTooltip(int xCoord, int yCoord, List list) {
		this.drawHoveringTexts(list, xCoord - 5, yCoord, fontRenderer);
	}

	protected void drawHoveringTexts(List list, int xCoord, int yCoord, FontRenderer font) {
		if (!list.isEmpty()) {
			GL11.glDisable(32826);
			GL11.glDisable(2929);
			int k = 0;
			Iterator iterator = list.iterator();

			int k2;
			while (iterator.hasNext()) {
				String j2 = (String) iterator.next();
				k2 = font.getStringWidth(j2);
				if (k2 > k) {
					k = k2;
				}
			}

			int var15 = xCoord + 12;
			k2 = yCoord - 12;
			int i1 = 8;
			if (list.size() > 1) {
				i1 += 2 + (list.size() - 1) * 10;
			}

			if (var15 + k > super.width) {
				var15 -= 28 + k;
			}

			if (k2 + i1 + 6 > super.height) {
				k2 = super.height - i1 - 6;
			}

			super.zLevel = 300.0F;
			int j1 = -267386864;
			this.drawGradientRect(var15 - 3, k2 - 4, var15 + k + 3, k2 - 3, j1, j1);
			this.drawGradientRect(var15 - 3, k2 + i1 + 3, var15 + k + 3, k2 + i1 + 4, j1, j1);
			this.drawGradientRect(var15 - 3, k2 - 3, var15 + k + 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(var15 - 4, k2 - 3, var15 - 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(var15 + k + 3, k2 - 3, var15 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			this.drawGradientRect(var15 - 3, k2 - 3 + 1, var15 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(var15 + k + 2, k2 - 3 + 1, var15 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(var15 - 3, k2 - 3, var15 + k + 3, k2 - 3 + 1, k1, k1);
			this.drawGradientRect(var15 - 3, k2 + i1 + 2, var15 + k + 3, k2 + i1 + 3, l1, l1);

			for (int i2 = 0; i2 < list.size(); ++i2) {
				String s1 = (String) list.get(i2);
				font.drawStringWithShadow(s1, var15, k2, -1);
				if (i2 == 0) {
					k2 += 2;
				}

				k2 += 10;
			}

			super.zLevel = 0.0F;
			GL11.glEnable(2929);
			GL11.glEnable(32826);
		}

	}

}
