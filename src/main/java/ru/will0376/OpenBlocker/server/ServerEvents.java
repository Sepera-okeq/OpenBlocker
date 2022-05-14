package ru.will0376.OpenBlocker.server;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;
import ru.will0376.OpenBlocker.common.net.ToClientBlocked;
import ru.will0376.OpenBlocker.common.utils.ChatForm;
import ru.will0376.OpenBlocker.common.utils.Flag;
import ru.will0376.OpenBlocker.server.tileentity.TileEntityChecker;

import java.util.HashMap;

@GradleSideOnly(GradleSide.SERVER)
@Mod.EventBusSubscriber(/*Side.SERVER*/)
public class ServerEvents {
	private static final HashMap<EntityPlayer, Long> cooldown = new HashMap<>();
	private static final HashMap<EntityPlayer, Long> cooldownDebug = new HashMap<>();

	public static ItemStack getPickBlock(World world, BlockPos pos) {
		try {
			Item item = Item.getItemFromBlock(world.getBlockState(pos).getBlock());
			if (item == Items.AIR) {
				return ItemStack.EMPTY;
			} else {
				return new ItemStack(item, 1, Block.getBlockFromItem(item).getMetaFromState(world.getBlockState(pos)));
			}
		} catch (Exception e) {
			return ItemStack.EMPTY;
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void checkBreakBlock(BlockEvent.BreakEvent e) {
		EntityPlayer player = e.getPlayer();
		ItemStack is = getPickBlock(e.getWorld(), e.getPos());
		boolean canceled = TileEntityChecker.checkBlock(player, is.getItem()
				.getRegistryName()
				.toString(), e.getWorld(), e.getPos());
		e.setCanceled(canceled);
		if (!e.isCanceled())
			e.setCanceled(checkBlock(player, is, "serverevent.breakBlock", "BlockEvent.BreakEvent"));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void checkPlaceBlock(BlockEvent.PlaceEvent e) {
		EntityPlayer player = e.getPlayer();
		ItemStack is = getPickBlock(e.getWorld(), e.getPos());
		e.setCanceled(TileEntityChecker.checkBlock(player, is.getItem().getRegistryName().toString(), e.getWorld(), e.getPos()));
		if (!e.isCanceled())
			e.setCanceled(checkBlock(player, is, "serverevent.placeBlock", "BlockEvent.PlaceEvent"));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void checkPlaceBlock(BlockEvent.MultiPlaceEvent e) {
		EntityPlayer player = e.getPlayer();
		ItemStack is = getPickBlock(e.getWorld(), e.getPos());
		e.setCanceled(TileEntityChecker.checkBlock(player, is.getItem().getRegistryName().toString(), e.getWorld(), e.getPos()));
		if (!e.isCanceled())
			e.setCanceled(checkBlock(player, is, "serverevent.placeBlock", "BlockEvent.MultiPlaceEvent"));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void playerCheckInteract(PlayerInteractEvent.RightClickBlock e) {
		EntityPlayer player = e.getEntityPlayer();
		ItemStack is = getPickBlock(e.getWorld(), e.getPos());
		e.setCanceled(TileEntityChecker.checkBlock(player, is.getItem().getRegistryName().toString(), e.getWorld(), e.getPos()));
		if (!e.isCanceled())
			e.setCanceled(checkBlock(player, is, "serverevent.interaction", "PlayerInteractEvent.RightClickBlock"));
	}

	public static boolean checkBlock(EntityPlayer player, ItemStack is, String translation, String debug) {
		if (translation.equals("serverevent.interaction")) {
			Blocked blockedByStack = BlockHelper.findBlockedByStack(is);
			if (blockedByStack != null && blockedByStack.containsFlag(Flag.Flags.Interaction) && blockedByStack.getFlagData(Flag.Flags.Interaction)
					.getAsBoolean())
				return false;
		}

		if (check(player, is, Main.config.isDeleteBlocked() && (!debug.contains("RightClickBlock") && !debug.contains(
				"BreakEvent")), ChatForm.prefix + new TextComponentTranslation(translation, is.getItem()
				.getRegistryName()
				.toString(), is.getMetadata()).getFormattedText())) {

			if (Main.debug)
				sendToPlayerDebugMessage(player, "[DEBUG_" + debug + "] Canceled event.");
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void checkPickupBlocker(PlayerEvent.ItemPickupEvent e) {
		if (!Main.config.isEnablePickupHandler())
			return;

		EntityPlayer player = e.player;
		ItemStack is = e.pickedUp.getItem();
		if (check(player, is, true, ChatForm.prefix + new TextComponentTranslation("serverevent.pickup", is.getItem()
				.getRegistryName()
				.toString(), is.getMetadata()).getFormattedText())) {
			e.setCanceled(true);
			e.pickedUp.setDead();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void everyTickRemover(TickEvent.PlayerTickEvent e) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().getTickCounter() % 20 == 0) {
			EntityPlayer player = e.player;
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack is = player.inventory.getStackInSlot(i);
				check(player, is, Main.config.isDeleteBlocked(), ChatForm.prefix + new TextComponentTranslation("serverevent" +
						".tick", is.getItem()
						.getRegistryName()
						.toString(), is.getMetadata()).getFormattedText());
				checkEnchant(player, is, i);
			}
		}
	}

	@SubscribeEvent
	public static void login(PlayerEvent.PlayerLoggedInEvent e) {
		EntityPlayerMP player = (EntityPlayerMP) e.player;
		BlockHelper.sendMessageToPlayer(player, null, ToClientBlocked.Action.ClearList);
		BlockHelper.sendAllToClient(player, ToClientBlocked.Action.AddBlock);
	}

	public static void checkEnchant(EntityPlayer player, ItemStack is, int invStackSlot) {
		try {
			NBTTagList nbts = (NBTTagList) is.getTagCompound().getTag("ench");
			if (is.isItemEnchanted()) {
				for (NBTBase tgs : nbts) {
					NBTTagCompound tmp = (NBTTagCompound) tgs;
					int id = tmp.getShort("id");
					int lvl = tmp.getShort("lvl");
					if (BlockHelper.findBlockedByEnch(id, lvl) != null && !checkPlayer(player)) {
						player.inventory.setInventorySlotContents(invStackSlot, removeEnchID(id, is));
						sendToPlayerMessage(player, ChatForm.prefix + new TextComponentTranslation("serverevent.blockenchant",
								Enchantment.getEnchantmentByID(id)
								.getTranslatedName(lvl)).getFormattedText());
					}
				}
			}
		} catch (Exception ignore) {
		}
	}

	public static boolean check(EntityPlayer player, ItemStack is, boolean delete, String text) {
		Blocked blockedByStack = BlockHelper.findBlockedByStack(is);
		if (blockedByStack != null && blockedByStack.getStatus()
				.contains(Blocked.Status.Blocked) && !checkPlayer(player) && checkNBT(player, is)) {
			if (delete) {
				for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
					if (player.inventory.getStackInSlot(i) == is)
						player.inventory.removeStackFromSlot(i);
				}
			}
			sendToPlayerMessage(player, text);
			return true;
		}
		return false;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void placeLimitBlock(BlockEvent.PlaceEvent event) {
		if (Main.debug || !event.getWorld().isRemote) {
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			Blocked blockedByStack = BlockHelper.findBlockedByStack(new ItemStack(block, 1,
					block.getMetaFromState(event.getWorld()
					.getBlockState(event.getPos()))));
			if (blockedByStack != null && blockedByStack.containStatus(Blocked.Status.Limit)) {
				int limit = blockedByStack.getFlagData(Flag.Flags.Limit).getAsInteger();
				if (getBlocksInChunk(event) > limit) {
					sendToPlayerMessage(event.getPlayer(), ChatForm.prefix + new TextComponentTranslation("serverevent" +
							".limitevent.limitover", limit).getFormattedText());
					if (Main.debug)
						sendToPlayerDebugMessage(event.getPlayer(), "[DEBUG_limit] pickup check done. Canceled event.");
					event.setCanceled(true);
				} else {
					event.getPlayer()
							.sendStatusMessage(new TextComponentTranslation("serverevent.limitevent.action",
									getBlocksInChunk(event), limit), true);
				}

			}
		}
	}

	@GradleSideOnly(GradleSide.SERVER)
	private static boolean checkNBT(EntityPlayer player, ItemStack is) {
		if (!is.hasTagCompound())
			return true;
		return BlockHelper.checkNBT(is.copy());
	}

	/**
	 * @param player - player
	 *
	 * @return false, if blocked.
	 */
	private static boolean checkPlayer(EntityPlayer player) {
		if (Main.debug)
			return false;
		return player.canUseCommand(4, "openblocker.bypasscheck");
	}

	private static void sendToPlayerMessage(EntityPlayer player, String line) {
		long time = System.currentTimeMillis() / 1000;
		if (cooldown.containsKey(player)) {
			if (!(cooldown.get(player) > time)) {
				cooldown.remove(player);
				cooldown.put(player, time + 2);
				player.sendMessage(new TextComponentString(line));
			}
		} else {
			cooldown.put(player, time + 2);
			player.sendMessage(new TextComponentString(line));
		}

	}

	private static void sendToPlayerDebugMessage(EntityPlayer player, String line) {
		if (player.canUseCommand(4, "ob.debug.messages")) {
			long time = System.currentTimeMillis() / 1000;
			if (cooldownDebug.containsKey(player)) {
				if (!(cooldownDebug.get(player) > time)) {
					cooldownDebug.remove(player);
					cooldownDebug.put(player, time + 1);
					player.sendMessage(new TextComponentString(line));
				}
			} else {
				cooldownDebug.put(player, time + 1);
				player.sendMessage(new TextComponentString(line));
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
		if (nbttaglist.isEmpty()) {
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

	private static int getBlocksInChunk(BlockEvent.PlaceEvent event) {
		int count = 0;
		Chunk ch = event.getWorld().getChunk(event.getPos());
		for (int x = 0; x <= 15; x++)
			for (int z = 0; z <= 15; z++)
				for (int y = 0; y <= 255; y++) {
					BlockPos bp = new BlockPos(ch.getPos().getXStart() + x, y, ch.getPos().getZStart() + z);
					if (event.getWorld().getBlockState(bp).equals(event.getPlacedBlock()))
						count = count + 1;
				}
		return count;
	}


}
