package ru.will0376.OpenBlocker.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
@AllArgsConstructor
public class FlagData<T> {
	Flags flag;
	T data;

	@Getter
	@AllArgsConstructor
	public enum Flags {
		AllMeta(Boolean.class, "Учитываются все метадаты!"),
		DisableBox(Boolean.class, ""),
		Temp(Boolean.class, "Временно"),
		Limit(Integer.class, "Лимитый предмет"),
		Tile(Boolean.class, ""),
		Interaction(Boolean.class, "Можно использовать в мире");

		public final Class<?> clazz;
		public final String lore;

		public static <T> FlagData<?> createNewByFlag(Flags flags, T data) {
			return new FlagData<>(flags, data);
		}
	}
}
