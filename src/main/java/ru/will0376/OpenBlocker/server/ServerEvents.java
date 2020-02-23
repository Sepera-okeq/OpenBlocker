package ru.will0376.OpenBlocker.server;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.HashMap;

@GradleSideOnly(GradleSide.SERVER)
@Mod.EventBusSubscriber
public class ServerEvents {
	private static HashMap<EntityPlayer, Long> cooldown = new HashMap<>();

	public static ItemStack getPickBlock(World world, BlockPos pos) {
		try {
			Item item = Item.getItemFromBlock(world.getBlockState(pos).getBlock());
			if (item == null)
				return ItemStack.EMPTY;
			else
				return new ItemStack(item, 1, Block.getBlockFromItem(item).getMetaFromState(world.getBlockState(pos)));
		} catch (Exception e) {
			return ItemStack.EMPTY;
		}
	}


	@SubscribeEvent
	public static void checkBreackBlock(PlayerInteractEvent.LeftClickBlock e) {
		EntityPlayer player = e.getEntityPlayer();
		ItemStack is = getPickBlock(e.getWorld(), e.getPos());
		if (check(player, is, Main.config.isDeleteBlocked())) e.setCanceled(true);
	}

	@SubscribeEvent
	public static void checkUseBlocker(PlayerInteractEvent e) {
		if (e.getItemStack() != ItemStack.EMPTY)
			return;
		EntityPlayer player = e.getEntityPlayer();
		ItemStack is = e.getItemStack();
		if (check(player, is, Main.config.isDeleteBlocked())) e.setCanceled(true);
	}

	/*@SubscribeEvent
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
*/
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

	@SubscribeEvent
	public static void login(PlayerEvent.PlayerLoggedInEvent e) {
		JsonHelper.sendToPlayer((EntityPlayerMP) e.player);
	}

	public static void checkEnchant(EntityPlayer player, ItemStack is, int invStackSlot) {
		try {
			NBTTagList nbts = (NBTTagList) is.getTagCompound().getTag("StoredEnchantments");
			if (nbts != null) {
				for (NBTBase tgs : nbts) {
					NBTTagCompound tmp = (NBTTagCompound) tgs;
					int id = tmp.getShort("id");
					int lvl = tmp.getShort("lvl");
					if (JsonHelper.containsEnchant(is) && !checkPlayer(player)) {
						player.inventory.setInventorySlotContents(invStackSlot, removeEnchID(id, is));
						sendToPlayerMessage(player, String.format("Enchantment: %s has been blocked! and removed from your item(s)", Enchantment.getEnchantmentByID(id).getTranslatedName(lvl)));
					}
				}
			}
		} catch (Exception ignore) {
		}
	}

	public static boolean check(EntityPlayer player, ItemStack is, boolean disable_del) {
		if (JsonHelper.containsItem(JsonHelper.BLOCKER, is) && !checkPlayer(player) && checkNBT(player, is)) {
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
		if (!is.hasTagCompound())
			return true;
		return JsonHelper.checkNBT(JsonHelper.BLOCKER, is);
	}

	private static boolean checkPlayer(EntityPlayer player) {
		System.out.println(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null);
		return (!Main.config.getWhiteList().contains(player.getName().toLowerCase()) ||
				(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList() != null
						&& FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers() != null
						&& FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null) && player.isCreative());
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
