package ru.will0376.OpenBlocker.common;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.net.ToClientBlocked;
import ru.will0376.OpenBlocker.common.utils.B64;
import ru.will0376.OpenBlocker.server.database.FileSystem;
import ru.will0376.OpenBlocker.server.database.Mysql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public enum BlockHelper {
	Instance;
	public List<Blocked> blockedList = new ArrayList<>();

	public static Blocked findBlockedByStack(ItemStack is) {
		if (is.isEmpty()) return null;
		for (Blocked blocked : (FMLCommonHandler.instance()
				.getSide()
				.isServer() ? Instance.blockedList : BlockHelperClient.blockedListClient)) {
			ItemStack stack = blocked.getStack();
			if ((blocked.getAllMeta() && stack.getItem() == is.getItem()) || stack.isItemEqual(is)) return blocked;
		}
		return null;
	}

	public static Blocked findBlockedByEnch(int id, int lvl) {
		for (Blocked blocked : (FMLCommonHandler.instance()
				.getSide()
				.isServer() ? Instance.blockedList : BlockHelperClient.blockedListClient)) {
			if (blocked.getStatus().contains(Blocked.Status.Enchant)) {
				if (id == blocked.getEnchantedID() && lvl == blocked.getEnchantedLVL()) return blocked;
			}
		}
		return null;
	}

	public static void sendMessageToPlayer(EntityPlayerMP player, Blocked blocked, ToClientBlocked.Action action) {
		Main.network.sendTo(ToClientBlocked.builder().action(action).blocked(blocked).build(), player);
	}

	public static void sendToAllClient(Blocked blocked, ToClientBlocked.Action action) {
		for (EntityPlayerMP player : FMLCommonHandler.instance()
				.getMinecraftServerInstance()
				.getPlayerList()
				.getPlayers()) {
			sendMessageToPlayer(player, blocked, action);
		}
	}

	public static void sendAllToClient(EntityPlayerMP player, ToClientBlocked.Action action) {
		sendMessageToPlayer(player, null, ToClientBlocked.Action.ClearList);
		Instance.blockedList.forEach(e -> sendMessageToPlayer(player, e, action));
	}

	public static void sendAllBlocksToAll() {
		for (EntityPlayerMP player : FMLCommonHandler.instance()
				.getMinecraftServerInstance()
				.getPlayerList()
				.getPlayers()) {
			sendAllToClient(player, ToClientBlocked.Action.AddBlock);
		}
	}

	public static List<Blocked> getAllByStatus(Blocked.Status status) {
		return FMLCommonHandler.instance().getSide().isServer() ? Instance.blockedList.stream()
				.filter(e -> e.status.contains(status))
				.collect(Collectors.toList()) : BlockHelperClient.blockedListClient.stream()
				.filter(e -> e.status.contains(status))
				.collect(Collectors.toList());
	}

	public static void addNewBlocked(Blocked blocked) {
		if (blocked.getStack().getItem() == Items.AIR) return;
		if (FMLCommonHandler.instance().getSide().isServer()) {
			Instance.blockedList.add(blocked);
			sendToAllClient(blocked, ToClientBlocked.Action.AddBlock);
		} else {
			BlockHelperClient.blockedListClient.add(blocked);
		}
	}

	public static void removeStatus(Blocked blocked, Blocked.Status status) {
		blocked.getStatus().remove(status);

		if (blocked.getStatus().isEmpty()) removeBlocked(blocked);
		else sendToAllClient(blocked, ToClientBlocked.Action.ReloadBlock);
	}

	public static void addStatus(Blocked blocked, Blocked.Status status) {
		if (blocked.getStack().getItem() == Items.AIR) return;

		blocked.addStatus(status);
		sendToAllClient(blocked, ToClientBlocked.Action.ReloadBlock);
	}

	public static void removeBlocked(Blocked blocked) {
		if (FMLCommonHandler.instance().getSide().isServer()) {
			Instance.blockedList.removeIf(e -> e.getStack().isItemEqual(blocked.getStack()));
			sendToAllClient(blocked, ToClientBlocked.Action.RemoveBlock);
		} else {
			BlockHelperClient.blockedListClient.removeIf(e -> e.getStack().isItemEqual(blocked.getStack()));
		}
	}

	public static boolean checkNBT(ItemStack is) {
		NBTTagCompound nbtTagCompound = is.writeToNBT(new NBTTagCompound());
		String encode = B64.encode(nbtTagCompound.toString());
		for (Blocked blocked : (FMLCommonHandler.instance()
				.getSide()
				.isServer() ? Instance.blockedList : BlockHelperClient.blockedListClient)) {
			if (!blocked.NBTEmpty() && blocked.getNbt().equals(encode)) return true;
		}
		return false;
	}

	@GradleSideOnly(GradleSide.SERVER)
	public static void init() throws Exception {
		Instance.blockedList = new ArrayList<>();
		if (Main.config.isBD()) {
			switch (Config.get().getBlockStorage()) {
				case Mysql:
					Main.storage = new Mysql();
					break;
				default:
					throw new Exception("Storage not found");
			}
		} else {
			Main.storage = new FileSystem();
		}
		Main.storage.load();
	}

	@GradleSideOnly(GradleSide.SERVER)
	public static void save() throws SQLException {
		Main.storage.save();
	}

	@Data
	public static class BlockHelperClient {
		public static List<Blocked> blockedListClient = new ArrayList<>();
	}
}
