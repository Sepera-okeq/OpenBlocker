package ru.will0376.OpenBlocker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.justagod.cutter.invoke.Invoke;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.CommonProxy;
import ru.will0376.OpenBlocker.common.Config;
import ru.will0376.OpenBlocker.common.net.ToClientBlocked;
import ru.will0376.OpenBlocker.common.utils.FlagData;
import ru.will0376.OpenBlocker.common.utils.FlagDataAdapter;
import ru.will0376.OpenBlocker.common.utils.ItemStackAdapter;
import ru.will0376.OpenBlocker.server.IO;
import ru.will0376.OpenBlocker.server.commands.CommandMain;
import ru.will0376.OpenBlocker.server.tileentity.TileEntityChecker;

import java.io.File;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class Main {
	public static final String MODID = "openblocker";
	public static final String NAME = "OpenBlocker";
	public static final String VERSION = "@version@";
	public static boolean debug = true, server = true;
	public static Config config;
	public static File configFile;
	public static SimpleNetworkWrapper network;
	public static Logger Logger;

	@Mod.Instance
	public static Main Instance;
	@SidedProxy(clientSide = "ru.will0376.OpenBlocker.client.ClientProxy", serverSide = "ru.will0376.OpenBlocker.server.ServerProxy")
	public static CommonProxy proxy;
	public static Gson gson = new GsonBuilder().setPrettyPrinting()
			.registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
			.registerTypeAdapter(FlagData.class, new FlagDataAdapter())
			.create();

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		if (debug) debug = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		Logger = event.getModLog();
		server = event.getSide().isServer();
		if (server || debug) Invoke.server(() -> serverPreInt(event));
		proxy.preInit(event);
	}

	@GradleSideOnly(GradleSide.SERVER)
	private void serverPreInt(FMLPreInitializationEvent event) {
		IO.path = new File(event.getModConfigurationDirectory().getAbsolutePath().trim(), "OpenBlocker");
		IO.fileJson = new File(IO.path, "config_new.json");
		BlockHelper.init();
		configFile = event.getSuggestedConfigurationFile();
		config = new Config(configFile);
		config.launch();
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		Main.network.registerMessage(new ToClientBlocked(), ToClientBlocked.class, 1, Side.CLIENT);
		proxy.init(event);
	}

	@GradleSideOnly(GradleSide.SERVER)
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandMain());
	}

	@GradleSideOnly(GradleSide.SERVER)
	@EventHandler
	public void onServerStarted(FMLServerStartedEvent event) {
		TileEntityChecker.loadTEs();

	}

	@GradleSideOnly(GradleSide.SERVER)
	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {
		BlockHelper.save();
	}
}
