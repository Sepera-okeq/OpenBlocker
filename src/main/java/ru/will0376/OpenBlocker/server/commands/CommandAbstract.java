package ru.will0376.OpenBlocker.server.commands;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@GradleSideOnly(GradleSide.SERVER)

public abstract class CommandAbstract {
	String commandName;

	public CommandAbstract(String commandName) {
		this.commandName = commandName;
	}

	public abstract void add(MinecraftServer server, ICommandSender sender, String[] args) throws Exception;

	public abstract void remove(MinecraftServer server, ICommandSender sender, String[] args) throws Exception;

	public abstract List<Argument> getArgList();

	public CommandLine getLine(String[] args) throws Exception {
		return new DefaultParser().parse(getArguments(), args);
	}

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
		if (args.length >= 2) {
			switch (args[1]) {
				case "add":
				case "a":
					add(server, sender, args);
					break;

				case "r":
				case "remove":
					remove(server, sender, args);
					break;
			}
		}
	}

	public String getUsage() {
		List<Argument> argMap = getArgList();
		if (argMap != null)
			return argMap.stream().map(e -> e.getName() + " - " + e.getDesc()).collect(Collectors.joining("\n"));
		return "";
	}

	public List<String> getTabList(int size) {
		if (size == 2) {
			return Arrays.asList("a", "r", "add", "remove");
		} else if (size >= 3) {
			List<Argument> argMap = getArgList();
			if (argMap != null) return argMap.stream().map(e -> "-" + e.getName()).collect(Collectors.toList());
		}
		return Collections.singletonList("");
	}

	public Options getArguments() {
		Options options = new Options();
		for (Argument argument : getArgList()) {
			Option build = Option.builder(argument.getName().replace("-", ""))
					.desc(argument.getDesc())
					.required(argument.isRequired())
					.hasArg(argument.isHasArg())
					.build();
			options.addOption(build);
		}
		return options;
	}

	@Builder(toBuilder = true)
	@Getter
	public static class Argument {
		String name;
		String desc;

		@Builder.Default
		boolean required = false;

		@Builder.Default
		boolean hasArg = false;
	}
}
