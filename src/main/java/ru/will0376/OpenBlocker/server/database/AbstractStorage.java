package ru.will0376.OpenBlocker.server.database;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Logger;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;
import ru.will0376.OpenBlocker.common.Config;
import ru.will0376.OpenBlocker.common.utils.FlagData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractStorage {
	String url = null;
	String username = null;
	String password = null;

	public abstract void load();

	public abstract void save() throws SQLException;

	public abstract Logger getLogger();

	public interface IDBConnect {
		Connection getConnect() throws SQLException;

		default String insertTableName(String in) {
			return String.format(in, Config.get().getDbTable());
		}

		default void checkTable() {
			try {
				Connection connect = getConnect();
				//connect.prepareStatement(getSQLCheckTable()).executeQuery();
				Statement stmt = connect.createStatement();
				stmt.execute(insertTableName(getSQLCheckTable()));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		default void loadFromDB() {
			try {
				Connection connect = getConnect();

				Statement s = connect.createStatement();
				ResultSet rs = s.executeQuery(insertTableName(getSQLItems()));
				next:
				while (rs.next()) {
					Blocked build = Blocked.builder().build();
					String itemName = rs.getString("itemName");
					String nbt = rs.getString("nbt");
					String reason = rs.getString("reason");
					String status = rs.getString("status");
					String data = rs.getString("data");

					String[] split = itemName.split(":");
					Item byNameOrId = Item.getByNameOrId(split[1]);

					if (byNameOrId == Items.AIR) {
						getDBLogger().error("One of the items from the database is air");
						continue;
					}

					build.setStack(new ItemStack(byNameOrId, 1, Integer.parseInt(split[2])));
					build.setReason(reason);

					for (String s1 : status.split("\\|")) {
						build.getStatus().add(Blocked.Status.valueOf(s1));
					}

					if (!data.isEmpty()) for (String s1 : data.split("\\|")) {
						String[] split1 = s1.split(":");
						build.getData().add(FlagData.Flag.createNewByFlag(FlagData.Flag.valueOf(split1[0]), split1[1]));
					}

					if (nbt.contains("|")) {
						String[] strings = nbt.split("\\|");
						for (String s1 : strings) {
							if (s1.isEmpty() || s1.contains("null")) break;

							Blocked clone = (Blocked) build.clone();
							clone.setNbt(s1);
							BlockHelper.Instance.blockedList.add(clone);

							if (Main.debug)
								getDBLogger().info("Loaded item {} -> {}", itemName, s1.substring(s1.length() - 5));

						}
						continue;
					} else build.setNbt(nbt.contains("null") ? "" : nbt);

					BlockHelper.Instance.blockedList.add(build);
					if (Main.debug) getDBLogger().info("Loaded item {}", itemName);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		default void saveIntoDB() throws SQLException {
			cleanTable();
			for (Blocked blocked : BlockHelper.Instance.blockedList) {
				saveElement(blocked);
			}
		}

		String getSQLCheckTable();

		String getSQLReplacedTable();

		String getSQLItems();

		String getSQLCleanTable();

		default void cleanTable() throws SQLException {
			getConnect().createStatement().execute(insertTableName(getSQLCleanTable()));
		}

		Logger getDBLogger();

		default void saveElement(Blocked blocked) throws SQLException {
			Connection connect = getConnect();
			ItemStack stack = blocked.getStack();
			StringBuilder builder = new StringBuilder();

			List<FlagData> data = blocked.getData();
			for (int i = 0; i < data.size(); i++) {
				FlagData datum = data.get(i);
				builder.append(datum.getFlag()).append(":" + datum.getData());

				if (i != data.size() - 1) builder.append("|");
			}
			Statement stmt = connect.createStatement();

			String collect = blocked.getStatus().stream().map(e -> e.name() + "|").collect(Collectors.joining());
			String s = stack.getItem().getRegistryName() + ":" + stack.getMetadata();
			String format = String.format(getSQLReplacedTable(), Config.get().getDbTable(), s, blocked.getNbt(), blocked
					.getReason(), collect, builder.toString());

			System.out.println(format);

			stmt.execute(format);
		}
	}
}
