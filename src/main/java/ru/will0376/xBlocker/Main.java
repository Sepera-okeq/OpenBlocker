package ru.will0376.xBlocker;

import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.xBlocker.common.CommonProxy;
import ru.will0376.xBlocker.common.Config;
import ru.will0376.xBlocker.common.ItemList;
import ru.will0376.xBlocker.common.JsonHelper;
import ru.will0376.xBlocker.server.IO;
import ru.will0376.xBlocker.server.comands.ComandsMain;

import java.io.File;
import java.util.ArrayList;

@Mod(
		modid = Main.MODID,
		name = Main.NAME,
		version = Main.VERSION,
		acceptedMinecraftVersions = "[1.12.2]"
)
public class Main {
	public static final String MODID = "xblocker";
	public static final String NAME = "xBlocker";
	public static final String VERSION = "1.0.7";
	public static boolean debug = true;
	public static Config config;
	public static File configFile;
	public static SimpleNetworkWrapper network;
	public static Logger Logger;
	public static Main Instance;
	@Mod.Instance
	private static Main mod;
	@SidedProxy(
			clientSide = "ru.will0376.xBlocker.client.ClientProxy",
			serverSide = "ru.will0376.xBlocker.server.ServerProxy"
	)
	public static CommonProxy proxy;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		Logger = event.getModLog();
		if(event.getSide().isServer() || debug) {
			IO.path = new File(event.getModConfigurationDirectory().getAbsolutePath().trim() +File.separator+ "xblocker");
			IO.fileJson = new File(IO.path+File.separator+"config.json");
			JsonHelper.init();
		}

		configFile = event.getSuggestedConfigurationFile();
		config = new Config(configFile);
		config.launch();
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.events(event);
		Instance = this;
	}

	@GradleSideOnly(GradleSide.SERVER)
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		serverInit(event);
	}

	@EventHandler
	public void onServerStartingOnClient(FMLServerStartingEvent event){

	}

	@GradleSideOnly(GradleSide.SERVER)
	private void serverInit(FMLServerStartingEvent event) {
	event.registerServerCommand(new ComandsMain());
	}

}
