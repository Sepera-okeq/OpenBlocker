package ru.will0376.OpenBlocker.common;

import java.util.ArrayList;
import java.util.List;

public class ItemList {
	private final List list = new ArrayList();

	public void add(String s) {
		if (s != null && s.indexOf(58) > -1) {
			String[] parts = s.split(":");
			String name = parts[0] + ":" + parts[1];
			int meta = Integer.parseInt(parts[2]);
			this.list.add(new ItemData(name, meta, parts));
		}

	}

	public ItemData get(int i) {
		return (ItemData) this.list.get(i);
	}

	public void clear() {
		this.list.clear();
	}

	public int size() {
		return this.list.size();
	}

	public boolean isEmpty() {
		return this.list.isEmpty();
	}
}
