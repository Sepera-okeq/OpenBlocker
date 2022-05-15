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
public class Data<T> {
	Const aConst;
	T data;

	public boolean getAsBoolean() {
		return getAs(Boolean.class, false);
	}

	public int getAsInteger() {
		return getAs(Integer.class, 0);
	}

	public String getString() {
		return getAs(String.class, "");
	}

	public <C> C getAs(Class<C> clazz, C defaultRet) {
		if (aConst.clazz == clazz)
			return clazz.cast(data);
		return defaultRet;
	}

	@Getter
	@AllArgsConstructor
	public enum Const {
		NBTData(String.class),
		EnchantId(Integer.class),
		EnchantLVL(Integer.class);

		Class<?> clazz;
	}
}
