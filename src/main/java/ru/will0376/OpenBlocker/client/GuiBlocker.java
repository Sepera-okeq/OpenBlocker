package ru.will0376.OpenBlocker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.client.utils.GuiHelper;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;
import ru.will0376.OpenBlocker.common.utils.B64;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuiBlocker extends GuiScreen {
	private int page = 0, localpage = 1;
	private int maxPages = 1;
	int itemsX, itemsY, itemsInPage;
	private List<Blocked> blockslist = new ArrayList<>();

	public void initGui() {
		blockslist.clear();
		buttonList.clear();
		localpage = 1;
		buttonList.add(new GuiButton(1, super.width / 2 - 70, super.height - 45, 18, 20, "<"));
		buttonList.add(new GuiButton(2, super.width / 2 + 50, super.height - 45, 18, 20, ">"));

		buttonList.add(new GuiButton(3, super.width / 2 - 90, super.height - 45, 18, 20, "<<"));
		buttonList.add(new GuiButton(4, super.width / 2 + 50 + 20, super.height - 45, 18, 20, ">>"));


		switch (page) {
			case 0:
				blockslist = new ArrayList<>(BlockHelper.BlockHelperClient.blockedListClient);
				break;
			case 1:
				blockslist = BlockHelper.getAllByStatus(Blocked.Status.Blocked);
				break;
			case 2:
				blockslist = BlockHelper.getAllByStatus(Blocked.Status.Limit);
				break;
			case 3:
				blockslist = BlockHelper.getAllByStatus(Blocked.Status.Craft);
				break;

		}
		itemsX = (this.width - 70) / 38;
		itemsY = (this.height - 76) / 38;
		itemsInPage = itemsX * itemsY;
		maxPages = (int) Math.ceil((double) blockslist.size() / itemsInPage);
	}

	protected void actionPerformed(GuiButton g) {
		switch (g.id) {
			case 1:
				if (this.localpage != 1) --this.localpage;
				break;
			case 2:
				if (this.localpage != this.maxPages) ++this.localpage;
				break;
			case 3:
				if (this.page != 0) {
					--this.page;
					mc.displayGuiScreen(this);
				}
				break;
			case 4:
				if (this.page != 3) {
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
				GuiHelper.drawScalledString((int) (width / 2 - ("Limit".length() * 1.5f)), 13, 1.5f, 1.5f, "Limit", -1);
				break;
			case 3:
				GuiHelper.drawScalledString((int) (width / 2 - ("Craft".length() * 1.5f)), 13, 1.5f, 1.5f, "Craft", -1);
				break;

		}
		try {
			RenderHelper.enableGUIStandardItemLighting();

			int startedIndex = (localpage - 1) * itemsInPage;
			int endIndex = (localpage) * itemsInPage;
			if (Main.debug)
				drawString(Minecraft.getMinecraft().fontRenderer, String.format("x: %s, y: %s, all: %s, page: %s", itemsX, itemsY, itemsInPage, localpage), 0, 0, Color.white
						.getRGB());

			Optional<Render> render = Optional.empty();
			if (!blockslist.isEmpty()) {
				for (Blocked blocked : blockslist) {

					int index = blockslist.indexOf(blocked) + 1;

					if (index < startedIndex) continue;
					if (index > endIndex) continue;

					int nowX = (index * 38) - ((index - 1) / itemsX * (itemsX * 38));
					int nowY = 38 + ((index - 1 - startedIndex) / itemsX) * 38;

					ItemStack itemStack = !blocked.NBTEmpty() ? new ItemStack(JsonToNBT.getTagFromJson(B64.decode(blocked
							.getNbt()))) : blocked.getStack();

					GlStateManager.pushMatrix();
					GlStateManager.translate(nowX, nowY, 1.5F);
					GlStateManager.scale(2.0F, 2.0F, 1.5F);
					itemRender.renderItemAndEffectIntoGUI(itemStack, 0, 0);
					GlStateManager.popMatrix();

					if (mouseX >= nowX && mouseX < nowX + 34 && mouseY >= nowY && mouseY < nowY + 34) {
						render = Optional.of(() -> {
							List<String> list = new ArrayList<>();
							list.add(I18n.format("guiblocker.blockname", itemStack.getDisplayName()));
							list.addAll(blocked.getLore());
							if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
								list.add(TextFormatting.BOLD + "_______________");
								list.add("NBT: " + blocked.getNbt());
							}
							renderTooltip(mouseX + 3, mouseY - 8, list);
						});
					}
				}
				render.ifPresent(Render::onRender);
			}
			drawCenteredString(super.fontRenderer, I18n.format("guiblocker.page.of", localpage, maxPages), super.width / 2, super.height - 40, 16777215);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void renderTooltip(int xCoord, int yCoord, List<String> list) {
		this.drawHoveringTexts(list, xCoord - 5, yCoord, fontRenderer);
	}

	protected void drawHoveringTexts(List<String> list, int xCoord, int yCoord, FontRenderer font) {
		if (!list.isEmpty()) {
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableDepth();
			int k = 0;
			int k2;
			for (String s : list) {
				k2 = font.getStringWidth(s);
				if (k2 > k) k = k2;
			}

			int var15 = xCoord + 12;
			k2 = yCoord - 12;
			int i1 = 8;
			if (list.size() > 1) i1 += 2 + (list.size() - 1) * 10;

			if (var15 + k > super.width) var15 -= 28 + k;

			if (k2 + i1 + 6 > super.height) k2 = super.height - i1 - 6;


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
				String s1 = list.get(i2);
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

	public interface Render {
		void onRender();
	}
}
