package ru.will0376.xBlocker.common.hooks;

import com.google.common.collect.Lists;
import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.HookPriority;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.RecipeBookServer;

import java.util.List;

public class Hooks {
	@Hook(priority = HookPriority.HIGHEST, returnCondition = ReturnCondition.ALWAYS)
	public static int getRecipeId(RecipeBook rb, IRecipe recipe) {
		try {
			int ret = CraftingManager.REGISTRY.getIDForObject(recipe);
			if (ret == -1) {
				ret = ((net.minecraftforge.registries.ForgeRegistry<IRecipe>) net.minecraftforge.fml.common.registry.ForgeRegistries.RECIPES).getID(recipe.getRegistryName());
				if (ret == -1) {
					return 1;

				}
			}
			return ret;
		} catch (Exception e) {
			return 1;
		}
	}

	@Hook(priority = HookPriority.HIGHEST, returnCondition = ReturnCondition.ALWAYS)
	public static void add(RecipeBookServer rbs, List<IRecipe> recipesIn, EntityPlayerMP player) {
		try {
			List<IRecipe> list = Lists.newArrayList();

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

	@Hook(priority = HookPriority.HIGHEST, returnCondition = ReturnCondition.ALWAYS)
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
						if (nbt != null)
							nbttaglist.appendTag(nbt);
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
						if (nbt != null)
							nbttaglist1.appendTag(nbt);
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
