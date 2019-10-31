package ru.will0376.OpenBlocker.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.ArrayList;
import java.util.Objects;

public class ItemsBlocks {
	public static ArrayList<ItemsBlocks> ib = new ArrayList<>();
	public String name;
	public ItemStack is;
	public String nbt;
	public int limit = -1;
	public double mincost = -1;
	public boolean blocked;
	public boolean craft;
	public boolean limitb;
	public boolean mincostb;
	public boolean allmeta;

	public ItemsBlocks(String block) {
		name = block;
		blocked = JsonHelper.containsItem(JsonHelper.BLOCKER, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		is = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(block.split(":")[0] + ":" + block.split(":")[1])), 0, Integer.parseInt(block.split(":")[2]));
		nbt = JsonHelper.getClient(JsonHelper.BLOCKER, block).getAsJsonObject().get("nbts").getAsJsonArray().toString();

		limitb = JsonHelper.containsItem(JsonHelper.LIMIT, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		if (limitb)
			limit = JsonHelper.getClient(JsonHelper.LIMIT, block).get("limit").getAsInt();

		allmeta = JsonHelper.checkAllMetas(JsonHelper.BLOCKER, block.split(":")[0] + ":" + block.split(":")[1]);
		mincostb = JsonHelper.containsItem(JsonHelper.MINCOST, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		if (mincostb)
			mincost = JsonHelper.getClient(JsonHelper.MINCOST, block).getAsJsonObject("cost").getAsDouble();

		craft = JsonHelper.containsItem(JsonHelper.CRAFT, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		ib.add(this);
	}

}
