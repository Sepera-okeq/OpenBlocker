package ru.will0376.xBlocker.server.comands;

import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import ru.will0376.xBlocker.common.ChatForm;
import ru.will0376.xBlocker.common.JsonHelper;

public class CommandEnchant {
	private String usage = Base.usage+"enchant <reason>\n" +
			"if enchant already blocked - it will be deleted\n";
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack is = player.getHeldItemMainhand();
		NBTTagList nbts = (NBTTagList) is.getTagCompound().getTag("StoredEnchantments");
		if (nbts != null) {
			for (NBTBase tgs : nbts) {
				NBTTagCompound tmp = (NBTTagCompound) tgs;
				int id = tmp.getShort("id");
				int lvl = tmp.getShort("lvl");
				if(JsonHelper.contains(JsonHelper.ENCHANT,id+":"+lvl)) {
					JsonHelper.removeFromServer(JsonHelper.ENCHANT,id+":"+lvl);
					player.sendMessage(new TextComponentString(ChatForm.prefix+"Enchant has UnBlocked!"));
				}
				else {
					JsonObject jo = new JsonObject();
					String reason = "";
					for (int i = 1; i < args.length; i++)
						reason+=args[i]+" ";
					jo.addProperty("reason",reason);
					JsonHelper.addServer(jo,JsonHelper.ENCHANT,id+":"+lvl);
					player.sendMessage(new TextComponentString(ChatForm.prefix+"Enchant has Blocked!"));
				}
			}
		}
	}

	public void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(usage));
	}
}
