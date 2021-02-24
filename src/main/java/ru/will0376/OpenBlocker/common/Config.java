package ru.will0376.OpenBlocker.common;

import lombok.Getter;
import net.minecraftforge.common.config.Configuration;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.Main;

import java.io.File;
import java.util.Arrays;

import static ru.will0376.OpenBlocker.common.Config.BlockStorage.FileSystem;

@Getter
@GradleSideOnly(GradleSide.SERVER)

public class Config {

	private final Configuration configuration;
	public static String GENERAL = "general";
	public static String DATABASE = "database";
	private String defRes;
	private boolean deleteBlocked;
	private boolean enablePickupHandler;
	private BlockStorage blockStorage;
	private String dbURL;
	private String dbUsername;
	private String dbPassword;
	private String dbTable;

	public static Config get() {
		return Main.config;
	}

	public Config(File file) {
		this.configuration = new Configuration(file);
	}

	public boolean isDeleteBlocked() {
		return deleteBlocked;
	}

	public Config launch() {
		this.load();
		this.setGeneralCategory();
		this.setDataBaseCategory();
		this.save();
		return this;
	}

	private void load() {
		this.configuration.load();
	}

	private void save() {
		this.configuration.save();
	}

	private void setGeneralCategory() {
		this.defRes = this.configuration.getString("defRes", GENERAL, "<~ причина ~>", "Default Reason");
		this.deleteBlocked = this.configuration.getBoolean("deleteBlocked", GENERAL, true, "Remove blocked items from inventory");
		this.enablePickupHandler = this.configuration.getBoolean("enablePickupHandler", GENERAL, true, "");
		this.blockStorage = BlockStorage.valueOf(configuration.getString("storage", GENERAL, FileSystem.name(), Arrays.toString(BlockStorage
				.values())));
	}

	public void setDataBaseCategory() {
		this.dbURL = this.configuration.getString("dbURL", DATABASE, "jdbc:mysql://localhost:3306/test", "Host URL");
		this.dbUsername = this.configuration.getString("dbUsername", DATABASE, "root", "DB username");
		this.dbPassword = this.configuration.getString("dbPassword", DATABASE, "root", "DB password");
		this.dbTable = this.configuration.getString("dbTable", DATABASE, "OpenBlocker", "DB table");
	}

	public String getDefRes() {
		return defRes;
	}

	public boolean isEnablePickupHandler() {
		return enablePickupHandler;
	}

	public boolean isBD() {
		return blockStorage != FileSystem;
	}

	@Getter
	public enum BlockStorage {
		FileSystem(),
		Mysql()
	}
}
