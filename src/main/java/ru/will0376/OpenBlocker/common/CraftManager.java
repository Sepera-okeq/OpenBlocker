package ru.will0376.OpenBlocker.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import ru.will0376.OpenBlocker.Main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CraftManager {
	public static ArrayList<CraftPOJO> removedRecipe = new ArrayList<>();

	public static void removeCraftingRecipe(ItemStack removed) {
		try {
			ArrayList<IRecipe> recipes = Lists.newArrayList(ForgeRegistries.RECIPES.getValues());
			for (IRecipe tmp : recipes) {
				if (tmp.getRecipeOutput().getItem() != Items.AIR
						&& !removed.isEmpty()
						&& removed.getItem() == tmp.getRecipeOutput().getItem()
						&& tmp.getRecipeOutput().getMetadata() == removed.getMetadata()) {
					removeCrafting(tmp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void bringBack(ItemStack is) {
		try {
			if (!is.isEmpty() && contains(is)) {
				ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
				CraftPOJO pojo = get(is);
				if (pojo == null) {
					System.err.println(is.getDisplayName() + " in POJO == null");
					return;
				}
				Field namesF = recipeRegistry.getClass().getDeclaredField("names");
				namesF.setAccessible(true);
				BiMap<ResourceLocation, IRecipe> names = (BiMap<ResourceLocation, IRecipe>) namesF.get(recipeRegistry);

				Field idsF = recipeRegistry.getClass().getDeclaredField("ids");
				idsF.setAccessible(true);
				BiMap<Integer, IRecipe> ids = (BiMap<Integer, IRecipe>) idsF.get(recipeRegistry);
				recipeRegistry.unfreeze();
				names.put(pojo.getLocation(), pojo.getRecipe());
				ids.put(pojo.getId(), pojo.getRecipe());
				recipeRegistry.freeze();
				pojo.setDelete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static CraftPOJO get(ItemStack is) {
		AtomicReference<CraftPOJO> ret = new AtomicReference<>(null);
		removedRecipe.forEach(e -> {
			if (e.getIs().isItemEqual(is)) ret.set(e);
		});
		return ret.get();
	}

	public static boolean contains(ItemStack is) {
		AtomicBoolean ab = new AtomicBoolean(false);
		removedRecipe.forEach(e -> {
			if (!ab.get() && e.getIs().isItemEqual(is)) ab.set(true);
		});
		return ab.get();
	}

	public static void removeCrafting(IRecipe recipe) {
		try {
			ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
			recipeRegistry.unfreeze();
			removedRecipe.add(new CraftPOJO(recipe.getRegistryName(), recipe, recipeRegistry.getID(recipe)));
			recipeRegistry.remove(recipe.getRegistryName());
			recipeRegistry.freeze();
			Main.Logger.info(ChatForm.prefix + "Removed recipe: " + recipe.getRecipeOutput().getDisplayName() + ":" + recipe.getRecipeOutput().getMetadata());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}