package ru.will0376.xBlocker;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.command.CommandHandler;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
import ru.will0376.xBlocker.client.commands.ClientComma;
import ru.will0376.xBlocker.client.network.*;
import ru.will0376.xBlocker.common.CommonProxy;
import ru.will0376.xBlocker.common.Config;
import ru.will0376.xBlocker.common.ItemData;
import ru.will0376.xBlocker.common.ItemList;
import ru.will0376.xBlocker.server.ServerProxy;
import ru.will0376.xBlocker.server.commands.Command;
import ru.will0376.xBlocker.server.events.EnchantEvent;
import ru.will0376.xBlocker.server.events.JoinEvent;
import ru.will0376.xBlocker.server.events.LimitedEvent;
import ru.will0376.xBlocker.server.events.LokiEvent;
import ru.will0376.xBlocker.server.network.PluginMessage;
import ru.will0376.xBlocker.server.utils.cfg.*;

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
	public static final boolean FOR_SERVER = true;
	public ArrayList<String> listItemsClient = Lists.newArrayList();
	public ArrayList<String> listTempClient = Lists.newArrayList();
	public ArrayList<String> listEnchantClient = Lists.newArrayList();
	public ArrayList<String> listLimitedClient = Lists.newArrayList();
	public ArrayList<String> listCostClient = Lists.newArrayList();
	private ItemList listItems = new ItemList();
	private ItemList listTemp = new ItemList();
	private ItemList listCost = new ItemList();
	private ArrayList listEnchant = Lists.newArrayList();
	private ArrayList listLimited = Lists.newArrayList();
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
		configFile = event.getSuggestedConfigurationFile();
		config = new Config(configFile);
		config.launch();
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		Main.network.registerMessage(new MessageHandler_list(), MessageHandler_list.class, 0, Side.CLIENT);
		Main.network.registerMessage(new MessageHandler_list_ench(), MessageHandler_list_ench.class, 1, Side.CLIENT);
		Main.network.registerMessage(new MessageHandler_list_limited(), MessageHandler_list_limited.class, 2, Side.CLIENT);
		Main.network.registerMessage(new MessageHandler_list_temp(), MessageHandler_list_temp.class, 3, Side.CLIENT);
		Main.network.registerMessage(new MessageHandler_list_cost(), MessageHandler_list_cost.class, 4, Side.CLIENT);
		Main.network.registerMessage(new PluginMessage(), PluginMessage.class, 5, Side.SERVER);
		proxy.events(event);
		Instance = this;
	}

	@GradleSideOnly(GradleSide.SERVER)
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		serverInit(event);
	}

	@EventHandler
	public void onServerStartingOnClient(FMLServerStartingEvent event) {
		event.registerServerCommand(new ClientComma());
	}

	@GradleSideOnly(GradleSide.SERVER)
	private void serverInit(FMLServerStartingEvent event) {
		event.registerServerCommand(new Command());
		ConfigUtils.readFromFile("list");
		ConfigEnchUtils.readFromFile("list_ench");
		ConfigLimitedUtils.readFromFile("list_limited");
		ConfigPlaceUtils.readFromFileToString("list_protect");
		ConfigTemp.readFromFile("list_temp");
		ConfigCost.readFromFile("list_costs");

		ServerProxy.removeCraft();
		ItemList il = Main.getInstance().getListItems();
		if (il != null && !il.isEmpty() && il.size() != 0) {
			for (int i = 0; i < il.size(); ++i) {
				ItemData check = Main.getInstance().getListItems().get(i);
				String name = check.name;
				int meta = check.meta;
				if (Item.getByNameOrId(name) != Items.AIR) {
					ItemStack is = new ItemStack(Item.getByNameOrId(name), 1, meta);
					if (is != null && Item.getItemFromBlock(Block.getBlockFromName(name)) == is.getItem()) {
						ServerProxy.removeCraftingRecipe(is, meta);
					}
				}
			}
		}
		String filePath = "./config/xBlocker";
		if (!(new File(filePath)).exists()) {
			(new File(filePath)).mkdir();
		}


		EnchantEvent enchant = new EnchantEvent();
		MinecraftForge.EVENT_BUS.register(enchant);

		JoinEvent join = new JoinEvent();
		MinecraftForge.EVENT_BUS.register(join);

		LokiEvent loki = new LokiEvent();
		MinecraftForge.EVENT_BUS.register(loki);

		LimitedEvent limited = new LimitedEvent();
		MinecraftForge.EVENT_BUS.register(limited);

	}

	public static Main getInstance() {
		return Instance;
	}

	public ItemList getListItems() {
		return listItems;
	}

	public ArrayList getListEnchant() {
		return listEnchant;
	}

	public ItemList getListTemp() {
		return listTemp;
	}

	public ItemList getListCost() {
		return listCost;
	}

	public ArrayList getListLimited() {
		return listLimited;
	}
}
