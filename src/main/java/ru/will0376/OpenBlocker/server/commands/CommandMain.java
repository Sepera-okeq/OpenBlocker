package ru.will0376.OpenBlocker.server.commands;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.will0376.OpenBlocker.common.utils.ChatForm;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@GradleSideOnly(GradleSide.SERVER)

public class CommandMain extends CommandBase {
	private static final List<SubCommands> commandsList = new ArrayList<>();

	@Override
	public String getName() {
		return "ob";
	}

	@Override
	public String getUsage(ICommandSender iCommandSender) {
		return "";
	}

	@Override
	public List<String> getAliases() {
		ArrayList<String> al = new ArrayList<>();
		al.add("OpenBlocker");
		al.add("xbl");
		al.add("xb");
		return al;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		switch (args.length) {
			case 1:
				return getListOfStringsMatchingLastWord(args, Arrays.asList(SubCommands.values()));
			default:
				return getListOfStringsMatchingLastWord(args, SubCommands.valueOf(args[0])
						.getComma()
						.getTabList(args.length));
		}
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			if (sender.canUseCommand(4, "ob.main")) {
				if (args.length >= 1) {
					SubCommands subCommands = SubCommands.valueOf(args[0]);
					subCommands.getComma().execute(server, sender, args);
				} else {
					StringBuilder builder = new StringBuilder();
					for (SubCommands subCommands : commandsList) {
						if (!subCommands.isDisableHelp()) builder.append(subCommands.name())
								.append(" :")
								.append("\n")
								.append(subCommands.getComma().getUsage())
								.append("\n")
								.append("<===>\n");
					}
					sender.sendMessage(new TextComponentString(builder.toString()));
				}
			} else sender.sendMessage(new TextComponentString(ChatForm.prefix_error + "No rights!"));
		} catch (Exception ex) {
			ex.printStackTrace();
			sender.sendMessage(new TextComponentString(ChatForm.prefix_error + TextFormatting.RED + ex.getMessage()));
		}
	}

	@Getter
	public enum SubCommands {
		block(new BlockCommand()),
		craft(new CraftCommand()),
		//		enchant(new EnchantCommand()), //TODO: как-нибудь сделать
		limit(new LimitCommand()),
		blockHotBar(new BlockHotBarCommand()),
		save(new SaveCommand(), true),
		reload(new ReloadCommand(), true),
		test(new TestCommand(), true),
		debug(new DebugCommand(), true);

		CommandAbstract comma;
		boolean disableHelp;

		SubCommands(CommandAbstract comma) {
			this.comma = comma;
			disableHelp = false;
			commandsList.add(this);
		}

		SubCommands(CommandAbstract comma, boolean b) {
			this.comma = comma;
			disableHelp = b;
			commandsList.add(this);
		}
	}
}
