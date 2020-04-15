package ru.will0376.OpenBlocker.client;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import ru.will0376.OpenBlocker.common.B64;
import ru.will0376.OpenBlocker.common.CraftManager;
import ru.will0376.OpenBlocker.common.ItemHelper;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemsBlocks implements Cloneable {
	public static ArrayList<ItemsBlocks> ib = new ArrayList<>();
	public String name;
	public String reasonBlock, reasonCraft;
	public ItemStack is;
	public ArrayList<String> nbts;
	public NBTTagCompound nbt = new NBTTagCompound();
	public int nbtsize = 0;
	public int limit = -1;
	public double mincost = -1;
	public boolean blocked;
	public boolean craft;
	public boolean limitb;
	public boolean mincostb;
	public boolean allmeta;

	public ItemsBlocks(String block) {
		name = block;
		blocked = JsonHelper.containsItemClient(JsonHelper.BLOCKER, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		is = new ItemStack(Item.getByNameOrId(block.split(":")[0] + ":" + block.split(":")[1]), 1, Integer.parseInt(block.split(":")[2]));

		nbts = JsonHelper.findAllNBT(block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		nbtsize = nbts.size();

		limitb = JsonHelper.containsItemClient(JsonHelper.LIMIT, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		if (limitb)
			limit = JsonHelper.getClient(JsonHelper.LIMIT, block).get("limit").getAsInt();

		allmeta = JsonHelper.checkAllMetas(JsonHelper.BLOCKER, block.split(":")[0] + ":" + block.split(":")[1]);
		if (limitb)
			allmeta = JsonHelper.checkAllMetas(JsonHelper.LIMIT, block.split(":")[0] + ":" + block.split(":")[1]);
		mincostb = JsonHelper.containsItemClient(JsonHelper.MINCOST, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		if (mincostb)
			mincost = Double.parseDouble(JsonHelper.getClient(JsonHelper.MINCOST, block).get("cost").getAsString());

		craft = JsonHelper.containsItemClient(JsonHelper.CRAFT, block.split(":")[0] + ":" + block.split(":")[1], Integer.parseInt(block.split(":")[2]));
		if (blocked || craft) {
			if (blocked && JsonHelper.getClient(JsonHelper.BLOCKER, block.split(":")[0] + ":" + block.split(":")[1] + ":" + Integer.parseInt(block.split(":")[2])).has("reason"))
				reasonBlock = JsonHelper.getClient(JsonHelper.BLOCKER, is.getItem().getRegistryName() + ":" + is.getMetadata()).get("reason").getAsString();
			if (craft && JsonHelper.getClient(JsonHelper.CRAFT, block.split(":")[0] + ":" + block.split(":")[1] + ":" + Integer.parseInt(block.split(":")[2])).has("reason"))
				reasonCraft = JsonHelper.getClient(JsonHelper.CRAFT, is.getItem().getRegistryName() + ":" + is.getMetadata()).get("reason").getAsString();
		}
		if (craft) CraftManager.removeCraftingRecipe(is);

		ib.add(this);

		compensationNBTS();
		if (allmeta)
			blockAllMeta();
	}

	private void blockAllMeta() {
		NonNullList<ItemStack> list = ItemHelper.getAllSubItems(is.getItem());
		for (ItemStack istemp : list) {
			if (istemp.isItemEqual(this.is))
				continue;
			ItemsBlocks newib = this.clone();
			newib.is = istemp;
			ib.add(newib);
		}
	}

	private void compensationNBTS() {
		if (!nbts.isEmpty()) {
			try {
				String tmpb64 = nbts.get(0);
				String tmp = B64.decode(tmpb64);
				nbt = JsonToNBT.getTagFromJson(tmp);
				is = new ItemStack(nbt);
				if (nbtsize > 1) {
					for (int i = 1; i < nbtsize; i++) {
						ItemsBlocks newib = this.clone();
						if (newib.nbt.equals(this.nbt)) newib.nbt = JsonToNBT.getTagFromJson(B64.decode(nbts.get(i)));
						newib.is = new ItemStack(newib.nbt);
						ib.add(newib);
					}
				}
			} catch (NBTException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<String> getLore() {
		ArrayList<String> ret = new ArrayList<>();
		if (mincostb) ret.add(I18n.format("ib.rore.mincost", mincost));
		if (blocked) ret.add(I18n.format("ib.lore.blocked", reasonBlock));
		if (allmeta) ret.add(I18n.format("ib.lore.allmeta"));
		if (limitb) ret.add(I18n.format("ib.lore.limit", limit));
		if (craft) ret.add(I18n.format("ib.lore.craft", reasonCraft));
		return ret;
	}

	@Override
	public ItemsBlocks clone() {
		try {
			return (ItemsBlocks) super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new InternalError();
		}
	}

	public static boolean containStack(ItemStack is) {
		AtomicBoolean ab = new AtomicBoolean(false);
		ib.forEach(e -> {
			if (e.is.isItemEqual(is)) ab.set(true);
		});
		return ab.get();
	}

	@Override
	public boolean equals(Object obj) {
		return is.isItemEqual((ItemStack) obj);
	}
}
