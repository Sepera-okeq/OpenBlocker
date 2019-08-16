package ru.will0376.xBlocker.server.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.client.network.*;
import ru.will0376.xBlocker.common.BaseUtils;
import ru.will0376.xBlocker.common.ChatForm;
import ru.will0376.xBlocker.server.ServerProxy;
import ru.will0376.xBlocker.server.utils.cfg.*;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@GradleSideOnly(GradleSide.SERVER)
public class Command extends CommandBase {
	public final String usage = TextFormatting.DARK_AQUA + "/xb (" + TextFormatting.RED + "add " + TextFormatting.RESET + "или " + TextFormatting.RED + "set) (причина)  \n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.RED + "tempblock" + TextFormatting.RESET + " (причина)\n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.LIGHT_PURPLE + "mincost <цена>\n" +
			TextFormatting.DARK_AQUA + "/xb (" + TextFormatting.GREEN + "delete " + TextFormatting.RESET + "или " + TextFormatting.GREEN + "remove) \n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.GREEN + "removeCost \n" +
			TextFormatting.DARK_AQUA + "/xb (" + TextFormatting.GREEN + "deletetempblock " + TextFormatting.RESET + "или " + TextFormatting.GREEN + "removetempblock) \n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.GOLD + "limit" + TextFormatting.RESET + " <кол-во> (причина)\n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.GREEN + "removeLimit\n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.DARK_PURPLE + "reload\n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.RED + "enchant\n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.GREEN + "enchant (remove или delete)\n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.AQUA + "perms\n" +
			TextFormatting.DARK_AQUA + "/xb " + TextFormatting.GOLD + "craft";

	@Override
	public String getName() {
		return "xb";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return ChatForm.prefix + usage;
	}

	@Override
	public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, "add", "set", "delete", "remove", "limit", "removeLimit", "reload", "enchant", "perms", "craft", "tempblock", "deletetempblock", "removetempblock", "mincost", "removeCost");
		else
			return args.length >= 2 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
	}

	@GradleSideOnly(GradleSide.SERVER)
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			if (!checkPermission(sender, "xblocker.main.help"))
				return;
			sender.sendMessage(new TextComponentString(ChatForm.prefix + "Commands:\n" + usage));
			return;
		}
		EntityPlayer player = (EntityPlayer) sender;
		if (args[0].equals("add") || args[0].equals("set")) {
			if (!checkPermission(sender, "xblocker.lists.add"))
				return;
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR) {
				ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
				String name = is.getItem().getRegistryName().toString();

				int meta = is.getMetadata();
				String res = "";

				for (int i = 1; i < args.length; i++) {
					res = res + args[i] + " ";
				}
				if (res.equalsIgnoreCase(""))
					res = Main.config.getDefRes();

				res = BaseUtils.cyr2lat(res);
				Main.network.sendToAll(new MessageHandler_list(1 + ";" + name + ";" + meta + ";" + res));
				ConfigUtils.addToFile("list", name + ":" + meta + ":" + res);
				ConfigCraftUtils.addToFile("list_craft", name + ":" + meta);
				ServerProxy.removeCraftingRecipe(is, meta);
				player.sendMessage(new TextComponentTranslation("list.addblock.successfull", ChatForm.prefix, name));
				player.sendMessage(new TextComponentTranslation("list.addblock.successfull.second", BaseUtils.lat2cyr(res).replaceAll("&", "§").replaceAll("_", " ")));
			} else {
				player.sendMessage(new TextComponentTranslation("error.handIsNull", ChatForm.prefix_error));

			}
		} else if (args[0].equals("tempblock")) {
			if (!checkPermission(sender, "xblocker.lists.tempblock"))
				return;
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR) {
				ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
				String name = is.getItem().getRegistryName().toString();

				int meta = is.getMetadata();
				String res = "";

				for (int i = 1; i < args.length; i++) {
					res = res + args[i] + " ";
				}
				if (res.equalsIgnoreCase(""))
					res = Main.config.getDefRes();

				res = BaseUtils.cyr2lat(res);

				ConfigTemp.addToFile("list_temp", name + ":" + meta + ":" + res);

//               player.sendMessage(new TextComponentString(ChatForm.prefix + " Предмет " + name + " временно запрещен!"));
//               player.sendMessage(new TextComponentString(" Причина: " + BaseUtils.lat2cyr(res).replaceAll("&", "§").replaceAll("_", " ")));
				player.sendMessage(new TextComponentTranslation("list.addbtemplock.successfull", ChatForm.prefix, name));
				player.sendMessage(new TextComponentTranslation("list.addbtemplock.successfull.second", BaseUtils.lat2cyr(res).replaceAll("&", "§").replaceAll("_", " ")));
////
			} else {
				player.sendMessage(new TextComponentTranslation("error.handIsNull", ChatForm.prefix_error));
			}
		} else if (args[0].equals("mincost")) {
			if (!checkPermission(sender, "xblocker.lists.mincost"))
				return;
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR) {
				ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
				String name = is.getItem().getRegistryName().toString();
				int meta = is.getMetadata();

				if (args.length <= 1 || args.length >= 3) {
					sender.sendMessage(new TextComponentString(ChatForm.prefix_error + "/xblocker mincost <Цена>"));
					return;
				}
				String res = args[1];
				ConfigCost.addToFile("list_costs", name + ":" + meta + ":" + res);
				Main.network.sendToAll(new MessageHandler_list_cost("1;" + name + ";" + meta + ";" + res));
				// player.sendMessage(new TextComponentString(ChatForm.prefix + "Предмет " + name + "  теперь имеет минимальную цену: "+res));
				player.sendMessage(new TextComponentTranslation("list.addcost.successfull", ChatForm.prefix, name, res));
			} else {
				player.sendMessage(new TextComponentTranslation("error.handIsNull", ChatForm.prefix_error));
			}
		} else if (args[0].equals("removeCost")) {
			if (!checkPermission(sender, "xblocker.lists.removecost"))
				return;
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR) {
				ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
				String name = is.getItem().getRegistryName().toString();
				int meta = player.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
				String[] allList = ConfigCost.readFromFileToString("list_costs").split("@");
				for (int i = 0; i < allList.length; ++i) {
					String limit1 = allList[i];
					String[] res1 = limit1.split(":");
					if (!(res1.length <= 1))
						if (name.contentEquals(res1[0] + ":" + res1[1]) && meta == Integer.valueOf(res1[2])) {
							ConfigCost.deleteFromFile("list_costs", name + ":" + meta + ":" + res1[3]);
							Main.network.sendToAll(new MessageHandler_list_cost("0;" + name + ";" + meta + ";" + res1[3]));
//                       sender.sendMessage(new TextComponentString(ChatForm.prefix + "Цена у предмета " + name + " была удалена!"));
							player.sendMessage(new TextComponentTranslation("list.removecost.successfull", ChatForm.prefix, name));
						}
				}
			} else player.sendMessage(new TextComponentTranslation("error.handIsNull", ChatForm.prefix_error));
		} else if (args[0].equals("delete") || args[0].equals("remove")) {
			if (!checkPermission(sender, "xblocker.lists.delete"))
				return;
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR) {
				ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
				String name = is.getItem().getRegistryName().toString();
				int meta = player.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
				String[] allList = ConfigUtils.readFromFileToString("list").split("@");
				for (int i = 0; i < allList.length; ++i) {
					String limit1 = allList[i];
					String[] res1 = limit1.split(":");
					if (name.contentEquals(res1[0] + ":" + res1[1]) && meta == Integer.valueOf(res1[2])) {
						Main.network.sendToAll(new MessageHandler_list("0;" + name + ";" + meta + ";" + res1[3]));
						ConfigUtils.deleteFromFile("list", name + ":" + meta + ":" + res1[3]);
						ConfigCraftUtils.deleteFromFile("list_craft", name + ":" + meta);
//                       sender.sendMessage(new TextComponentString(ChatForm.prefix + "Предмет " + name + " был разрешен!"));
						player.sendMessage(new TextComponentTranslation("list.removeblock.successfull", ChatForm.prefix, name));
					}
				}
			} else player.sendMessage(new TextComponentTranslation("error.handIsNull", ChatForm.prefix_error));

		} else if (args[0].equals("deletetempblock") || args[0].equals("removetempblock")) {
			if (!checkPermission(sender, "xblocker.lists.deletetempblock"))
				return;
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR) {
				ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
				String name = is.getItem().getRegistryName().toString();
				int meta = player.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
				String[] allList = ConfigTemp.readFromFileToString("list_temp").split("@");
				for (int i = 0; i < allList.length; ++i) {
					String limit1 = allList[i];
					String[] res1 = limit1.split(":");
					if (name.contentEquals(res1[0] + ":" + res1[1]) && meta == Integer.valueOf(res1[2])) {
						ConfigTemp.deleteFromFile("list_temp", name + ":" + meta + ":" + res1[3]);
						Main.network.sendToAll(new MessageHandler_list_temp("0;" + name + ";" + meta + ";" + res1[3]));
						player.sendMessage(new TextComponentTranslation("list.removeblock.successfull", ChatForm.prefix, name));
						sender.sendMessage(new TextComponentString(ChatForm.prefix + "Предмет " + name + " был разрешен!"));

					}
				}
			} else player.sendMessage(new TextComponentTranslation("error.handIsNull", ChatForm.prefix_error));

		} else if (args[0].equals("reload")) {
			if (!checkPermission(sender, "xblocker.main.reload"))
				return;
			Main.network.sendToAll(new MessageHandler_list("2;0;0;nulled"));
			Main.network.sendToAll(new MessageHandler_list_ench("2;nulled"));
			Main.network.sendToAll(new MessageHandler_list_limited("2;0;0;0;nulled"));
			Main.network.sendToAll(new MessageHandler_list_temp("2;0;0;nulled"));
			Main.network.sendToAll(new MessageHandler_list_cost("2;0;0;nulled"));
			String listfile = ConfigUtils.readFromFileToString("list");
			String[] allList = listfile.split("@");
			for (int i = 0; i < allList.length; ++i) {
				if (!(allList.length < 1)) {
					if (listfile.contains(":")) {
						String limit1 = allList[i];
						String[] res1 = limit1.split(":");
						String name = res1[0] + ":" + res1[1];
						String meta = res1[2];
						Main.network.sendToAll(new MessageHandler_list("1;" + name + ";" + meta + ";" + res1[3]));
					}
				}
			}

			String fileenchlist = ConfigEnchUtils.readFromFileToString("list_ench");
			String[] splitench = fileenchlist.split("@");
			for (int i = 0; i < splitench.length; ++i) {
				if (!(splitench.length < 1)) {
					if (fileenchlist.contains(":")) {
						String limit1 = splitench[i];
						Main.network.sendToAll(new MessageHandler_list_ench("1;" + limit1));
					}
				}
			}

			String filelimit = ConfigLimitedUtils.readFromFileToString("list_limited");
			String[] files = filelimit.split("@");
			for (int i = 0; i < files.length; ++i) {
				if (!(files.length < 1)) {
					if (filelimit.contains(":")) {
						String limit1 = files[i];
						String[] res1 = limit1.split(":");
						String name = res1[0] + ":" + res1[1];
						String meta = res1[2];
						Main.network.sendToAll(new MessageHandler_list_limited("1;" + name + ";" + meta + ";" + res1[3] + ";" + res1[4]));
					}
				}
			}
			String filetmp = ConfigTemp.readFromFileToString("list_temp");
			String[] filestmp = filetmp.split("@");
			if (filetmp.contains(":"))
				for (String tmp : filestmp) {
					String[] res = tmp.split(":");
					String name = res[0] + ":" + res[1];
					String meta = res[2];
					Main.network.sendToAll(new MessageHandler_list_temp("1;" + name + ";" + meta + ";" + res[3]));
				}

			String filecost = ConfigCost.readFromFileToString("list_costs");
			String[] filescost = filecost.split("@");
			if (filecost.contains(":"))
				for (String tmp : filescost) {
					String[] res = tmp.split(":");
					String name = res[0] + ":" + res[1];
					String meta = res[2];
					Main.network.sendToAll(new MessageHandler_list_cost("1;" + name + ";" + meta + ";" + res[3]));
				}

			ConfigUtils.readFromFile("list");
			ConfigEnchUtils.readFromFile("list_ench");
			ConfigLimitedUtils.readFromFile("list_limited");
			ConfigTemp.readFromFile("list_temp");
			ConfigCost.readFromFile("list_costs");
			player.sendMessage(new TextComponentTranslation("list.reload.successfull", ChatForm.prefix));
		} else if (args[0].equals("removeLimit")) {
			if (!checkPermission(sender, "xblocker.lists.removeLimit"))
				return;
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR) {
				if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock) {
					String name = player.getHeldItem(EnumHand.MAIN_HAND).getItem().getRegistryName().toString();
					int meta = player.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
					String allLimited = ConfigLimitedUtils.readFromFileToString("list_limited");

					String[] res = allLimited.split("@");

					for (int limit = 0; limit < res.length; ++limit) {
						//minecraft:glowstone:0:5:Potomu-chto!
						// 0         1        2 3  4
						String[] splitStrings = res[limit].split(":");
						int splitmeta = Integer.parseInt(splitStrings[2]);
						int count = Integer.parseInt(splitStrings[3]);
						String reason = splitStrings[4];
						if (name.equals(splitStrings[0] + ":" + splitStrings[1]) && meta == splitmeta) {
							Main.network.sendToAll(new MessageHandler_list_limited("0;" + name + ";" + meta + ";" + count + ";" + reason));

							ConfigLimitedUtils.deleteFromFile("list_limited", name + ":" + meta + ":" + count + ":" + reason);
//                            sender.sendMessage(new TextComponentString(ChatForm.prefix + "Лимитированный предмет " + player.getHeldItem(EnumHand.MAIN_HAND).getDisplayName() + " - был разрешен "));
							player.sendMessage(new TextComponentTranslation("listlimit.remover.successfull", ChatForm.prefix, player.getHeldItem(EnumHand.MAIN_HAND).getDisplayName()));
							break;
						}
					}
				} else
					player.sendMessage(new TextComponentTranslation("error.isnotblock", ChatForm.prefix_error));//sender.sendMessage(new TextComponentString(ChatForm.prefix_error + "Данный предмет должен быть блоком."));
			} else player.sendMessage(new TextComponentTranslation("error.handIsNull", ChatForm.prefix_error));
		} else if (args[0].equals("limit")) {
			if (!checkPermission(sender, "xblocker.lists.limit"))
				return;
			if (args.length <= 1) {
				sender.sendMessage(new TextComponentString(ChatForm.prefix_error + "/xblocker limit <count> (res)"));
				return;
			}
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR) {
				if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock) {
					String name = player.getHeldItem(EnumHand.MAIN_HAND).getItem().getRegistryName().toString();
					int meta = player.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
					String allStrFromFile = ConfigLimitedUtils.readFromFileToString("list_limited");
					String tmpstr = "";

					for (int limit = 2; limit < args.length; ++limit) {
						tmpstr += args[limit] + " ";
					}
					if (tmpstr.equals(""))
						tmpstr = Main.config.getDefRes();

					tmpstr = BaseUtils.cyr2lat(tmpstr);
					int limit = Integer.parseInt(args[1]);
					if (limit > 0) {
						if (!allStrFromFile.contains(name + ":" + meta + ":" + limit + ":" + tmpstr)) {
							Main.network.sendToAll(new MessageHandler_list_limited("1;" + name + ";" + meta + ";" + limit + ";" + tmpstr));
							ConfigLimitedUtils.addToFile("list_limited", name + ":" + meta + ":" + limit + ":" + tmpstr);
//                           sender.sendMessage(new TextComponentString(ChatForm.prefix + "Предмет " + player.getHeldItem(EnumHand.MAIN_HAND).getDisplayName() + " ограничен ( " + limit + " )"));
//                           sender.sendMessage(new TextComponentString("Причина: " + BaseUtils.lat2cyr(tmpstr).replaceAll("&", "§").replaceAll("_", " ")));
							player.sendMessage(new TextComponentTranslation("list.limit.adding", ChatForm.prefix, player.getHeldItem(EnumHand.MAIN_HAND).getDisplayName(), limit));
							player.sendMessage(new TextComponentTranslation("list.addblock.successfull.second", BaseUtils.lat2cyr(tmpstr).replaceAll("&", "§").replaceAll("_", " ")));

						}
					} else player.sendMessage(new TextComponentTranslation("error.limitzero", ChatForm.prefix_error));
				} else player.sendMessage(new TextComponentTranslation("error.isnotblock", ChatForm.prefix_error));
			} else player.sendMessage(new TextComponentTranslation("error.handIsNull", ChatForm.prefix_error));
		} else if (args[0].equals("enchant")) {
			if (!checkPermission(sender, "xblocker.lists.enchant"))
				return;

			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEnchantedBook) {
				ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
				NBTTagList rr = (NBTTagList) is.getTagCompound().getTag("StoredEnchantments");
				if (rr != null) {
					for (NBTBase tgs : rr) {
						NBTTagCompound tmp = (NBTTagCompound) tgs;
						int id = tmp.getShort("id");
						int lvl = tmp.getShort("lvl");
						if (Enchantment.getEnchantmentByID(id) != null) {
							if (args.length == 2) {
								if (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("delete")) {
									if (ConfigEnchUtils.readFromFileToString("list_ench").contains(id + ":" + lvl)) {
										Main.network.sendToAll(new MessageHandler_list_ench("0;" + id + ":" + lvl));
										ConfigEnchUtils.deleteFromFile("list_ench", id + ":" + lvl);
//                                            player.sendMessage(new TextComponentString(ChatForm.prefix+"Зачарование " + Enchantment.getEnchantmentByID(id).getTranslatedName(lvl) + " разрешено!"));
										player.sendMessage(new TextComponentTranslation("list.enchant.removed", ChatForm.prefix, Enchantment.getEnchantmentByID(id).getTranslatedName(lvl)));
									} else
//                                            player.sendMessage(new TextComponentString(ChatForm.prefix+"Зачарование не было запрещено!"));
										player.sendMessage(new TextComponentTranslation("list.enchant.error", ChatForm.prefix, Enchantment.getEnchantmentByID(id).getTranslatedName(lvl)));
								}
							} else {
								Main.network.sendToAll(new MessageHandler_list_ench("1;" + id + ":" + lvl));
								ConfigEnchUtils.addToFile("list_ench", id + ":" + lvl);
//                                player.sendMessage(new TextComponentString(ChatForm.prefix + "Зачарование " + Enchantment.getEnchantmentByID(id).getTranslatedName(lvl) + " запрещено!"));
								player.sendMessage(new TextComponentTranslation("list.enchant.added", ChatForm.prefix, Enchantment.getEnchantmentByID(id).getTranslatedName(lvl)));
							}
						}
					}
				}
			} else
				player.sendMessage(new TextComponentTranslation("list.enchant.erradded", ChatForm.prefix_error));//player.sendMessage(new TextComponentString(ChatForm.prefix_error+"Возьмите книгу зачарований в руку."));
		} else if (args[0].equals("craft")) {
			if (!checkPermission(sender, "xblocker.lists.craft"))
				return;
			int meta = player.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.AIR) {
				ItemStack is = player.getHeldItem(EnumHand.MAIN_HAND);
				String name = is.getItem().getRegistryName().toString();

				String allFTS = ConfigCraftUtils.readFromFileToString("list_craft");
				if (!allFTS.contains("@" + name + ":" + meta) && !allFTS.contains(name + ":" + meta + "@") && !allFTS.equals(name + ":" + meta)) {
					ConfigCraftUtils.addToFile("list_craft", name + ":" + meta);
					ServerProxy.removeCraft();
//                   sender.sendMessage(new TextComponentString(ChatForm.prefix+"Крафт " + new ItemStack(Item.getByNameOrId(name),1,meta).getDisplayName() + " запрещен!"));
					player.sendMessage(new TextComponentTranslation("list.craft.added", ChatForm.prefix, new ItemStack(Item.getByNameOrId(name), 1, meta).getDisplayName()));

				} else {
					ConfigCraftUtils.deleteFromFile("list_craft", name + ":" + meta);
//                   sender.sendMessage(new TextComponentString(ChatForm.prefix+"Крафт " + new ItemStack(Item.getByNameOrId(name),1,meta).getDisplayName() + " разрешен!"));
					player.sendMessage(new TextComponentTranslation("list.enchant.removed", ChatForm.prefix, new ItemStack(Item.getByNameOrId(name), 1, meta).getDisplayName()));
				}
			} else player.sendMessage(new TextComponentTranslation("error.handIsNull", ChatForm.prefix_error));
		} else if (args[0].equals("perms")) {
			if (!checkPermission(sender, "xblocker.main.perms"))
				return;
			sender.sendMessage(new TextComponentString(ChatForm.prefix + "Perms:\n" +
					"Main:\n" +
					"xblocker.main.help\n" +
					"xblocker.main.reload\n" +
					"xblocker.main.perms\n" +
					"Lists:\n" +
					"xblocker.lists.add\n" +
					"xblocker.lists.tempblock\n" +
					"xblocker.lists.mincost\n" +
					"xblocker.lists.delete\n" +
					"xblocker.lists.deletetempblock\n" +
					"xblocker.lists.removeLimit\n" +
					"xblocker.lists.limit\n" +
					"xblocker.lists.enchant\n" +
					"xblocker.lists.craft" +
					"Blocks:\n" +
					"xblocker.interact." + Main.config.getServerName() + ".name:meta\n" +
					"xblocker." + Main.config.getServerName() + ".name:meta\n" +
					"xblocker.attack.name:meta\n" +
					"xblocker.drop." + Main.config.getServerName() + ".name:meta\n" +
					"xblocker.clear." + Main.config.getServerName() + ".name:meta\n" +
					"xblocker." + Main.config.getServerName() + ".limit.name:meta\n" +
					"xblocker.enchant\n" +
					"xblocker.getAll.perm"));

		} else sender.sendMessage(new TextComponentString(ChatForm.prefix + usage));
	}

	@GradleSideOnly(GradleSide.SERVER)
	private boolean checkPermission(ICommandSender sender, String permkey) {
		if (sender.canUseCommand(4, "xblocker.getAll.perm") || sender.canUseCommand(4, permkey))
			return true;
		else {
//           sender.sendMessage(new TextComponentString(ChatForm.prefix_error+TextFormatting.RED+"You do not have the right to command!"));
			sender.sendMessage(new TextComponentTranslation("error.perm", ChatForm.prefix_error));
			return false;
		}
	}
}
