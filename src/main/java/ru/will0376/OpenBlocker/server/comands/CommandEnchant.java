package ru.will0376.OpenBlocker.server.comands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;

@GradleSideOnly(GradleSide.SERVER)
public class CommandEnchant {
	private final String usage = Base.usage + "enchant <reason>\n" + "if enchant already blocked - it will be deleted\n";

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		NBTTagList nbts = (NBTTagList) is.getTagCompound().getTag("ench");
		if (is.isItemEnchanted()) {
			for (NBTBase tgs : nbts) {
				NBTTagCompound tmp = (NBTTagCompound) tgs;
				int id = tmp.getShort("id");
				int lvl = tmp.getShort("lvl");
				Blocked blockedByEnch = BlockHelper.findBlockedByEnch(id, lvl);
				if (blockedByEnch != null) {
					if (blockedByEnch.getStatus().contains(Blocked.Status.Enchant))
						BlockHelper.removeStatus(blockedByEnch, Blocked.Status.Enchant);
					else BlockHelper.addStatus(blockedByEnch, Blocked.Status.Enchant);
				} else {
					StringBuilder reason = new StringBuilder();
					for (int i = 1; i < args.length; i++)
						reason.append(args[i]).append(" ");
					Blocked blocked = Blocked.builder()
							.reason(reason.toString())
							.stack(ItemStack.EMPTY)
							.build()
							.addStatus(Blocked.Status.Enchant);
					BlockHelper.addNewBlocked(blocked);
				}
				BlockHelper.save();

			}
		}
	}

	public void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(usage));
	}
}
