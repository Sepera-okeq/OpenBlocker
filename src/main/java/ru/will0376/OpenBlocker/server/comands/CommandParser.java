package ru.will0376.OpenBlocker.server.comands;

import java.util.HashMap;

public class CommandParser {
	/*
	 *  /command <boolArg; Strings:bla bla bla;>
	 * return - hashmap
	 * */
	public HashMap<String, String> commandParser(String args) {
		HashMap<String, String> ret = new HashMap<>();
		for (String tmp : args.split(";")) {
			if (tmp.contains(":")) {//if contains : == stirng.
				ret.put(tmp.split(":")[0], tmp.split(":")[1]);
			} else {// == boolean
				ret.put(tmp, "true");
			}
		}
		return ret;
	}
}
