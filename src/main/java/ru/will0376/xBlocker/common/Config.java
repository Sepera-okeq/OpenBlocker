package ru.will0376.xBlocker.common;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Config {

	private Configuration configuration;
	private String serverName;
	private String defRes;
	private String defSign;

	private List<String> whiteList;
	private boolean LimitWarring;
	private boolean deleteBlocked;

	public boolean isDeleteBlocked() {
		return deleteBlocked;
	}

	public Config(File file) {
		this.configuration = new Configuration(file);
	}

	public void launch() {
		this.load();
		this.setConfigs();
		this.save();
	}

	private void load() {
		this.configuration.load();
	}

	private void save() {
		this.configuration.save();
	}

	private void setConfigs() {
		this.whiteList = Arrays.asList(this.configuration.getStringList("Whitelist", "general", new String[]{"will0376"}, "whitelist"));
		this.serverName = this.configuration.getString("serverName", "general", "", "");
		this.defRes = this.configuration.getString("defRes", "general", "Потому-что!", "Default Reason");
		this.defSign = this.configuration.getString("defSign", "general", "$", "Default Sign");
		this.LimitWarring = this.configuration.getBoolean("limitwarring", "general", true, "");
		this.deleteBlocked = this.configuration.getBoolean("deleteBlocked", "general", true, "Remove blocked items from inventory");
	}

	public String getServerName() {
		return serverName;
	}

	public String getDefRes() {
		return defRes;
	}

	public String getDefSign() {
		return defSign;
	}

	public boolean getLimitWarring() {
		return LimitWarring;
	}

	public List<String> getWhiteList() {
		return whiteList;
	}
}
