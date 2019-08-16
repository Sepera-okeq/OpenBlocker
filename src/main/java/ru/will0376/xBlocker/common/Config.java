package ru.will0376.xBlocker.common;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

	private Configuration configuration;
	private String serverName;
	private String defRes;
	private String defSign;
	private boolean LimitWarring;

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
		this.serverName = this.configuration.getString("serverName", "general", "", "");
		this.defRes = this.configuration.getString("defRes", "general", "Потому-что!", "Default Reason");
		this.defSign = this.configuration.getString("defSign", "general", "$", "Default Sign");
		this.LimitWarring = this.configuration.getBoolean("limitwarring", "general", true, "");
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
}
