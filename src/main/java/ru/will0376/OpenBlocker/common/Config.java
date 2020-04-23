package ru.will0376.OpenBlocker.common;

import net.minecraftforge.common.config.Configuration;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Config {

	private final Configuration configuration;
	private String serverName;
	private String defRes;
	private String defSign;

	private List<String> whiteList;
	private boolean LimitWarring;
	private boolean deleteBlocked;
	private boolean enabledMinCost;

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
		this.whiteList = Arrays.asList(this.configuration.getStringList("Whitelist", "general", new String[]{"will0376"}, "whitelist"));
		this.serverName = this.configuration.getString("serverName", "general", "", "");
		this.defRes = this.configuration.getString("defRes", "general", "Потому-что!", "Default Reason");
		this.defSign = this.configuration.getString("defSign", "general", "$", "Default Sign");
		this.LimitWarring = this.configuration.getBoolean("limitwarring", "general", true, "");
		this.deleteBlocked = this.configuration.getBoolean("deleteBlocked", "general", true, "Remove blocked items from inventory");
		this.enabledMinCost = this.configuration.getBoolean("enabledMinCost", "general", false, "Enable module?");
	}

	@GradleSideOnly(GradleSide.SERVER)
	public String getServerName() {
		return serverName;
	}

	@GradleSideOnly(GradleSide.SERVER)
	public String getDefRes() {
		return defRes;
	}

	@GradleSideOnly(GradleSide.SERVER)
	public String getDefSign() {
		return defSign;
	}

	@GradleSideOnly(GradleSide.SERVER)
	public boolean getLimitWarring() {
		return LimitWarring;
	}

	@GradleSideOnly(GradleSide.SERVER)
	public List<String> getWhiteList() {
		return whiteList;
	}

	@GradleSideOnly(GradleSide.SERVER)
	public boolean isEnabledMinCost() {
		return enabledMinCost;
	}
}
