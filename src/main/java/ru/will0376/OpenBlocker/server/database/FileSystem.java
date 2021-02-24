package ru.will0376.OpenBlocker.server.database;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import ru.will0376.OpenBlocker.common.BlockHelper;
import ru.will0376.OpenBlocker.common.Blocked;
import ru.will0376.OpenBlocker.server.IO;

import java.util.List;

@Log4j2
public class FileSystem extends AbstractStorage {

	@Override
	public void load() {
		List<Blocked> read = IO.read();
		if (read == null) log.error("File Read Error!");
		else BlockHelper.Instance.blockedList = read;
	}

	@Override
	public void save() {
		IO.write(BlockHelper.Instance.blockedList);
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
