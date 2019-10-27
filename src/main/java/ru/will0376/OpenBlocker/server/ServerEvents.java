package ru.will0376.OpenBlocker.server;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class ServerEvents {
	private static HashMap<EntityPlayer, Long> cooldown = new HashMap<>();

	@SubscribeEvent
	public static void checkUseBlocker(PlayerInteractEvent e) {
		if (e.getItemStack() != ItemStack.EMPTY)
			return;
		EntityPlayer player = e.getEntityPlayer();
		ItemStack is = e.getItemStack();
		if (check(player, is, false)) e.setCanceled(true);
	}

	@SubscribeEvent
	public static void checkCraftBlocker(PlayerEvent.ItemCraftedEvent e) {
		EntityPlayer player = e.player;
		ItemStack is = e.crafting;
		if (check(player, is, true)) e.setCanceled(true);
	}

	@SubscribeEvent
	public static void checkSmeltBlocker(PlayerEvent.ItemSmeltedEvent e) {
		EntityPlayer player = e.player;
		ItemStack is = e.smelting;
		if (check(player, is, true)) e.setCanceled(true);
	}

	@SubscribeEvent
	public static void checkPickupBlocker(PlayerEvent.ItemPickupEvent e) {
		EntityPlayer player = e.player;
		ItemStack is = e.pickedUp.getItem();
		if (check(player, is, true)) e.setCanceled(true);
	}

	@SubscribeEvent
	public static void everyTickRemover(TickEvent.PlayerTickEvent e) {
		EntityPlayer player = e.player;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack is = player.inventory.getStackInSlot(i);
			check(player, is, false);
			checkEnchant(player, is, i);
		}
	}

	public static void checkEnchant(EntityPlayer player, ItemStack is, int invStackSlot) {
		try {
			NBTTagList nbts = (NBTTagList) is.getTagCompound().getTag("StoredEnchantments");
			if (nbts != null) {
				for (NBTBase tgs : nbts) {
					NBTTagCompound tmp = (NBTTagCompound) tgs;
					int id = tmp.getShort("id");
					int lvl = tmp.getShort("lvl");
					if (JsonHelper.containsEnchant(is) && checkPlayer(player)) {
						player.inventory.setInventorySlotContents(invStackSlot, removeEnchID(id, is));
						sendToPlayerMessage(player, String.format("Enchantment: %s has been blocked! and removed from your item(s)", Enchantment.getEnchantmentByID(id).getTranslatedName(lvl)));
					}
				}
			}
		} catch (Exception ignore) {
		}
	}

	public static boolean check(EntityPlayer player, ItemStack is, boolean disable_del) {
		if (JsonHelper.containsItem(JsonHelper.BLOCKER, is) && checkPlayer(player) && checkNBT(player, is)) {
			String text = String.format(ChatForm.prefix + "ItemStack %s has been blocked!", is.getItem().getRegistryName().toString());
			if (Main.config.isDeleteBlocked() && !disable_del) {
				text += (" and removed! =3");
				is.setCount(0);
			}
			sendToPlayerMessage(player, text);
			return true;
		}
		return false;
	}

	private static boolean checkNBT(EntityPlayer player, ItemStack is) {
		return JsonHelper.checkNBT(JsonHelper.BLOCKER, is);
	}

	private static boolean checkPlayer(EntityPlayer player) {
		return true;/*(!Main.config.getWhiteList().contains(player.getName().toLowerCase())
				|| (player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) == null
				&& player.isCreative()));*/
	}

	private static void sendToPlayerMessage(EntityPlayer player, String line) {
		long time = System.currentTimeMillis() / 1000;
		if (cooldown.containsKey(player)) {
			if (!(cooldown.get(player) > time)) {
				cooldown.remove(player);
				cooldown.put(player, time + 1);
				player.sendMessage(new TextComponentString(line));
			}
		} else {
			cooldown.put(player, time + 1);
			player.sendMessage(new TextComponentString(line));
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
			is.getTagCompound().removeTag("StoredEnchantments");
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
			nbt.setTag("StoredEnchantments", newnbttaglist);
			is.setTagCompound(nbt);
			return is;
		}
	}
}
