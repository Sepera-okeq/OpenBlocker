package ru.will0376.OpenBlocker.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.ArrayList;

public class ItemsBlocks {
	public static ArrayList<ItemsBlocks> ib = new ArrayList<>();
	public String name;
	public ItemStack is;
	public ArrayList<String> nbt = new ArrayList<>();
	public int limit = -1;
	public double mincost = -1;
	public boolean blocked = false;
	public boolean craft = false;
	public boolean limitb = false;
	public boolean mincostb = false;
	public boolean allmeta = false;

	public ItemsBlocks(String block) {
		name = block;
		blocked = JsonHelper.containsItem(JsonHelper.BLOCKER, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		is = new ItemStack(Item.getByNameOrId(block.split(":")[0] + ":" + block.split(":")[1]), 1, Integer.parseInt(block.split(":")[2]));
		nbt = JsonHelper.findAllNBT(block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]), true);

		limitb = JsonHelper.containsItem(JsonHelper.LIMIT, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		if (limitb)
			limit = JsonHelper.getClient(JsonHelper.LIMIT, block).get("limit").getAsInt();

		allmeta = JsonHelper.checkAllMetas(JsonHelper.BLOCKER, block.split(":")[0] + ":" + block.split(":")[1]);
		mincostb = JsonHelper.containsItem(JsonHelper.MINCOST, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		if (mincostb)
			mincost = Double.parseDouble(JsonHelper.getClient(JsonHelper.MINCOST, block).get("cost").getAsString());

		craft = JsonHelper.containsItem(JsonHelper.CRAFT, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		ib.add(this);
	}

}
