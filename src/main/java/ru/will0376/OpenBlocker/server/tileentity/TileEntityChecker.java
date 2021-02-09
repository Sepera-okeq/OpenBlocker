package ru.will0376.OpenBlocker.server.tileentity;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GradleSideOnly(GradleSide.SERVER)
public class TileEntityChecker {
	public static Map<List<String>, TEBase> loadedCheckers = new HashMap<>();

	public static boolean checkBlock(EntityPlayer player, String inputReg, World world, BlockPos pos) {
		for (Map.Entry<List<String>, TEBase> listTEBaseEntry : loadedCheckers.entrySet()) {
			for (String s : listTEBaseEntry.getKey()) {
				if (inputReg.contains(s)) return listTEBaseEntry.getValue().handler(player, inputReg, world, pos);
			}
		}
		return false;
	}

	public static void loadTEs() {
		for (TEs value : TEs.values()) {
			if (Loader.isModLoaded(value.getModid())) {
				TEBase teBase = value.getCheckerClass();
				loadedCheckers.put(teBase.getCheckList(), teBase);
			}
		}
	}

	@Getter
	public enum TEs {
		IC2("ic2", new IC2Checker());

		String modid;
		TEBase checkerClass;

		TEs(String modid, TEBase checkerClass) {
			this.modid = modid;
			this.checkerClass = checkerClass;
		}
	}
}
