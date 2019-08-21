package ru.will0376.xBlocker.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.client.gui.renders.RenderUtils;
import ru.will0376.xBlocker.common.BaseUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiMiniBlockedBlocked extends GuiScreen {
	private int field_146445_a;
	private Minecraft minecraft = Minecraft.getMinecraft();
	int scaledWidth;
	int scaledHeight;
	int maxPages = 1;
	int page = 1;
	int xCoord = 0;
	int yCoord = 38;
	int line = 1;
	int itemsInLine = 1;

	public void initGui() {
		this.field_146445_a = 0;
	}

	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
			case 1:
				if (this.page != 1) {
					--this.page;
				}
				break;
			case 2:
				if (this.page != this.maxPages) {
					++this.page;
				}
				break;
			case 3:
				this.minecraft.displayGuiScreen(new GuiListItemBlocked());
		}

	}

	public void updateScreen() {
		super.updateScreen();
	}

	public void moveCoord(int xCoord, int yCoord) {
		if (this.xCoord + 70 >= this.scaledWidth - 35) {
			this.xCoord = 35;
			this.yCoord += 38;
		} else {
			this.xCoord += 35;
		}

	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		boolean var1 = true;
		this.drawCenteredString(fontRenderer, I18n.format("guiminiblock.allist"), super.width / 2, 13, 16777215);
		ScaledResolution scaledResolution = new ScaledResolution(this.minecraft);
		this.scaledWidth = scaledResolution.getScaledWidth();
		this.scaledHeight = scaledResolution.getScaledHeight();
		byte k1 = 115;
		int itemsInPage = (this.scaledWidth - 35 - 35) * k1 / 1024;
		this.xCoord = 0;
		this.yCoord = 38;
		if (Main.getInstance().listLimitedClient.size() == 0)
			this.drawCenteredString(fontRenderer, I18n.format("guiminiblock.allist.empty"), super.width / 2, 35, 16777215);


		if (Main.getInstance().listLimitedClient != null) {
			RenderHelper.enableGUIStandardItemLighting();
			int bEnch1;
			String b2;
			String[] name;
			String blockname;
			int meta;
			ItemStack is;
			int k;
			for (bEnch1 = 0; bEnch1 < Main.getInstance().listLimitedClient.size(); ++bEnch1) {
				b2 = Main.getInstance().listLimitedClient.get(bEnch1);
				if (b2 != null && b2.contains(":")) {
					name = Main.getInstance().listLimitedClient.get(bEnch1).split(":");
					blockname = name[0] + ":" + name[1];
					meta = Integer.parseInt(name[2]);
					boolean allmeta = false;
					if(meta == -99){
						meta = 0;
					}
					is = new ItemStack(Item.getByNameOrId(blockname), 1, meta);
					if (is != null && Item.getByNameOrId(blockname) == is.getItem() && is.getMetadata() == meta) {
						for (k = 1; k < 50; ++k) {
							if (k == 1) {
								this.maxPages = 1;
								if (this.page == 1 && bEnch1 >= 0 && bEnch1 < itemsInPage) {
									this.moveCoord(this.xCoord, this.yCoord);
									if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
										RenderUtils.renderItemEnable(this.xCoord, this.yCoord, 32, 32);
									}
								}
							} else {
								if (bEnch1 >= itemsInPage * (k - 1) && bEnch1 < itemsInPage * k) {
									this.maxPages = k;
								}

								if (this.page == k && bEnch1 >= itemsInPage * (k - 1) && bEnch1 < itemsInPage * k) {
									this.moveCoord(this.xCoord, this.yCoord);
									if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
										RenderUtils.renderItemEnable(this.xCoord, this.yCoord, 32, 32);
									}
								}
							}
						}
					}
				}
			}

			this.xCoord = 0;
			this.yCoord = 38;
			this.itemsInLine = 1;

			for (bEnch1 = 0; bEnch1 < Main.getInstance().listLimitedClient.size(); ++bEnch1) {
				b2 = Main.getInstance().listLimitedClient.get(bEnch1);
				if (b2 != null && b2.contains(":")) {
					name = Main.getInstance().listLimitedClient.get(bEnch1).split(":");
					blockname = name[0] + ":" + name[1];
					meta = Integer.parseInt(name[2]);
					if(meta == -99){
						meta = 0;
					}
					is = new ItemStack(Item.getByNameOrId(blockname), 1, meta);
					if (is != null && Item.getByNameOrId(blockname) == is.getItem() && is.getMetadata() == meta) {
						for (k = 1; k < 50; ++k) {
							if (k == 1) {
								this.maxPages = 1;
								if (this.page == 1 && bEnch1 >= 0 && bEnch1 < itemsInPage) {
									this.moveCoord(this.xCoord, this.yCoord);
									GL11.glPushMatrix();
									GL11.glTranslatef((float) this.xCoord, (float) this.yCoord, 1.5F);
									GL11.glScalef(2.0F, 2.0F, 1.5F);
									this.itemRender.renderItemAndEffectIntoGUI(is, 0, 0);
									GL11.glDisable(2896);
									GL11.glPopMatrix();
								}
							} else {
								if (bEnch1 >= itemsInPage * (k - 1) && bEnch1 < itemsInPage * k) {
									this.maxPages = k;
								}

								if (this.page == k && bEnch1 >= itemsInPage * (k - 1) && bEnch1 < itemsInPage * k) {
									this.moveCoord(this.xCoord, this.yCoord);
									GL11.glPushMatrix();
									GL11.glTranslatef((float) this.xCoord, (float) this.yCoord, 1.5F);
									GL11.glScalef(2.0F, 2.0F, 1.5F);
									this.itemRender.renderItemAndEffectIntoGUI(is, 0, 0);
									GL11.glDisable(2896);
									GL11.glPopMatrix();
								}
							}
						}
					}
				}
			}

			this.xCoord = 0;
			this.yCoord = 38;
			this.itemsInLine = 0;

			for (bEnch1 = 0; bEnch1 < Main.getInstance().listLimitedClient.size(); ++bEnch1) {
				b2 = Main.getInstance().listLimitedClient.get(bEnch1);
				if (b2 != null && b2.contains(":")) {
					name = Main.getInstance().listLimitedClient.get(bEnch1).split(":");
					blockname = name[0] + ":" + name[1];
					meta = Integer.parseInt(name[2]);
					if(meta == -99){
						meta = 0;
					}
					ArrayList list;
					is = new ItemStack(Item.getByNameOrId(blockname), 1, meta);
					if (is != null && Item.getByNameOrId(blockname) == is.getItem() && is.getMetadata() == meta) {
						for (k = 1; k < 50; ++k) {
							if (k == 1) {
								this.maxPages = 1;
								if (this.page == 1 && bEnch1 >= 0 && bEnch1 < itemsInPage) {
									this.moveCoord(this.xCoord, this.yCoord);
									list = Lists.newArrayList();
									if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
										list.add(I18n.format("guiminiblock.list.add1", is.getDisplayName()));
										list.add(I18n.format("guiminiblock.list.add2", name[3]));
										list.add(TextFormatting.ITALIC + BaseUtils.lat2cyr(name[4].replaceAll("&", "§").replaceAll("_", " ")));
										//  list.add(TextFormatting.UNDERLINE+""+TextFormatting.ITALIC+"[Ограничено] " + is.getDisplayName());
										//   list.add("Доступнось: " + name[3]);
										this.renderTooltip(mouseX + 3, mouseY - 8, list);
									}
								}
							} else {
								if (bEnch1 >= itemsInPage * (k - 1) && bEnch1 < itemsInPage * k) {
									this.maxPages = k;
								}

								if (this.page == k && bEnch1 >= itemsInPage * (k - 1) && bEnch1 < itemsInPage * k) {
									this.moveCoord(this.xCoord, this.yCoord);
									list = Lists.newArrayList();
									if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
										list.add(I18n.format("guiminiblock.list.add1", is.getDisplayName()));
										list.add(I18n.format("guiminiblock.list.add2", name[3]));
										list.add(TextFormatting.ITALIC + BaseUtils.lat2cyr(name[4].replaceAll("&", "§").replaceAll("_", " ")));
										this.renderTooltip(mouseX + 3, mouseY - 8, list);
									}
								}
							}
						}
					}
				}
			}
		}

		super.buttonList.clear();
		GuiButtonBlocked var16;
		if (this.maxPages != 0) {
//         var16 = new GuiButtonBlocked(1, super.width / 2 - 70, super.height - 45, 18, 20, "\u00a7c<");
			var16 = new GuiButtonBlocked(1, super.width / 2 - 70, super.height - 45, 18, 20, I18n.format("gui.button.left"));
//         GuiButtonBlocked var17 = new GuiButtonBlocked(2, super.width / 2 + 50, super.height - 45, 18, 20, "\u00a7c>");
			GuiButtonBlocked var17 = new GuiButtonBlocked(2, super.width / 2 + 50, super.height - 45, 18, 20, I18n.format("gui.button.right"));
			var17.enabled = this.page != this.maxPages;

			var16.enabled = this.page != 1 && this.page != 0;

			super.buttonList.add(var16);
			super.buttonList.add(var17);
//         this.drawCenteredString(super.fontRenderer, "Страница " + this.page + " из " + this.maxPages, super.width / 2, super.height - 40, 16777215);
			this.drawCenteredString(super.fontRenderer, I18n.format("gui.page.of", page, maxPages), super.width / 2, super.height - 40, 16777215);
		}

//      var16 = new GuiButtonBlocked(3, super.width / 2 - 90, super.height - 45, 18, 20, "\u00a7b<<");
		var16 = new GuiButtonBlocked(3, super.width / 2 - 90, super.height - 45, 18, 20, I18n.format("gui.button.left.left"));
		super.buttonList.add(var16);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void renderTooltip(int xCoord, int yCoord, List list) {
		this.drawHoveringTexts(list, xCoord - 5, yCoord, super.fontRenderer);
	}

	protected void drawHoveringTexts(List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font) {
		if (!p_146283_1_.isEmpty()) {
			GL11.glDisable(32826);
			GL11.glDisable(2929);
			int k = 0;
			Iterator iterator = p_146283_1_.iterator();

			int k2;
			while (iterator.hasNext()) {
				String j2 = (String) iterator.next();
				k2 = font.getStringWidth(j2);
				if (k2 > k) {
					k = k2;
				}
			}

			int var15 = p_146283_2_ + 12;
			k2 = p_146283_3_ - 12;
			int i1 = 8;
			if (p_146283_1_.size() > 1) {
				i1 += 2 + (p_146283_1_.size() - 1) * 10;
			}

			if (var15 + k > super.width) {
				var15 -= 28 + k;
			}

			if (k2 + i1 + 6 > super.height) {
				k2 = super.height - i1 - 6;
			}

			super.zLevel = 300.0F;
			this.itemRender.zLevel = 300F;
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

			for (int i2 = 0; i2 < p_146283_1_.size(); ++i2) {
				String s1 = (String) p_146283_1_.get(i2);
				font.drawStringWithShadow(s1, var15, k2, -1);
				if (i2 == 0) {
					k2 += 2;
				}

				k2 += 10;
			}

			super.zLevel = 0.0F;
			this.itemRender.zLevel = 0F;
			GL11.glEnable(2929);
			GL11.glEnable(32826);
		}

	}
}
