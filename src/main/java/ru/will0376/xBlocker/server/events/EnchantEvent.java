package ru.will0376.xBlocker.server.events;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.common.ChatForm;
import ru.will0376.xBlocker.server.utils.PexUtils;

@GradleSideOnly(GradleSide.SERVER)
public class EnchantEvent {
	private static boolean debug1 = Main.debug;

	@SubscribeEvent
	public void findItem(TickEvent.PlayerTickEvent event) {
	if (debug1 || !event.player.getEntityWorld().isRemote && !event.player.capabilities.isCreativeMode && Main.getInstance().getListEnchant() != null)
		for (int i = 0; i < Main.getInstance().getListEnchant().size(); ++i) {
			int id = Integer.valueOf(Main.getInstance().getListEnchant().get(i).toString().split(":")[0]);
		if (debug1 || !PexUtils.hasPex("xblocker.enchant", event.player))
			for (int j = 0; j < event.player.inventory.getSizeInventory(); ++j) {
				ItemStack item2 = event.player.inventory.getStackInSlot(j);
				if (item2 != null && id < 256 && findEnchID(id, item2)) {
					event.player.inventory.setInventorySlotContents(j, removeEnchID(id, item2));
					event.player.sendMessage(new TextComponentTranslation("enchant.block.erroruse", ChatForm.prefix));
					break;
				}
			}
		}
	}

	private static boolean findEnchID(int findID, ItemStack is) {
		NBTTagList nbttaglist = is.getEnchantmentTagList();
		if (nbttaglist == null) {
			return false;
		} else {
			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				short id = nbttaglist.getCompoundTagAt(i).getShort("id");
				short lvl = nbttaglist.getCompoundTagAt(i).getShort("lvl");
				if (id == findID) {
					return true;
				}
			}

			return false;
		}
	}

	private static ItemStack removeEnchID(int findID, ItemStack is) {
		NBTTagList nbttaglist = is.getEnchantmentTagList();
		NBTTagList newnbttaglist = new NBTTagList();
		if (nbttaglist == null) {
			return is;
		} else if (nbttaglist.tagCount() <= 1) {
			is.getTagCompound().removeTag("ench");
			return is;
		} else {
			for (int nbt = 0; nbt < nbttaglist.tagCount(); ++nbt) {
				short id = nbttaglist.getCompoundTagAt(nbt).getShort("id");
				short lvl = nbttaglist.getCompoundTagAt(nbt).getShort("lvl");
				if (id != findID) {
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					nbttagcompound.setShort("id", id);
					nbttagcompound.setShort("lvl", lvl);
					newnbttaglist.appendTag(nbttagcompound);
				}
			}
			NBTTagCompound nbt = is.getTagCompound();
			nbt.setTag("ench", newnbttaglist);
			is.setTagCompound(nbt);
			return is;
		}
	}
}
