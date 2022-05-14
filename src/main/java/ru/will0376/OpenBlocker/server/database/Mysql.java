package ru.will0376.OpenBlocker.server.database;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.common.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Log4j2
@GradleSideOnly(GradleSide.SERVER)

public class Mysql extends AbstractStorage implements AbstractStorage.IDBConnect {

	public Mysql() {
		url = Config.get().getDbURL();
		username = Config.get().getDbUsername();
		password = Config.get().getDbPassword();
	}

	@Override
	public Connection getConnect() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(url, username, password);
	}

	@Override
	public String getSQLCheckTable() {
		return "CREATE TABLE IF NOT EXISTS %s ( itemName VARCHAR(100), " + //itemstack.getname : itemstack.getmeta
				"nbt LONGTEXT, " + "reason VARCHAR(100), " + "status VARCHAR(250), " + "flags VARCHAR(250), " + "PRIMARY KEY " +
				"(itemName)" + ");";
	}

	@Override
	public String getSQLReplacedTable() {
		return "INSERT INTO %s (itemName,nbt,reason,status,flags) VALUES('%s','%s','%s','%s','%s')" + //itemName,nbt,reason,
				// status,flags
				" ON DUPLICATE KEY UPDATE reason = reason, status = status, flags = flags, nbt =  concat(nbt,'|',VALUES(nbt));"; //reason,status,flags,nbtob;
	}

	@Override
	public String getSQLItems() {
		return "SELECT * FROM %s";
	}

	@Override
	public String getSQLCleanTable() {
		return "TRUNCATE TABLE %s";
	}

	@Override
	public Logger getDBLogger() {
		return log;
	}

	@Override
	public void load() {
		checkTable();
		loadFromDB();
	}

	@Override
	public void save() throws SQLException {
		saveIntoDB();
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
