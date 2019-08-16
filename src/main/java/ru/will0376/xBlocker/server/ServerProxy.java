package ru.will0376.xBlocker.server;

import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.client.network.MessageHandler_list;
import ru.will0376.xBlocker.common.ChatForm;
import ru.will0376.xBlocker.common.CommonProxy;
import ru.will0376.xBlocker.server.utils.cfg.ConfigCraftUtils;

import java.util.ArrayList;

public class ServerProxy extends CommonProxy {
	@Override
	public void events(FMLInitializationEvent event) {

	}

	@GradleSideOnly(GradleSide.SERVER)
	public static void removeCraftingRecipe(ItemStack removed, int meta) {
		try {
			ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
			ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());
			for (IRecipe tmp : recipes) {
				if (tmp.getRecipeOutput().getItem() != Items.AIR && removed != null && removed.getItem() == tmp.getRecipeOutput().getItem() && tmp.getRecipeOutput().getMetadata() == meta) {
					removeCraftingRecipe(tmp, meta, recipeRegistry);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GradleSideOnly(GradleSide.SERVER)
	public static void removeCraftingRecipe(IRecipe recipes, int meta, ForgeRegistry<IRecipe> recipeRegistry) {
		try {
			recipeRegistry.unfreeze();
			recipeRegistry.remove(recipes.getRegistryName());
			Main.network.sendToAll(new MessageHandler_list("3;" + recipes.getRecipeOutput().getItem().getRegistryName().toString() + ";" + meta + ";1"));
			recipeRegistry.freeze();
			Main.Logger.info(ChatForm.prefix + "Removed recipe: " + recipes.getRecipeOutput().getDisplayName() + ":" + meta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GradleSideOnly(GradleSide.SERVER)
	public static void removeCraft() {
		try {
			ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
			ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());
			String[] all = ConfigCraftUtils.readFromFileToString("list_craft").split("@");
			for (IRecipe iRecipe : recipes) {
				for (String tmp : all) {
					if (!tmp.equals("")) {
						String name = tmp.split(":")[0] + ":" + tmp.split(":")[1];
						int meta = Integer.valueOf(tmp.split(":")[2]);
						if (iRecipe.getRecipeOutput().getItem() != Items.AIR && iRecipe.getRecipeOutput().getItem().getRegistryName().toString().equals(name) && iRecipe.getRecipeOutput().getMetadata() == meta) {
							removeCraftingRecipe(iRecipe, meta, recipeRegistry);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
