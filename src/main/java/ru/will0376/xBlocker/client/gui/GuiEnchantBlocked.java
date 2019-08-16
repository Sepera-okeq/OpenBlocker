package ru.will0376.xBlocker.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.client.gui.renders.RenderUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiEnchantBlocked extends GuiScreen {
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
			case 3:
			default:
				break;
			case 4:
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
		try {
			this.drawDefaultBackground();
			boolean var1 = true;
//      this.drawCenteredString(fontRenderer, "Список запрещённой магии на сервере:", super.width / 2, 13, 16777215);
			this.drawCenteredString(fontRenderer, I18n.format("guienchant.allist"), super.width / 2, 13, 16777215);
			ScaledResolution scaledResolution = new ScaledResolution(this.minecraft);
			this.scaledWidth = scaledResolution.getScaledWidth();
			this.scaledHeight = scaledResolution.getScaledHeight();
			byte k1 = 115;
			int itemsInPage = (this.scaledWidth - 35 - 35) * k1 / 1024;
			this.xCoord = 0;
			this.yCoord = 38;

			if (Main.getInstance().listEnchantClient.size() == 0) {
//         this.drawCenteredString(fontRenderer, "Вся магия на сервере разрешена.", super.width / 2, 35, 16777215);
				this.drawCenteredString(fontRenderer, I18n.format("guienchant.allist.empty"), super.width / 2, 35, 16777215);
			}

			if (Main.getInstance().listEnchantClient != null && !Main.getInstance().listEnchantClient.isEmpty() && Main.getInstance().listEnchantClient.size() != 0) {
				ItemStack bEnch2 = new ItemStack(Items.ENCHANTED_BOOK, 1);

				int b2, id, k, lvl;
				for (b2 = 0; b2 < Main.getInstance().listEnchantClient.size(); ++b2) {
					for (k = 1; k < 50; ++k) {
						if (k == 1) {
							this.maxPages = 1;
							if (this.page == 1 && b2 >= 0 && b2 < itemsInPage) {
								this.moveCoord(this.xCoord, this.yCoord);
								if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
									RenderUtils.renderItemEnable(this.xCoord, this.yCoord, 32, 32);
								}
							}
						} else {
							if (b2 >= itemsInPage * (k - 1) && b2 < itemsInPage * k) {
								this.maxPages = k;
							}

							if (this.page == k && b2 >= itemsInPage * (k - 1) && b2 < itemsInPage * k) {
								this.moveCoord(this.xCoord, this.yCoord);
								if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
									RenderUtils.renderItemEnable(this.xCoord, this.yCoord, 32, 32);
								}
							}
						}
					}
				}

				this.xCoord = 0;
				this.yCoord = 38;

				for (b2 = 0; b2 < Main.getInstance().listEnchantClient.size(); ++b2) {
					for (k = 1; k < 50; ++k) {
						if (k == 1) {
							this.maxPages = 1;
							if (this.page == 1 && b2 >= 0 && b2 < itemsInPage) {
								this.moveCoord(this.xCoord, this.yCoord);
								GL11.glPushMatrix();
								GL11.glTranslatef((float) this.xCoord, (float) this.yCoord, 1.5F);
								GL11.glScalef(1.5F, 1.5F, 1.5F);
								this.itemRender.renderItemAndEffectIntoGUI(bEnch2, 3, 5);
								GL11.glDisable(2896);
								GL11.glPopMatrix();
							}
						} else {
							if (b2 >= itemsInPage * (k - 1) && b2 < itemsInPage * k) {
								this.maxPages = k;
							}

							if (this.page == k && b2 >= itemsInPage * (k - 1) && b2 < itemsInPage * k) {
								this.moveCoord(this.xCoord, this.yCoord);
								GL11.glPushMatrix();
								GL11.glTranslatef((float) this.xCoord, (float) this.yCoord, 1.5F);
								GL11.glScalef(2.0F, 2.0F, 1.5F);
								this.itemRender.renderItemAndEffectIntoGUI(bEnch2, 3, 5);
								GL11.glDisable(2896);
								GL11.glPopMatrix();
							}
						}
					}
				}

				this.xCoord = 0;
				this.yCoord = 38;

				for (b2 = 0; b2 < Main.getInstance().listEnchantClient.size(); ++b2) {
					id = Integer.valueOf(Main.getInstance().listEnchantClient.get(b2).split(":")[0]);
					lvl = Integer.valueOf(Main.getInstance().listEnchantClient.get(b2).split(":")[1]);

					for (k = 1; k < 50; ++k) {
						ArrayList list;
						String name;
						if (k == 1) {
							this.maxPages = 1;
							if (this.page == 1 && b2 >= 0 && b2 < itemsInPage) {
								this.moveCoord(this.xCoord, this.yCoord);
								list = Lists.newArrayList();
								if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
									if (id < 256) {
//                           list.add(TextFormatting.UNDERLINE+""+TextFormatting.BOLD+""+TextFormatting.ITALIC+"Зачарование: " + Enchantment.getEnchantmentByID(id).getTranslatedName(lvl));
										list.add(I18n.format("guienchant.list.add1", Enchantment.getEnchantmentByID(id).getTranslatedName(lvl)));
									}

									renderTooltip(mouseX + 3, mouseY - 8, list);
								}
							}
						} else {
							if (b2 >= itemsInPage * (k - 1) && b2 < itemsInPage * k) {
								this.maxPages = k;
							}

							if (this.page == k && b2 >= itemsInPage * (k - 1) && b2 < itemsInPage * k) {
								this.moveCoord(this.xCoord, this.yCoord);
								list = Lists.newArrayList();
								if (mouseX >= this.xCoord && mouseX < this.xCoord + 30 && mouseY >= this.yCoord && mouseY < this.yCoord + 34) {
									if (id < 256) {
										list.add(I18n.format("guienchant.list.add1", Enchantment.getEnchantmentByID(id).getTranslatedName(lvl)));
									}

									renderTooltip(mouseX + 3, mouseY - 8, list);
								}
							}
						}
					}
				}
			}

			super.buttonList.clear();
			GuiButtonBlocked var14;
			if (this.maxPages != 0) {
//         var14 = new GuiButtonBlocked(1, super.width / 2 - 70, super.height - 45, 18, 20, "\u00a7c<");
				var14 = new GuiButtonBlocked(1, super.width / 2 - 70, super.height - 45, 18, 20, I18n.format("gui.button.left"));
//         GuiButtonBlocked var15 = new GuiButtonBlocked(2, super.width / 2 + 50, super.height - 45, 18, 20, "\u00a7c>");
				GuiButtonBlocked var15 = new GuiButtonBlocked(2, super.width / 2 + 50, super.height - 45, 18, 20, I18n.format("gui.button.right"));
				var15.enabled = this.page != this.maxPages;

				var14.enabled = this.page != 1 && this.page != 0;

				super.buttonList.add(var14);
				super.buttonList.add(var15);
				this.drawCenteredString(super.fontRenderer, I18n.format("gui.page.of", page, maxPages), super.width / 2, super.height - 40, 16777215);
			}

//      var14 = new GuiButtonBlocked(4, super.width / 2 + 50 + 20, super.height - 45, 18, 20, "\u00a7b>>");
			var14 = new GuiButtonBlocked(4, super.width / 2 + 50 + 20, super.height - 45, 18, 20, I18n.format("gui.button.right.right"));
			super.buttonList.add(var14);
			super.drawScreen(mouseX, mouseY, partialTicks);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
				font.drawStringWithShadow(s1, var15 - 2, k2, -1);
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
