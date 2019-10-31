package ru.will0376.OpenBlocker;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import ru.justagod.mineplugin.GradleSide;
import ru.justagod.mineplugin.GradleSideOnly;
import ru.will0376.OpenBlocker.common.Blocker;
import ru.will0376.OpenBlocker.common.CommonProxy;
import ru.will0376.OpenBlocker.common.Config;
import ru.will0376.OpenBlocker.common.JsonHelper;
import ru.will0376.OpenBlocker.server.IO;
import ru.will0376.OpenBlocker.server.comands.ComandsMain;

import java.io.File;

@Mod(
		modid = Main.MODID,
		name = Main.NAME,
		version = Main.VERSION,
		acceptedMinecraftVersions = "[1.12.2]"
)
public class Main {
	public static final String MODID = "openblocker";
	public static final String NAME = "OpenBlocker";
	public static final String VERSION = "1.0.7";
	public static boolean debug = true;
	public static Config config;
	public static File configFile;
	public static SimpleNetworkWrapper network;
	public static Logger Logger;
	public static Main Instance;
	@SidedProxy(
			clientSide = "ru.will0376.OpenBlocker.client.ClientProxy",
			serverSide = "ru.will0376.OpenBlocker.server.ServerProxy"
	)
	public static CommonProxy proxy;
	@Mod.Instance
	private static Main mod;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		Logger = event.getModLog();
		if (event.getSide().isServer() || debug) {
			IO.path = new File(event.getModConfigurationDirectory().getAbsolutePath().trim() + File.separator + "OpenBlocker");
			IO.fileJson = new File(IO.path + File.separator + "config.json");
			JsonHelper.init();
		}

		configFile = event.getSuggestedConfigurationFile();
		config = new Config(configFile);
		config.launch();
		proxy.preInit(event);
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		Main.network.registerMessage(new Blocker(), Blocker.class, 1, Side.CLIENT);
		proxy.init(event);
		Instance = this;
	}

	@GradleSideOnly(GradleSide.SERVER)
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		serverInit(event);
	}

	@EventHandler
	public void onServerStartingOnClient(FMLServerStartingEvent event) {

	}

	@GradleSideOnly(GradleSide.SERVER)
	private void serverInit(FMLServerStartingEvent event) {
		event.registerServerCommand(new ComandsMain());
	}

}
