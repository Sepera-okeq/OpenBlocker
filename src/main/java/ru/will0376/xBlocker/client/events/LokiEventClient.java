package ru.will0376.xBlocker.client.events;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.common.BaseUtils;
import ru.will0376.xBlocker.common.ChatForm;

public class LokiEventClient {
	private static boolean debug1 = Main.debug;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTooltipGet(ItemTooltipEvent event) {
		try {
			if (event.getItemStack() != ItemStack.EMPTY) {
				NBTTagCompound nbt = new NBTTagCompound();
				event.getItemStack().writeToNBT(nbt);
				if (nbt.hasKey("fromCreative")) {
					if (nbt.getString("fromCreative").equals("true")) {
						event.getToolTip().add(1, "test");
					}
				}
			}
			for (int i = 0; i < Main.getInstance().listItemsClient.size(); ++i) {
				String line = new TextComponentTranslation("lokievent.tooltip.blockeditem", ChatForm.prefix).getFormattedText();
				printToTooltip(event, Main.getInstance().listItemsClient.get(i), line);
			}
			for (int i = 0; i < Main.getInstance().listTempClient.size(); i++) {
				String line = new TextComponentTranslation("lokievent.tooltip.blockedtempitem").getFormattedText();
				printToTooltip(event, Main.getInstance().listTempClient.get(i), line);
			}
			for (int i = 0; i < Main.getInstance().listCostClient.size(); i++) {

				printToCastTooltip(event, Main.getInstance().listCostClient.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printToCastTooltip(ItemTooltipEvent event, String s) {
		try {
			String[] all = s.split(":");
			String name = all[0] + ":" + all[1];
			int meta = Integer.valueOf(all[2].split("@")[0]);
			String count = all[2].split("@")[1];
			ItemStack is = event.getItemStack();
			if (is.getItem() != Items.AIR && Item.getByNameOrId(name) == is.getItem() && is.getMetadata() == meta) {
				String line = new TextComponentTranslation("lokievent.tooltip.cost", count, Main.config.getDefSign()).getFormattedText();
				if (!event.getToolTip().contains(line)) {
					event.getToolTip().add(1, line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printToTooltip(ItemTooltipEvent event, String id, String line) {
		try {
			String[] all = id.split(":");
			String name = all[0] + ":" + all[1];
			int meta = Integer.parseInt(all[2].split("@")[0]);
			String ress = all[2].split("@")[1];
			ItemStack is = event.getItemStack();
			if (is.getItem() != Items.AIR && Item.getByNameOrId(name) == is.getItem() && is.getMetadata() == meta) {
				String reason = new TextComponentTranslation("lokievent.tooltipres", BaseUtils.lat2cyr(ress.replaceAll("&", "§").replaceAll("_", " "))).getFormattedText();//reason = "Причина: " + BaseUtils.lat2cyr(id.parts[3].replaceAll("&", "§").replaceAll("_", " "));

				if (!event.getToolTip().contains(line)) {
					event.getToolTip().add(1, line);
				}
				if (!event.getToolTip().contains(reason)) {
					event.getToolTip().add(2, reason);
				}

			}
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void tooltip(ItemTooltipEvent event) {
		try {
			if (Main.getInstance().listLimitedClient.size() != 0)
				for (int i = 0; i < Main.getInstance().listLimitedClient.size(); ++i) {
					String check = Main.getInstance().listLimitedClient.get(i);
					if (check != null && check.contains(":")) {
						String[] itemdata = Main.getInstance().listLimitedClient.get(i).split(":");
						String name = itemdata[0] + ":" + itemdata[1];
						int meta = Integer.parseInt(itemdata[2]);
						ItemStack var10 = event.getItemStack();
						if (var10.getItem() != Items.AIR && Item.getByNameOrId(name) == var10.getItem() && var10.getMetadata() == meta) {
							String line = new TextComponentTranslation("limitevent.tooltip.limit", Integer.parseInt(itemdata[3])).getFormattedText();
							String reason = new TextComponentTranslation("list.addblock.successfull.second", BaseUtils.lat2cyr(itemdata[4].replaceAll("&", "§").replaceAll("_", " "))).getFormattedText();
							if (!event.getToolTip().contains(line)) {
								event.getToolTip().add(1, line);
							}

						/*if (!event.getToolTip().contains(reason)) {
							event.getToolTip().add(2, reason);
						}*/
						}
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
