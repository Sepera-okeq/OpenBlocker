package ru.will0376.OpenBlocker.common.utils;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class FlagData {
	Flag flag;

	public abstract <T> T getData();

	public abstract void setData(Object data);

	public abstract String getLore();

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	@Getter
	public enum Flag {
		AllMeta(AllMetaData.class),
		DisableBox(DisableBoxData.class),
		Temp(TempData.class),
		Limit(LimitData.class),
		Tile(TileData.class),
		Interaction(InteractionData.class);

		public Class<?> clazz;

		Flag(Class<?> clazz) {
			this.clazz = clazz;
		}

		public static FlagData createNewByFlag(Flag flag, Object data) throws IllegalAccessException, InstantiationException {
			FlagData flagData = (FlagData) flag.getClazz().newInstance();
			flagData.setData(data);
			flagData.setFlag(flag);
			return flagData;
		}
	}

	public static class AllMetaData extends FlagData {
		boolean enabled;

		@Override
		public Boolean getData() {
			return enabled;
		}

		@Override
		public void setData(Object data) {
			enabled = Boolean.parseBoolean(String.valueOf(data));
		}

		@Override
		public String getLore() {
			return "Учитываются все метадаты!";
		}
	}

	public static class InteractionData extends FlagData {
		boolean enabled;

		@Override
		public Boolean getData() {
			return enabled;
		}

		@Override
		public void setData(Object data) {
			enabled = Boolean.parseBoolean(String.valueOf(data));
		}

		@Override
		public String getLore() {
			return "Можно использовать в мире";
		}
	}

	public static class TempData extends FlagData {
		boolean enabled;

		@Override
		public Boolean getData() {
			return enabled;
		}

		@Override
		public void setData(Object data) {
			enabled = Boolean.parseBoolean(String.valueOf(data));
		}

		@Override
		public String getLore() {
			return "Временно";
		}
	}

	public static class DisableBoxData extends FlagData {
		boolean enabled;

		@Override
		public Boolean getData() {
			return enabled;
		}

		@Override
		public void setData(Object data) {
			enabled = Boolean.parseBoolean(String.valueOf(data));

		}

		@Override
		public String getLore() {
			return "";
		}
	}

	public static class TileData extends FlagData {
		boolean enabled;

		@Override
		public Boolean getData() {
			return enabled;
		}

		@Override
		public void setData(Object data) {
			enabled = Boolean.parseBoolean(String.valueOf(data));
		}

		@Override
		public String getLore() {
			return "";
		}
	}

	public static class LimitData extends FlagData {
		int limit;

		@Override
		public Integer getData() {
			return limit;
		}

		@Override
		public void setData(Object data) {
			limit = Integer.parseInt(String.valueOf(data));
		}

		@Override
		public String getLore() {
			return "Лимитый предмет";
		}
	}
}
