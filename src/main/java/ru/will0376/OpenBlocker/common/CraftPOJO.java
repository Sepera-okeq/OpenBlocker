package ru.will0376.OpenBlocker.common;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public class CraftPOJO {

	private final ResourceLocation location;
	private final IRecipe recipe;
	private final int id;
	private final ItemStack is;
	private boolean delete = false;

	public CraftPOJO(ResourceLocation location, IRecipe recipe, int id) {
		this.location = location;
		this.recipe = recipe;
		this.id = id;
		this.is = recipe.getRecipeOutput();
	}

	public boolean contains(IRecipe recipeIn) {
		return recipeIn.equals(recipe);
	}

	public ItemStack getIs() {
		return is;
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public IRecipe getRecipe() {
		return recipe;
	}

	public int getId() {
		return id;
	}

	public void setDelete() {
		delete = true;
	}

	public boolean getDelete() {
		return delete;
	}
}
