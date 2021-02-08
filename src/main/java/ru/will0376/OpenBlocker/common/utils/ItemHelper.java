package ru.will0376.OpenBlocker.common.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemHelper {
	public static int getCountAllSubItems(Item i) {
		return getAllSubItems(i).size();
	}

	public static NonNullList<ItemStack> getAllSubItems(Item i) {
		NonNullList<ItemStack> listall = NonNullList.create();
		for (CreativeTabs creativeTabs : CreativeTabs.CREATIVE_TAB_ARRAY) {
			if (!creativeTabs.equals(CreativeTabs.SEARCH)) {
				NonNullList<ItemStack> list = NonNullList.create();
				i.getSubItems(creativeTabs, list);
				listall.addAll(list);
			}
		}
		return listall;
	}
}
