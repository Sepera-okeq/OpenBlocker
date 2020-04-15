package ru.will0376.OpenBlocker.server;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.ChatForm;
import ru.will0376.OpenBlocker.common.JsonHelper;

import java.util.HashMap;

@GradleSideOnly(GradleSide.SERVER)
@Mod.EventBusSubscriber()
public class ServerEvents {
	private static HashMap<EntityPlayer, Long> cooldown = new HashMap<>();
	private static HashMap<EntityPlayer, Long> cooldownDebug = new HashMap<>();

	public static ItemStack getPickBlock(World world, BlockPos pos) {
		try {
			Item item = Item.getItemFromBlock(world.getBlockState(pos).getBlock());
			if (item == null) {
				return ItemStack.EMPTY;
			} else {
				return new ItemStack(item, 1, Block.getBlockFromItem(item).getMetaFromState(world.getBlockState(pos)));
			}
		} catch (Exception e) {
			return ItemStack.EMPTY;
		}
	}


	@SubscribeEvent
	public static void checkBreakBlock(PlayerInteractEvent.LeftClickBlock e) {
		EntityPlayer player = e.getEntityPlayer();
		ItemStack is = getPickBlock(e.getWorld(), e.getPos());
		if (check(player, is, Main.config.isDeleteBlocked(),
				ChatForm.prefix + new TextComponentTranslation("serverevent.interaction", is.getItem().getRegistryName().toString(), is.getMetadata()).getFormattedText())) {
			if (Main.debug) sendToPlayerDebugMessage(player, "[DEBUG_Break] Break check done. Canceled event.");
			//if (Main.debug) sendToPlayerDebugMessage(player,"[DEBUG_Break] Break check done. Canceled event.");
			e.setCanceled(true);
		}

	}

	@SubscribeEvent
	public static void checkPlayerInteract(PlayerInteractEvent e) {
		if (e.getItemStack().isEmpty()) {
			Block block = e.getWorld().getBlockState(e.getPos()).getBlock();
			String nameblock = block.getRegistryName().toString();
			int meta = block.getMetaFromState(e.getWorld().getBlockState(e.getPos()));
			if (JsonHelper.checkAllMetas(JsonHelper.BLOCKER, nameblock)) meta = 0;
			if (JsonHelper.containsItemServer(JsonHelper.BLOCKER, nameblock, meta)) {
				sendToPlayerMessage(e.getEntityPlayer(), ChatForm.prefix +
						new TextComponentTranslation("serverevent.interaction", nameblock, meta).getFormattedText());
				if (Main.debug)
					sendToPlayerDebugMessage(e.getEntityPlayer(), "[DEBUG_Use1] Use check done. Canceled event.");
				e.setCanceled(true);
			}
		} else {
			EntityPlayer player = e.getEntityPlayer();

			ItemStack is = e.getItemStack();
			/*if(is.getItem() instanceof ItemBlock) {
				System.out.println(e.getWorld().getBlockState(e.getPos()).getBlock().getRegistryName().toString());
			}*/
			if (check(player, is, Main.config.isDeleteBlocked(),
					ChatForm.prefix + new TextComponentTranslation("serverevent.interaction", is.getItem().getRegistryName().toString(), is.getMetadata()).getFormattedText())) {
				if (Main.debug) sendToPlayerDebugMessage(player, "[DEBUG_Use2] Use check done. Canceled event.");
				e.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void placeBlock(BlockEvent.PlaceEvent e) {
		EntityPlayer player = e.getPlayer();

		IBlockState state = e.getPlacedBlock();
		Block bl = state.getBlock();
		ItemStack is = new ItemStack(bl, 1, state.getBlock().getMetaFromState(state));
		if (check(player, is, Main.config.isDeleteBlocked(),
				ChatForm.prefix + new TextComponentTranslation("serverevent.interaction", is.getItem().getRegistryName().toString(), is.getMetadata()).getFormattedText())) {
			if (Main.debug) sendToPlayerDebugMessage(player, "[DEBUG_BreakEvent] Use check done. Canceled event.");
			e.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void checkBlock(BlockEvent.BreakEvent e) {
		try {
			EntityPlayer player = e.getPlayer();
			IBlockState state = e.getState();
			Block bl = state.getBlock();
			ItemStack is = new ItemStack(bl, 1, state.getBlock().getMetaFromState(state));
			if (check(player, is, Main.config.isDeleteBlocked(),
					ChatForm.prefix + new TextComponentTranslation("serverevent.interaction", is.getItem().getRegistryName().toString(), is.getMetadata()).getFormattedText())) {
				if (Main.debug) sendToPlayerDebugMessage(player, "[DEBUG_BreakEvent] Use check done. Canceled event.");
				e.setCanceled(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void checkBlock(BlockEvent.MultiPlaceEvent e) {
		try {
			EntityPlayer player = e.getPlayer();
			IBlockState state = e.getState();
			Block bl = state.getBlock();
			ItemStack is = new ItemStack(bl, 1, state.getBlock().getMetaFromState(state));
			if (check(player, is, Main.config.isDeleteBlocked(),
					ChatForm.prefix + new TextComponentTranslation("serverevent.interaction", is.getItem().getRegistryName().toString(), is.getMetadata()).getFormattedText())) {
				if (Main.debug)
					sendToPlayerDebugMessage(player, "[DEBUG_MultiPlaceEvent] Use check done. Canceled event.");
				e.setCanceled(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void checkPickupBlocker(PlayerEvent.ItemPickupEvent e) {
		EntityPlayer player = e.player;
		ItemStack is = e.pickedUp.getItem();
		if (check(player, is, true,
				ChatForm.prefix + new TextComponentTranslation("serverevent.interaction", is.getItem().getRegistryName().toString(), is.getMetadata()).getFormattedText())) {
			if (Main.debug) sendToPlayerDebugMessage(player, "[DEBUG_Pickup] pickup check done. Canceled event.");
			e.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void everyTickRemover(TickEvent.PlayerTickEvent e) {
		EntityPlayer player = e.player;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack is = player.inventory.getStackInSlot(i);
			//check(player, is, false,
			//		ChatForm.prefix + new TextComponentTranslation("serverevent.interaction", is.getItem().getRegistryName().toString(), is.getMetadata()).getFormattedText());
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
					if (JsonHelper.containsEnchantServer(is) && !checkPlayer(player)) {
						player.inventory.setInventorySlotContents(invStackSlot, removeEnchID(id, is));
						sendToPlayerMessage(player,
								ChatForm.prefix + new TextComponentTranslation("serverevent.blockenchant", Enchantment.getEnchantmentByID(id).getTranslatedName(lvl)).getFormattedText()
						);
					}
				}
			}
		} catch (Exception ignore) {
		}
	}

	public static boolean check(EntityPlayer player, ItemStack is, boolean delete, String text) {
		try {
			if (Main.debug)
				if (!is.getDisplayName().contains("Air") && !is.getDisplayName().equals("Воздух"))
					sendToPlayerMessage(player, "[DEBUG] check itemstack: " + is.getDisplayName() + " for player: " + player.getName() + " disable_delete: " + delete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (JsonHelper.containsItemServer(JsonHelper.BLOCKER, is) && !checkPlayer(player) && checkNBT(player, is)) {
			if (delete) {
				text += " " + new TextComponentTranslation("serverevent.interaction.remove", ChatForm.prefix).getFormattedText();
				is.setCount(0);
			}
			sendToPlayerMessage(player, text);
			return true;
		}
		return false;
	}

//	public static boolean checkTE(World world, BlockPos pos, ItemStack is) {
//
//	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void placeLimitBlock(BlockEvent.PlaceEvent event) {
		if (Main.debug || !event.getWorld().isRemote) {
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			if (JsonHelper.containsItemServer(JsonHelper.LIMIT,
					block.getRegistryName().toString(),
					block.getMetaFromState(event.getWorld().getBlockState(event.getPos())))) {
				String nameblock = block.getRegistryName().toString();
				int meta = block.getMetaFromState(event.getWorld().getBlockState(event.getPos()));
				if (JsonHelper.checkAllMetas(JsonHelper.LIMIT, nameblock)) meta = 0;
				int limit = JsonHelper.getServer(JsonHelper.LIMIT, nameblock + ":" + meta).get("limit").getAsInt();
				if (getBlocksInChunk(event) > limit) {
					sendToPlayerMessage(event.getPlayer(), ChatForm.prefix + new TextComponentTranslation("serverevent.limitevent.limitover", limit).getFormattedText());
					if (Main.debug)
						sendToPlayerDebugMessage(event.getPlayer(), "[DEBUG_limit] pickup check done. Canceled event.");
					//event.getPlayer().sendMessage(new TextComponentTranslation("serverevent.limitevent.limitover",ChatForm.prefix ,limit));
					event.setCanceled(true);
				} else {
					event.getPlayer().sendStatusMessage(new TextComponentTranslation("serverevent.limitevent.action", getBlocksInChunk(event), limit), true);
				}

			}
/*			ItemsBlocks.ib.forEach((ib)-> {
				ItemStack placedStack = getPickBlock(event.getWorld(), event.getPos());
				if(placedStack.isItemEqual(ib.is))
				if(ib.limitb && getBlocksInChunk(event) > ib.limit) {
					event.getPlayer().sendMessage(new TextComponentTranslation("serverevent.limitevent.limitover",ChatForm.prefix ,ib.limit));
					event.setCanceled(true);
				} else {
					event.getPlayer().sendStatusMessage(new TextComponentTranslation("serverevent.limitevent.action", getBlocksInChunk(event), ib.limit), true);
				}
			});*/
		}
	}

	private static boolean checkNBT(EntityPlayer player, ItemStack is) {
		if (!is.hasTagCompound())
			return true;
		return JsonHelper.checkNBT(JsonHelper.BLOCKER, is);
	}

	private static boolean checkPlayer(EntityPlayer player) {
		if (Main.debug) return false;
		return (!Main.config.getWhiteList().contains(player.getName().toLowerCase()) ||
				player.canUseCommand(4, "openblocker.bypasscheck") && player.isCreative());
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
