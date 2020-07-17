package ru.will0376.OpenBlocker;

import net.minecraft.launchwrapper.Launch;
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
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.justagod.cutter.invoke.Invoke;
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
	public static final String VERSION = "1.1.3";
	public static boolean debug = true, server = false;
	public static Config config;
	public static File configFile;
	public static SimpleNetworkWrapper network;
	public static Logger Logger;
	@Mod.Instance
	public static Main Instance;
	@SidedProxy(
			clientSide = "ru.will0376.OpenBlocker.client.ClientProxy",
			serverSide = "ru.will0376.OpenBlocker.server.ServerProxy"
	)
	public static CommonProxy proxy;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		if (debug) debug = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		Logger = event.getModLog();
		server = event.getSide().isServer();
		if (server || debug)
			Invoke.server(() -> serverPreInt(event));
		proxy.preInit(event);
	}

	@GradleSideOnly(GradleSide.SERVER)
	private void serverPreInt(FMLPreInitializationEvent event) {
		IO.path = new File(event.getModConfigurationDirectory().getAbsolutePath().trim() + File.separator + "OpenBlocker");
		IO.fileJson = new File(IO.path + File.separator + "config.json");
		JsonHelper.init();
		configFile = event.getSuggestedConfigurationFile();
		config = new Config(configFile);
		config.launch();
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		Main.network.registerMessage(new Blocker(), Blocker.class, 1, Side.CLIENT);
		proxy.init(event);
	}

	@GradleSideOnly(GradleSide.SERVER)
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new ComandsMain());
	}
}
