package ru.will0376.OpenBlocker.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Flag<T> {
	Flags flag;
	T data;

	public boolean getAsBoolean() {
		return getAs(Boolean.class, false);
	}

	public int getAsInteger() {
		return getAs(Integer.class, 0);
	}

	public String getString() {
		return String.valueOf(data);
	}

	public <C> C getAs(Class<C> clazz, C defaultRet) {
		if (flag.clazz == clazz)
			return clazz.cast(data);
		return defaultRet;
	}

	@Getter
	@AllArgsConstructor
	public enum Flags {
		AllMeta(Boolean.class, "Учитываются все метадаты!"),
		DisableBox(Boolean.class, ""),
		Temp(Boolean.class, "Временно"),
		Limit(Integer.class, "Лимитный предмет"),
		Tile(Boolean.class, ""),
		Interaction(Boolean.class, "Можно использовать в мире");

		public final Class<?> clazz;
		public final String lore;

		public static <T> Flag<?> createNewByFlag(Flags flags, T data) {
			return new Flag<>(flags, data);
		}
	}
}
