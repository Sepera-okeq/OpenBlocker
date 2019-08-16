package ru.will0376.xBlocker.server.events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.xBlocker.Main;
import ru.will0376.xBlocker.client.network.*;
import ru.will0376.xBlocker.server.utils.cfg.ConfigEnchUtils;
import ru.will0376.xBlocker.server.utils.cfg.ConfigLimitedUtils;
import ru.will0376.xBlocker.server.utils.cfg.ConfigTemp;
import ru.will0376.xBlocker.server.utils.cfg.ConfigUtils;

@GradleSideOnly(GradleSide.SERVER)
public class JoinEvent {
	@SubscribeEvent
	public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		try {
			Main.network.sendTo(new MessageHandler_list("2;0;0;0"), (EntityPlayerMP) event.player);
			Main.network.sendTo(new MessageHandler_list_ench("2;0"), (EntityPlayerMP) event.player);
			Main.network.sendTo(new MessageHandler_list_limited("2;0;0;0;0"), (EntityPlayerMP) event.player);
			Main.network.sendTo(new MessageHandler_list_temp("2;0;0;0"), (EntityPlayerMP) event.player);
			Main.network.sendTo(new MessageHandler_list_cost("2;0;0;0"), (EntityPlayerMP) event.player);

			String filelist = ConfigUtils.readFromFileToString("list");
			String[] allList = filelist.split("@");
			for (int i = 0; i < allList.length; ++i) {
				if (!(allList.length < 1)) {
					if (filelist.contains(":")) {
						String limit1 = allList[i];
						String[] res1 = limit1.split(":");
						String name = res1[0] + ":" + res1[1];
						String meta = res1[2];
						if (event.player instanceof EntityPlayerMP)
							Main.network.sendTo(new MessageHandler_list("1;" + name + ";" + meta + ";" + res1[3]), (EntityPlayerMP) event.player);
						Main.network.sendTo(new MessageHandler_list(3 + ";" + name + ";" + meta + ";" + res1[3]), (EntityPlayerMP) event.player);
					}
				}
			}
			String filelist2 = ConfigEnchUtils.readFromFileToString("list_ench");
			String[] allList2 = filelist2.split("@");
			for (int i = 0; i < allList2.length; ++i) {
				if (!(allList2.length < 1)) {
					if (filelist2.contains(":")) {
						String limit1 = allList2[i];
						Main.network.sendTo(new MessageHandler_list_ench("1;" + limit1), (EntityPlayerMP) event.player);
					}
				}
			}
			String filelist3 = ConfigLimitedUtils.readFromFileToString("list_limited");
			String[] info3 = filelist3.split("@");
			for (int i = 0; i < info3.length; ++i) {
				if (!(info3.length < 1)) {
					if (filelist3.contains(":")) {
						String limit1 = info3[i];
						String[] res1 = limit1.split(":");
						String name = res1[0] + ":" + res1[1];
						String meta = res1[2];
						if (event.player instanceof EntityPlayerMP)
							Main.network.sendTo(new MessageHandler_list_limited("1;" + name + ";" + meta + ";" + res1[3] + ";" + res1[4]), (EntityPlayerMP) event.player);
					}
				}
			}
			String filelist4 = ConfigTemp.readFromFileToString("list_temp");
			String[] info4 = filelist4.split("@");
			if (filelist4.contains(":"))
				for (String tmp : info4) {
					String[] intmp = tmp.split(":");
					String name = intmp[0] + ":" + intmp[1];
					String meta = intmp[2];
					if (event.player instanceof EntityPlayerMP)
						Main.network.sendTo(new MessageHandler_list_temp("1;" + name + ";" + meta + ";" + intmp[3]), (EntityPlayerMP) event.player);
				}
			String filelist5 = ConfigTemp.readFromFileToString("list_costs");
			String[] info5 = filelist5.split("@");
			if (filelist5.contains(":"))
				for (String tmp : info5) {
					String[] intmp = tmp.split(":");
					String name = intmp[0] + ":" + intmp[1];
					String meta = intmp[2];
					if (event.player instanceof EntityPlayerMP)
						Main.network.sendTo(new MessageHandler_list_cost("1;" + name + ";" + meta + ";" + intmp[3]), (EntityPlayerMP) event.player);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
