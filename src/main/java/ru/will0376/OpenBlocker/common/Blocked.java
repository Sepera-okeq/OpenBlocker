package ru.will0376.OpenBlocker.common;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import ru.will0376.OpenBlocker.Main;
import ru.will0376.OpenBlocker.common.utils.Flag;
import ru.will0376.OpenBlocker.common.utils.FlagData;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class Blocked implements Cloneable {
	String reason;
	ItemStack stack;

	@Builder.Default
	List<Status> status = new ArrayList<>();

	@Builder.Default
	List<Flag<?>> flags = new ArrayList<>();

	@Builder.Default
	List<FlagData<?>> data = new ArrayList<>();

	public int getEnchId() {
		return data.stream()
				.filter(e -> e.getAConst() == FlagData.Const.EnchantId)
				.findFirst()
				.orElse(new FlagData<>())
				.getAsInteger();
	}

	public int getEnchLVL() {
		return data.stream()
				.filter(e -> e.getAConst() == FlagData.Const.EnchantLVL)
				.findFirst()
				.orElse(new FlagData<>())
				.getAsInteger();
	}

	public void addData(FlagData.Const c, Object data) {
		this.data.add(new FlagData<>(c, data));
	}

	public String getNBT() {
		return data.stream()
				.filter(e -> e.getAConst() == FlagData.Const.NBTData)
				.findFirst()
				.orElse(new FlagData<>())
				.getString();
	}

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

	public Blocked addNewFlag(Flag.Flags flag, Object data) {
		flags.add(new Flag<>(flag, data));
		return this;
	}

	public String toStr() {
		return Main.gson.toJson(this);
	}

	public Flag<?> getFlagData(Flag.Flags flag) {
		return this.flags.stream().filter(e -> e.getFlag() == flag).findFirst().orElse(new Flag<>());
	}

	public boolean containsFlag(Flag.Flags flag) {
		return this.flags.stream().anyMatch(e -> e.getFlag() == flag);
	}

	public boolean getAllMeta() {
		return flags.stream().anyMatch(e -> e.getFlag() == Flag.Flags.AllMeta);
	}

	public ArrayList<String> getLore() {
		ArrayList<String> ret = new ArrayList<>();
		ret.add(new TextComponentTranslation("lore.reason", reason).getFormattedText());
		ret.add("---------------");

		for (Status statues : status)
			if (!statues.getLore().isEmpty())
				ret.add(statues.getLore());

		for (Flag<?> datum : this.getFlags()) {
			ret.add(datum.getFlag().getLore() + (datum.getFlag() == Flag.Flags.Limit ? " : " + datum.getData() : ""));
		}

		return ret;
	}

	public boolean NBTEmpty() {
		String nbt = getNBT();
		return nbt != null && !nbt.isEmpty() && !nbt.equals("null");
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Getter
	public enum Status {
		Blocked("Заблокированно"),
		Craft("Отключен крафт"),
		Limit(""),
		Enchant("");
		final String lore;

		Status(String s) {
			lore = s;
		}
	}
}
