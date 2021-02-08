package ru.will0376.OpenBlocker.common.utils;

import lombok.Getter;

public abstract class FlagData {
	public abstract <T> T getData();

	public abstract void setData(Object data);

	public abstract String getLore();

	@Getter
	public enum Flag {
		AllMeta(AllMetaData.class),
		DisableBox(DisableBoxData.class),
		Temp(TempData.class),
		Limit(LimitData.class),
		Tile(TileData.class);

		public Class<?> clazz;

		Flag(Class<?> clazz) {
			this.clazz = clazz;
		}

		public static FlagData createNewByFlag(Flag flag, Object data) throws IllegalAccessException, InstantiationException {
			FlagData flagData = (FlagData) flag.getClazz().newInstance();
			flagData.setData(data);
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
			enabled = (boolean) data;
		}

		@Override
		public String getLore() {
			return "Учитываются все метадаты!";
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
			enabled = (boolean) data;
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
			enabled = (boolean) data;
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
			enabled = (boolean) data;
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
			limit = (int) data;
		}

		@Override
		public String getLore() {
			return "Лимитый предмет!";
		}
	}
}
