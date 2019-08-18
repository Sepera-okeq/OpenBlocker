package ru.will0376.xBlocker.server.events;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.common.ChatForm;
import ru.will0376.xBlocker.server.utils.PexUtils;
import ru.will0376.xBlocker.server.utils.cfg.ConfigPlaceUtils;

@GradleSideOnly(GradleSide.SERVER)
public class LimitedEvent {
	private static boolean debug1 = Main.debug;

	public Item getItem(World w, BlockPos pos) {
		return Item.getItemFromBlock(w.getBlockState(pos).getBlock());
	}

	public ItemStack getPickBlock(World world, BlockPos pos) {
		try {
			Item item = getItem(world, pos);
			if (item == null) {
				return ItemStack.EMPTY;
			} else {
				Block block = ((Block) (item instanceof ItemBlock ? Block.getBlockFromItem(item) : this));
				return new ItemStack(item, 1, block.getMetaFromState(world.getBlockState(pos)));
			}
		} catch (Exception e) {
			//e.printStackTrace();
			return ItemStack.EMPTY;
		}
	}

	private int getBlocksInChunk(BlockEvent.PlaceEvent event) {
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


	@SubscribeEvent(
			priority = EventPriority.HIGHEST
	)
	public void placeBlock(BlockEvent.PlaceEvent event) {
		if (debug1 || !event.getWorld().isRemote && Main.getInstance().getListLimited() != null && !Main.getInstance().getListLimited().isEmpty())
			for (int i = 0; i < Main.getInstance().getListLimited().size(); ++i) {
				String check = Main.getInstance().getListLimited().get(i).toString();
				if (check != null && check.contains(":")) {
					String[] itemdata = check.split(":");
					if (itemdata.length > 2) {
						String name = itemdata[0] + ":" + itemdata[1];
						int meta = Integer.parseInt(itemdata[2]);
						if (debug1 || !PexUtils.hasPex("xblocker." + Main.config.getServerName() + ".limit." + name + ":" + meta, event.getPlayer())) {
							Block block = Block.getBlockFromName(name);
							int count = Integer.parseInt(itemdata[3]);
							Block block2 = event.getWorld().getBlockState(event.getPos()).getBlock();
							ItemStack item = getPickBlock(event.getWorld(), event.getPos());
							if (item != null && block2 == block && item.getMetadata() == meta)
								try {
									if (getBlocksInChunk(event) > count) {
										event.getPlayer().sendMessage(new TextComponentTranslation("limitevent.blockowner.limitover", ChatForm.prefix, count));
										event.setCanceled(true);
										if (event.getWorld().getBlockState(event.getPos()).getBlock() == block2 && Main.config.getLimitWarring())
											Main.Logger.warn(new TextComponentTranslation("limitevent.warring", event.getPos(), event.getPlayer().getName()).getFormattedText());
									} else {
										event.getPlayer().sendStatusMessage(new TextComponentTranslation("action.bar.place.block", getBlocksInChunk(event), count), true);
										ConfigPlaceUtils.addToFile("list_protect", event.getPlayer().getDisplayName() + ":" + event.getPos().getX() + ":" + event.getPos().getY() + ":" + event.getPos().getZ());
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
					}
				}
			}
	}
}
