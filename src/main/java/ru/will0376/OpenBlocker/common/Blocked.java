package ru.will0376.OpenBlocker.common;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.utils.FlagData;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class Blocked implements Cloneable {
	String nbt;
	String reason;
	ItemStack stack;
	int enchantedID;
	int enchantedLVL;

	@Builder.Default
	List<Status> status = new ArrayList<>();

	@Builder.Default
	List<FlagData> data = new ArrayList<>();

	public static Blocked fromJson(String in) {
		return Main.gson.fromJson(in, Blocked.class);
	}

	public boolean containStatus(Status in) {
		return status.contains(in);
	}

	public Blocked addStatus(Status status) {
		this.status.add(status);
		return this;
	}

	public Blocked addNewFlag(FlagData.Flag flag, Object data) throws InstantiationException, IllegalAccessException {
		this.data.add(FlagData.Flag.createNewByFlag(flag, data));
		return this;
	}

	public String toStr() {
		return Main.gson.toJson(this);
	}

	public boolean containsFlag(FlagData.Flag flag) {
		for (FlagData datum : data) {
			if (flag.getClazz().getSimpleName().equals(datum.getClass().getSimpleName())) return true;
		}
		return false;
	}

	public Object getDataFromFlag(FlagData.Flag flag) {
		for (FlagData datum : data) {
			if (flag.getClazz().isAssignableFrom(datum.getClass())) return datum.getData();
		}
		if (flag == FlagData.Flag.AllMeta || flag == FlagData.Flag.DisableBox) return false;
		return null;
	}

	public boolean getAllMeta() {
		for (FlagData datum : data) {
			if (datum instanceof FlagData.AllMetaData) return datum.getData();
		}
		return false;
	}

	public ArrayList<String> getLore() {
		ArrayList<String> ret = new ArrayList<>();
		ret.add(new TextComponentTranslation("lore.reason", reason).getFormattedText());
		ret.add("---------------");

		for (Status statues : status)
			if (!statues.getLore().isEmpty()) ret.add(statues.getLore());

		for (FlagData datum : getData()) {
			ret.add(datum.getLore() + (datum instanceof FlagData.LimitData ? " : " + datum.getData() : ""));
		}

		return ret;
	}

	public boolean NBTEmpty() {
		return getNbt() == null || getNbt().isEmpty() || getNbt().equals("null");
	}

	@Getter
	public enum Status {
		Blocked("Заблокированно"),
		Craft("Отключен крафт"),
		Limit(),
		Enchant;

		String lore;

		Status() {
			lore = "";
		}

		Status(String s) {
			lore = s;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
