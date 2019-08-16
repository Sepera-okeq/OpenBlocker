package ru.will0376.xBlocker.server.events;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.common.ChatForm;
import ru.will0376.xBlocker.common.ItemData;
import ru.will0376.xBlocker.common.ItemList;
import ru.will0376.xBlocker.server.utils.PexUtils;

import java.util.HashMap;

@GradleSideOnly(GradleSide.SERVER)
public class LokiEvent {
	private HashMap<EntityPlayer, Long> cooldown = new HashMap<>();
	private static boolean debug1 = Main.debug;

	public Item getItem(World w, int x, int y, int z) {
		return Item.getItemFromBlock(w.getBlockState(new BlockPos(x, y, z)).getBlock());
	}

	public ItemStack getPickBlock(World world, int x, int y, int z, Block block2) {
		Item item = this.getItem(world, x, y, z);
		if (item == null) {
			return null;
		} else {
			Block block = Block.getBlockFromItem(item);
			return new ItemStack(item, 1, block.getMetaFromState(world.getBlockState(new BlockPos(x, y, z))));
		}
	}

	@SubscribeEvent(
			priority = EventPriority.HIGHEST
	)
	public void onBlockInteract(PlayerInteractEvent event) {
		try {
			if (debug1 || !event.getWorld().isRemote && Main.getInstance().getListItems() != null) {
				for (int i = 0; i < Main.getInstance().getListItems().size(); ++i) {
					blockBlockInteract(event, Main.getInstance().getListItems().get(i), i, new TextComponentTranslation("lokievent.block.blockint", ChatForm.prefix).getFormattedText(), new TextComponentTranslation("lokievent.tooltip.blockeditem2", ChatForm.prefix).getFormattedText());
				}
				for (int i = 0; i < Main.getInstance().getListTemp().size(); ++i) {
					blockBlockInteract(event, Main.getInstance().getListTemp().get(i), i, new TextComponentTranslation("lokievent.block.tempblockint", ChatForm.prefix).getFormattedText(), new TextComponentTranslation("lokievent.tooltip.blockedtempitem2", ChatForm.prefix).getFormattedText());
				}
			}
		} catch (Exception ignore) {
		}
	}

	private void blockBlockInteract(PlayerInteractEvent event, ItemData id, int i, String line, String line2) {
		ItemData check = id;
		String name = check.name;
		int meta = check.meta;
		Block block = Block.getBlockFromName(name);
		Item it = Item.getByNameOrId(name);
		boolean has = false;
		if (PexUtils.hasPex("xblocker.interact." + Main.config.getServerName() + "." + name + ":" + meta, event.getEntityPlayer())) {
			has = true;
		}

		if (debug1 || !PexUtils.hasPex("xblocker." + Main.config.getServerName() + "." + name + ":" + meta, event.getEntityPlayer())) {
			ItemStack is;
			if (debug1)
				has = false;
			is = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);

			if (is.getItem() != Items.AIR && it == is.getItem() && meta == is.getMetadata()) {
				sendToPlayerMessage(event.getEntityPlayer(), line2);
				event.setCanceled(true);
				return;
			}
			is = event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND);
			if (is.getItem() != Items.AIR && it == is.getItem() && meta == is.getMetadata()) {
				event.setCanceled(true);
				sendToPlayerMessage(event.getEntityPlayer(), line2);
				return;
			}
		}
		int x = event.getPos().getX();
		int y = event.getPos().getY();
		int z = event.getPos().getZ();
		Block block2 = event.getWorld().getBlockState(event.getPos()).getBlock();
		ItemStack item = null;
		if (block2 != null) {
			item = this.getPickBlock(event.getWorld(), x, y, z, block2);
		}

		if (!has && item != null && block2 == block && item.getItem() instanceof ItemBlock && item.getMetadata() == meta) {
			event.setCanceled(true);
			sendToPlayerMessage(event.getEntityPlayer(), line);
			return;
		}
	}

	@SubscribeEvent(
			priority = EventPriority.HIGHEST
	)
	public void onItemUse(PlayerInteractEvent.RightClickItem event) {
		if (debug1 || !event.getEntityPlayer().getEntityWorld().isRemote && Main.getInstance().getListItems() != null) {
			for (int i = 0; i < Main.getInstance().getListItems().size(); ++i) {
				blockItemUse(event, Main.getInstance().getListItems().get(i), i, new TextComponentTranslation("lokievent.item.use.err", ChatForm.prefix).getFormattedText());
			}
			for (int i = 0; i < Main.getInstance().getListTemp().size(); ++i) {
				blockItemUse(event, Main.getInstance().getListTemp().get(i), i, new TextComponentTranslation("lokievent.item.use.temperr", ChatForm.prefix).getFormattedText());
			}
		}

	}

	private void blockItemUse(PlayerInteractEvent.RightClickItem event, ItemData id, int i, String line) {
		String name = id.name;
		int meta = id.meta;
		if (debug1 || !PexUtils.hasPex("xblocker." + Main.config.getServerName() + "." + name + ":" + meta, event.getEntityPlayer())) {
			ItemStack is;
			is = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
			if (is.getItem() != Items.AIR && Item.getByNameOrId(name) == is.getItem() && is.getMetadata() == meta) {
				//event.getEntityPlayer().sendMessage(new TextComponentString(line));
				sendToPlayerMessage(event.getEntityPlayer(), line);
				event.setCanceled(true);
				return;
			}
			is = event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND);
			if (is.getItem() != Items.AIR && Item.getByNameOrId(name) == is.getItem() && is.getMetadata() == meta) {
				//event.getEntityPlayer().sendMessage(new TextComponentString(line));
				sendToPlayerMessage(event.getEntityPlayer(), line);
				event.setCanceled(true);
				return;
			}
		}
	}

	@SubscribeEvent(
			priority = EventPriority.HIGHEST
	)
	public void onEntityAttack(AttackEntityEvent event) {
		if (debug1 || !event.getEntityPlayer().getEntityWorld().isRemote && Main.getInstance().getListItems() != null) {
			for (int i = 0; i < Main.getInstance().getListItems().size(); ++i) {
				blockEntityAtc(event, Main.getInstance().getListItems().get(i), i, false);
			}
			for (int i = 0; i < Main.getInstance().getListTemp().size(); ++i) {
				blockEntityAtc(event, Main.getInstance().getListTemp().get(i), i, true);
			}
		}

	}

	private void blockEntityAtc(AttackEntityEvent event, ItemData id, int i, boolean timed) {
		ItemData check = id;
		String name = check.name;
		int meta = check.meta;
		String message = "";
		if (!timed) {
			if (event.getTarget() instanceof EntityPlayer)
//				message = "Взаимодействие по игрокам данным предметом запрещено."; //по игрокам
				message = new TextComponentTranslation("lokievent.block.entitypl.atc", ChatForm.prefix).getFormattedText();
			else
//				message = "Взаимодействие по сущностям данным предметом запрещено."; //по сущностям
				message = new TextComponentTranslation("lokievent.block.entity.atc", ChatForm.prefix).getFormattedText();
		} else {
			if (event.getTarget() instanceof EntityPlayer)
//				message = "Взаимодействие по игрокам данным предметом временно запрещено."; //по игрокам
				message = new TextComponentTranslation("lokievent.block.entitypl.atctemp", ChatForm.prefix).getFormattedText();
			else
//				message = "Взаимодействие по сущностям данным предметом запрещено."; //по сущностям
				message = new TextComponentTranslation("lokievent.block.entity.atctemp", ChatForm.prefix).getFormattedText();
		}

		if (debug1 || !PexUtils.hasPex("xblocker.attack." + name + ":" + meta, event.getEntityPlayer())) {
			ItemStack is;
			is = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
			if (is.getItem() != Items.AIR && Item.getByNameOrId(name) == is.getItem()) {
				//event.getEntityPlayer().sendMessage(new TextComponentString(message));
				sendToPlayerMessage(event.getEntityPlayer(), message);
				event.setCanceled(true);
				return;
			}
			is = event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND);
			if (is.getItem() != Items.AIR && Item.getByNameOrId(name) == is.getItem()) {
				//event.getEntityPlayer().sendMessage(new TextComponentString(message));
				sendToPlayerMessage(event.getEntityPlayer(), message);
				event.setCanceled(true);
				return;
			}
		}
	}

	@SubscribeEvent(
			priority = EventPriority.HIGHEST
	)
	public void onItemPickup(EntityItemPickupEvent event) {
		if (debug1 || !event.getEntityPlayer().getEntityWorld().isRemote && event.getItem() != null && Main.getInstance().getListItems() != null) {
			for (int i = 0; i < Main.getInstance().getListItems().size(); ++i) {
				ItemData check = Main.getInstance().getListItems().get(i);
				String name = check.name;
				int meta = check.meta;
				//String message = "Данный предмет запрещено подбирать.";
				String message = new TextComponentTranslation("lokievent.pickup.error", ChatForm.prefix).getFormattedText();
				if (debug1 || !PexUtils.hasPex("xblocker.drop." + Main.config.getServerName() + "." + name + ":" + meta, event.getEntityPlayer())) {
					ItemStack is;
					is = event.getItem().getItem();
					if (is.getItem() != Items.AIR && Item.getByNameOrId(name) == is.getItem() && is.getMetadata() == meta) {
						//event.getEntityPlayer().sendMessage(new TextComponentString(message));
						sendToPlayerMessage(event.getEntityPlayer(), message);
						event.setCanceled(true);
						return;
					}
				}
			}
		}

	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && !event.player.getEntityWorld().isRemote && !event.player.capabilities.isCreativeMode) {
			ItemList list = Main.getInstance().getListItems();
			if (list != null) {
				for (int i = 0; i < list.size(); ++i) {
					ItemData check = list.get(i);
					processDeleteItem(event.player, check.name, check.meta);

				}
			}
		}

	}

	private void processDeleteItem(EntityPlayer player, String name, int meta) {
		Item it = Item.getByNameOrId(name);

		for (int slot = 0; slot < player.inventory.getSizeInventory(); ++slot) {
			ItemStack stack = player.inventory.getStackInSlot(slot);
			if (stack.getItem() != Items.AIR && it == stack.getItem() && meta == stack.getMetadata()) {
				boolean hasPermission = false;
				if (PexUtils.hasPex("xblocker." + Main.config.getServerName() + '.' + name + ':' + meta, player)) {
					hasPermission = true;
				} else if (PexUtils.hasPex("xblocker.clear." + Main.config.getServerName() + '.' + name + ':' + meta, player)) {
					hasPermission = true;
				}
				if (debug1)
					hasPermission = false;
				if (!hasPermission) {
					int stackMeta = stack.getMetadata();
					String message;
					if (stackMeta == 0) {
//						message = ChatForm.prefix+"Предмет " + name + " удален из Вашего инвентаря.";
						message = new TextComponentTranslation("lokievent.deleteitem", ChatForm.prefix, name).getFormattedText();
					} else {
//						message = ChatForm.prefix+"Предмет " + name + ':' + stackMeta + " удален из Вашего инвентаря.";
						message = new TextComponentTranslation("lokievent.deleteitem", ChatForm.prefix, name + ":" + stackMeta).getFormattedText();
					}
					sendToPlayerMessage(player, message);
					//player.sendMessage(new TextComponentString(message));
					player.inventory.removeStackFromSlot(slot);
					break;
				}
			}
		}

	}

	private void sendToPlayerMessage(EntityPlayer player, String line) {
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
}
