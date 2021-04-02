package ru.will0376.OpenBlocker.server.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.cli.CommandLine;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;
import ru.will0376.OpenBlocker.common.utils.B64;
import ru.will0376.OpenBlocker.common.utils.ChatForm;
import ru.will0376.OpenBlocker.common.utils.FlagData;

import java.util.Arrays;
import java.util.List;

@GradleSideOnly(GradleSide.SERVER)
public class BlockCommand extends CommandAbstract {
	public BlockCommand() {
		super("block");
	}

	@Override
	public void add(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
		CommandLine parse = getLine(args);

		EntityPlayer player = (EntityPlayer) sender;
		ItemStack itemStack = player.getHeldItemMainhand().copy();


		if (itemStack.isEmpty()) {
			if (parse.hasOption("useItem")) {
				String optionValue = parse.getOptionValue("useItem");
				Item useItem = Item.getByNameOrId(optionValue);
				if (useItem == null) {
					throw new NullPointerException(String.format("Item %s not found", optionValue));
				}
				itemStack = new ItemStack(useItem, 1, Integer.parseInt(parse.getOptionValue("useItemMeta", "0")));
			} else {
				sender.sendMessage(new TextComponentString(ChatForm.prefix_warring + "Take a subject in a hand"));
				return;
			}
		}
		itemStack.setCount(1);
		Blocked blockedByStack = BlockHelper.findBlockedByStack(itemStack);

		if (blockedByStack == null) blockedByStack = Blocked.builder()
				.stack(itemStack)
				.reason(parse.getOptionValue("reason", Main.config.getDefRes()))
				.build()
				.addStatus(Blocked.Status.Blocked);

		if (blockedByStack.getNbt() != null && !blockedByStack.getNbt().isEmpty() && !blockedByStack.getNbt()
				.equals("null")) blockedByStack = (Blocked) blockedByStack.clone();


		if (!blockedByStack.getStatus().contains(Blocked.Status.Blocked))
			blockedByStack.getStatus().add(Blocked.Status.Blocked);

		if (parse.hasOption("temp")) blockedByStack.addNewFlag(FlagData.Flag.Temp, true);
		if (parse.hasOption("allMeta")) blockedByStack.addNewFlag(FlagData.Flag.AllMeta, true);
		if (parse.hasOption("disableBox")) blockedByStack.addNewFlag(FlagData.Flag.DisableBox, true);
		if (parse.hasOption("tile")) blockedByStack.addNewFlag(FlagData.Flag.Tile, true);
		if (parse.hasOption("interaction")) blockedByStack.addNewFlag(FlagData.Flag.Interaction, true);
		if (parse.hasOption("useNbt")) {
			NBTTagCompound nbtTagCompound = itemStack.writeToNBT(new NBTTagCompound());
			blockedByStack.setNbt(B64.encode(nbtTagCompound.toString()));
		}

		BlockHelper.addNewBlocked(blockedByStack);
		sender.sendMessage(new TextComponentString("Done!"));
		BlockHelper.save();
	}

	@Override
	public void remove(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
		CommandLine parse = getLine(args);

		EntityPlayer player = (EntityPlayer) sender;
		ItemStack itemStack = player.getHeldItemMainhand();

		if (itemStack.isEmpty() && parse.hasOption("useItem")) {
			String optionValue = parse.getOptionValue("useItem");
			Item useItem = Item.getByNameOrId(optionValue);
			if (useItem == null) {
				throw new NullPointerException(String.format("Item %s not found", optionValue));
			}
			itemStack = new ItemStack(useItem, 1, (parse.hasOption("useItemMeta") ? Integer.parseInt(parse.getOptionValue("useItemMeta")) : 0));
		} else if (itemStack.isEmpty()) {
			sender.sendMessage(new TextComponentString(ChatForm.prefix_warring + "Take a subject in a hand"));
			return;
		}

		Blocked blockedByStack = BlockHelper.findBlockedByStack(itemStack);

		if (blockedByStack != null) BlockHelper.removeStatus(blockedByStack, Blocked.Status.Blocked);

		sender.sendMessage(new TextComponentString("Done!"));
		BlockHelper.save();
	}

	@Override
	public List<Argument> getArgList() {
		return Arrays.asList(Argument.builder()
				.name("allMeta")
				.desc("блокирует все метадаты")
				.build(), Argument.builder()
				.name("reason")
				.desc("причина блокировки")
				.hasArg(true)
				.build(), Argument.builder().name("temp").desc("помечает предмет временно заблокированным")
				.build(), Argument.builder()
				.name("disableBox")
				.desc("отключает выделение блока в мире")
				.build(), Argument.builder().name("useNbt").desc("использовать НБТ").build(), Argument.builder()
				.name("useTile")
				.desc("Тест")
				.build(), Argument.builder()
				.name("useItem")
				.desc("использовать предмет из строки")
				.hasArg(true)
				.build(), Argument.builder().name("useItemMeta").desc("использовать мету из строки")
				.hasArg(true)
				.build(), Argument.builder().name("interaction").desc("разрешает использовать блок в мире").build());
	}
}
