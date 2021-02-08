package ru.will0376.OpenBlocker.common.hooks;

import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.HookPriority;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.recipebook.GuiButtonRecipe;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.RecipeBookServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.justagod.cutter.invoke.Invoke;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;

import java.util.ArrayList;
import java.util.List;

import static gloomyfolken.hooklib.asm.ReturnCondition.ALWAYS;

public class Hooks {
	@Hook(priority = HookPriority.HIGHEST, returnCondition = ALWAYS)
	public static int getRecipeId(RecipeBook rb, IRecipe recipe) {
		try {
			int ret = CraftingManager.REGISTRY.getIDForObject(recipe);
			if (ret == -1) {
				ret = ((net.minecraftforge.registries.ForgeRegistry<IRecipe>) net.minecraftforge.fml.common.registry.ForgeRegistries.RECIPES)
						.getID(recipe.getRegistryName());
				if (ret == -1) {
					return 1;
				}
			}
			return ret;
		} catch (Exception e) {
			return 1;
		}
	}

	@GradleSideOnly(GradleSide.SERVER)
	@Hook(injectOnExit = true, returnCondition = ReturnCondition.ALWAYS)
	public static boolean matches(ShapelessRecipes recipes, InventoryCrafting inv, World worldIn, @Hook.ReturnValue boolean returnValue) {
		return Invoke.serverValue(() -> {
			Blocked blockedByStack = BlockHelper.findBlockedByStack(recipes.getRecipeOutput());
			return (blockedByStack != null && blockedByStack.getStatus().contains(Blocked.Status.Craft)) && returnValue;
		});
	}

	@GradleSideOnly(GradleSide.SERVER)
	@Hook(injectOnExit = true, returnCondition = ReturnCondition.ALWAYS)
	public static boolean matches(ShapedRecipes recipes, InventoryCrafting inv, World worldIn, @Hook.ReturnValue boolean returnValue) {
		return Invoke.serverValue(() -> {
			Blocked blockedByStack = BlockHelper.findBlockedByStack(recipes.getRecipeOutput());
			return (blockedByStack != null && blockedByStack.getStatus().contains(Blocked.Status.Craft)) && returnValue;
		});
	}


	@GradleSideOnly(GradleSide.CLIENT)
	@Hook(priority = HookPriority.HIGHEST, returnCondition = ALWAYS)
	public static void drawButton(GuiButtonRecipe br, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		try {
			if (br.visible) {
				if (!GuiScreen.isCtrlKeyDown()) {
					br.time += partialTicks;
				}

				br.hovered = mouseX >= br.x && mouseY >= br.y && mouseX < br.x + br.width && mouseY < br.y + br.height;
				RenderHelper.enableGUIStandardItemLighting();
				mc.getTextureManager().bindTexture(GuiButtonRecipe.RECIPE_BOOK);
				GlStateManager.disableLighting();
				int i = 29;

				if (!br.list.containsCraftableRecipes()) {
					i += 25;
				}

				int j = 206;

				if (br.list.getRecipes(br.book.isFilteringCraftable()).size() > 1) {
					j += 25;
				}

				boolean flag = br.animationTime > 0.0F;

				if (flag) {
					float f = 1.0F + 0.1F * (float) Math.sin(br.animationTime / 15.0F * (float) Math.PI);
					GlStateManager.pushMatrix();
					GlStateManager.translate((float) (br.x + 8), (float) (br.y + 12), 0.0F);
					GlStateManager.scale(f, f, 1.0F);
					GlStateManager.translate((float) (-(br.x + 8)), (float) (-(br.y + 12)), 0.0F);
					br.animationTime -= partialTicks;
				}

				br.drawTexturedModalRect(br.x, br.y, i, j, br.width, br.height);
				List<IRecipe> list = br.getOrderedRecipes();
				br.currentIndex = MathHelper.floor(br.time / 30.0F) % list.size();
				ItemStack itemstack = list.get(br.currentIndex).getRecipeOutput();
				int k = 4;

				if (br.list.hasSingleResultItem() && br.getOrderedRecipes().size() > 1) {
					mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, br.x + k + 1, br.y + k + 1);
					--k;
				}

				mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, br.x + k, br.y + k);

				if (flag) {
					GlStateManager.popMatrix();
				}

				GlStateManager.enableLighting();
				RenderHelper.disableStandardItemLighting();
			}
		} catch (Exception ignore) {
		}

	}

	@Hook(priority = HookPriority.HIGHEST, returnCondition = ALWAYS)
	public static void add(RecipeBookServer rbs, List<IRecipe> recipesIn, EntityPlayerMP player) {
		try {
			List<IRecipe> list = new ArrayList<>();

			for (IRecipe irecipe : recipesIn) {
				if (!rbs.recipes.get(RecipeBook.getRecipeId(irecipe)) && !irecipe.isDynamic()) {
					rbs.unlock(irecipe);
					rbs.markNew(irecipe);
					list.add(irecipe);
					CriteriaTriggers.RECIPE_UNLOCKED.trigger(player, irecipe);
				}
			}

			rbs.sendPacket(SPacketRecipeBook.State.ADD, player, list);
		} catch (Exception ignore) {
		}
	}

	@Hook(priority = HookPriority.HIGHEST, returnCondition = ALWAYS)
	public static NBTTagCompound write(RecipeBookServer rbs) {
		try {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setBoolean("isGuiOpen", rbs.isGuiOpen);
			nbttagcompound.setBoolean("isFilteringCraftable", rbs.isFilteringCraftable);
			NBTTagList nbttaglist = new NBTTagList();

			for (IRecipe irecipe : rbs.getRecipes()) {
				if (irecipe != null) {
					String tmp = CraftingManager.REGISTRY.getNameForObject(irecipe).toString();
					if (tmp != null) {
						NBTTagString nbt = new NBTTagString(tmp);
						if (nbt != null) nbttaglist.appendTag(nbt);
					}
				}
			}

			nbttagcompound.setTag("recipes", nbttaglist);
			NBTTagList nbttaglist1 = new NBTTagList();

			for (IRecipe irecipe1 : rbs.getDisplayedRecipes()) {
				if (irecipe1 != null) {
					String tmp = CraftingManager.REGISTRY.getNameForObject(irecipe1).toString();
					if (tmp != null) {
						NBTTagString nbt = new NBTTagString(tmp);
						if (nbt != null) nbttaglist1.appendTag(nbt);
					}
				}
			}

			nbttagcompound.setTag("toBeDisplayed", nbttaglist1);
			return nbttagcompound;
		} catch (Exception e) {
			return new NBTTagCompound();
		}
	}
}
