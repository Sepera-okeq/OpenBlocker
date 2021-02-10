package ru.will0376.OpenBlocker.common;

import net.minecraftforge.common.config.Configuration;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.io.File;

public class Config {

	private final Configuration configuration;

	private String defRes;
	private boolean deleteBlocked;
	private boolean enablePickupHandler;

	public Config(File file) {
		this.configuration = new Configuration(file);
	}

	@GradleSideOnly(GradleSide.SERVER)
	public boolean isDeleteBlocked() {
		return deleteBlocked;
	}

	@GradleSideOnly(GradleSide.SERVER)
	public void launch() {
		this.load();
		this.setConfigs();
		this.save();
	}

	@GradleSideOnly(GradleSide.SERVER)
	private void load() {
		this.configuration.load();
	}

	@GradleSideOnly(GradleSide.SERVER)
	private void save() {
		this.configuration.save();
	}

	@GradleSideOnly(GradleSide.SERVER)
	private void setConfigs() {
		this.defRes = this.configuration.getString("defRes", "general", "Потому-что!", "Default Reason");
		this.deleteBlocked = this.configuration.getBoolean("deleteBlocked", "general", true, "Remove blocked items from inventory");
		this.enablePickupHandler = this.configuration.getBoolean("enablePickupHandler", "general", true, "");
	}

	@GradleSideOnly(GradleSide.SERVER)
	public String getDefRes() {
		return defRes;
	}


	@GradleSideOnly(GradleSide.SERVER)
	public boolean isEnabledMinCost() {
		return false;
	}

	@GradleSideOnly(GradleSide.SERVER)
	public boolean isEnablePickupHandler() {
		return enablePickupHandler;
	}
}
