package ru.will0376.OpenBlocker.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiBlocker extends GuiScreen {
	private int page = 0, localpage = 1;
	private int maxPages = 1;
	private int xCoord = 0, yCoord = 38, scaledWidth = 0;

	private ArrayList<ItemsBlocks> blockslist = new ArrayList<>();

	public void initGui() {
		blockslist.clear();
		buttonList.clear();
		localpage = 1;
		buttonList.add(new GuiButton(1, super.width / 2 - 70, super.height - 45, 18, 20, "<"));
		buttonList.add(new GuiButton(2, super.width / 2 + 50, super.height - 45, 18, 20, ">"));

		buttonList.add(new GuiButton(3, super.width / 2 - 90, super.height - 45, 18, 20, "<<"));
		buttonList.add(new GuiButton(4, super.width / 2 + 50 + 20, super.height - 45, 18, 20, ">>"));


		if (ItemsBlocks.ib != null)
			switch (page) {
				case 0:
					blockslist = (ArrayList<ItemsBlocks>) ItemsBlocks.ib.clone();
					break;
				case 1:
					ItemsBlocks.ib.stream().filter(i -> i.blocked).forEach(blockslist::add);
					break;
				case 2:
					ItemsBlocks.ib.stream().filter(i -> i.allmeta).forEach(blockslist::add);
					break;
				case 3:
					ItemsBlocks.ib.stream().filter(i -> i.limitb).forEach(blockslist::add);
					break;
				case 4:
					ItemsBlocks.ib.stream().filter(i -> i.mincostb).forEach(blockslist::add);
					break;
				case 5:
					ItemsBlocks.ib.stream().filter(i -> i.craft).forEach(blockslist::add);
					break;
			}
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		scaledWidth = scaledResolution.getScaledWidth();
	}

	protected void actionPerformed(GuiButton g) {
		switch (g.id) {
			case 1:
				if (this.localpage != 1)
					--this.localpage;
				break;
			case 2:
				if (this.localpage != this.maxPages)
					++this.localpage;
				break;
			case 3:
				if (this.page != 0) {
					--this.page;
					mc.displayGuiScreen(this);
				}
				break;
			case 4:
				if (this.page != 5) {
					++this.page;
					mc.displayGuiScreen(this);
				}
				break;
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawBlocks(mouseX, mouseY);
		RenderHelper.enableGUIStandardItemLighting();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawBlocks(int mouseX, int mouseY) {
		switch (page) {
			case 0:
				GuiHelper.drawScalledString((int) (width / 2 - ("All".length() * 1.5f)), 13, 1.5f, 1.5f, "All", -1);
				break;

			case 1:
				GuiHelper.drawScalledString((int) (width / 2 - ("Block".length() * 1.5f)), 13, 1.5f, 1.5f, "Block", -1);
				break;

			case 2:
				GuiHelper.drawScalledString((int) (width / 2 - ("All Meta".length() * 1.5f)), 13, 1.5f, 1.5f, "All Meta", -1);
				break;

			case 3:
				GuiHelper.drawScalledString((int) (width / 2 - ("Limit".length() * 1.5f)), 13, 1.5f, 1.5f, "Limit", -1);
				break;

			case 4:
				GuiHelper.drawScalledString((int) (width / 2 - ("Min Cost".length() * 1.5f)), 13, 1.5f, 1.5f, "Min Cost", -1);
				break;

			case 5:
				GuiHelper.drawScalledString((int) (width / 2 - ("Craft".length() * 1.5f)), 13, 1.5f, 1.5f, "Craft", -1);
				break;
		}
		RenderHelper.enableGUIStandardItemLighting();
		ItemsBlocks tmpib = null;
		int itemsInPage = (this.scaledWidth - 35 - 35) * (byte) 115 / 1024;
		int offset = 0;
		ArrayList<String> list;
		if (!blockslist.isEmpty()) {
			for (ItemsBlocks ib : blockslist) {
				for (int k = 1; k < 50; ++k) {
					if (k == 1) {
						this.maxPages = 1;
						if (this.localpage == 1 && offset >= 0 && offset < itemsInPage) {
							this.moveCoord();
							if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
								tmpib = ib;
								RenderUtils.renderItemEnable(this.xCoord, this.yCoord, 32, 32);
							}
						}
					} else {
						if (offset >= itemsInPage * (k - 1) && offset < itemsInPage * k) {
							this.maxPages = k;
						}

						if (this.localpage == k && offset >= itemsInPage * (k - 1) && offset < itemsInPage * k) {
							this.moveCoord();
							if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
								tmpib = ib;
								RenderUtils.renderItemEnable(this.xCoord, this.yCoord, 32, 32);

							}
						}
					}
				}
				test(itemsInPage, offset, ib.is);
				offset++;
			}
			this.yCoord = 38;
			this.xCoord = 0;
			if (tmpib != null) {
				list = new ArrayList<>();
				list.add(I18n.format("guiblocker.blockname", tmpib.is.getDisplayName()));
				list.addAll(tmpib.getLore());
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
					list.add("NBT: " + tmpib.nbt);
				renderTooltip(mouseX + 3, mouseY - 8, list);
			}
		}
		drawCenteredString(super.fontRenderer, I18n.format("guiblocker.page.of", localpage, maxPages), super.width / 2, super.height - 40, 16777215);
	}

	private void test(int itemsInPage, int bEnch1, ItemStack is) {
		int k;
		for (k = 1; k < 50; ++k) {
			if (k == 1) {
				this.maxPages = 1;
				if (this.localpage == 1 && bEnch1 >= 0 && bEnch1 < itemsInPage) {
					GlStateManager.pushMatrix();
					GlStateManager.translate((float) this.xCoord, (float) this.yCoord, 1.5F);
					GlStateManager.scale(2.0F, 2.0F, 1.5F);
					itemRender.renderItemAndEffectIntoGUI(is, 0, 0);
					GlStateManager.popMatrix();
				}
			} else {
				if (bEnch1 >= itemsInPage * (k - 1) && bEnch1 < itemsInPage * k) {
					this.maxPages = k;
				}

				if (this.localpage == k && bEnch1 >= itemsInPage * (k - 1) && bEnch1 < itemsInPage * k) {
					GlStateManager.pushMatrix();
					GlStateManager.translate((float) this.xCoord, (float) this.yCoord, 1.5F);
					GlStateManager.scale(2.0F, 2.0F, 1.5F);
					itemRender.renderItemAndEffectIntoGUI(is, 0, 0);
					GlStateManager.popMatrix();
				}
			}
		}
	}

	public void moveCoord() {
		if (this.xCoord + 70 >= scaledWidth - 35) {
			this.xCoord = 35;
			this.yCoord += 38;
		} else {
			this.xCoord += 35;
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
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableDepth();
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
			GlStateManager.enableDepth();
			GlStateManager.enableRescaleNormal();
		}
	}

}
